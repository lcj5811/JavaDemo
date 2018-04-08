package com.lee.gis.downloadmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lee.util.SystemUtil;

/**
 * @ClassName com.lee.gis.downloadmap.StorageTilesInDB
 * @description 将瓦片存入SQLite数据库
 * @author 凌霄
 * @data 2017年2月20日 下午3:53:36
 */
public class StorageTilesInDB {

	private String mProvider = "";
	private String mDBName = "";
	private String mLastName = "";

	private ExecutorService mThreadPool;

	private int mProgress, mNewProgress, mCompleteNum, mSize;

	private boolean mLocalFile;

	static long start;
	long end;

	// public static void main(String[] args) {
	// start = System.currentTimeMillis();
	// // try {
	// // new StorageTilesInDB(args[0], args[1], args[2], args[2], args[3],
	// // args[4]);
	// // } catch (IOException e) {
	// // e.printStackTrace();
	// // } catch (SQLException e) {
	// // e.printStackTrace();
	// // }
	// try {
	// new StorageTilesInDB("D:\\Program Files (x86)\\GeoServer
	// 2.7.0\\data_dir\\gwc\\raster_UTM");
	// } catch (IOException | SQLException e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * 构造方法
	 * 
	 * @param tilesPath
	 *            瓦片路径
	 * @param dbPath
	 *            数据库路径
	 * @param dbName
	 *            数据库名称
	 * @param provider
	 *            地图供应源名称
	 * @param isLocalFile
	 * @throws IOException
	 * @throws SQLException
	 */
	public StorageTilesInDB(String tilesPath, String dbPath, String dbName, String provider, String lastName,
			String mSize, String name_cn, String min_z, String max_z, String cp,String urltype) throws IOException, SQLException {
		start = System.currentTimeMillis();
		String DB_PathName = dbPath + "/" + dbName + "_" + SystemUtil.getInstance().getTimeName() + ".sqlite";
		SQLiteUtil.getInstance().initSQLiteDatabase(DB_PathName);
		this.mProvider = provider;
		this.mDBName = dbName;
		this.mLastName = lastName;
		this.mSize = Integer.valueOf(mSize);
		this.mLocalFile = false;
		String sql = "insert into metadata (name_cn,name_en,min_z,max_z,format,urltype,cp) values (?,?,?,?,?,?,?) ";
		SQLiteUtil.getInstance().insertData(sql,
				new String[] { name_cn, dbName, min_z, max_z, lastName.substring(1), urltype, cp });
		doInBackground(tilesPath);
	}

	public StorageTilesInDB(String localFilePath, String dbPath, String dbName, String size, String lastName,
			String name_cn, String min_z, String max_z, String cp,String urltype) throws IOException, SQLException {
		start = System.currentTimeMillis();
		String DB_PathName = dbPath + "/" + dbName + "_" + SystemUtil.getInstance().getTimeName() + ".sqlite";
		SQLiteUtil.getInstance().initSQLiteDatabase(DB_PathName);
		this.mProvider = dbName;
		this.mSize = Integer.valueOf(size);
		this.mLocalFile = true;
		this.mLastName = lastName;
		String sql = "insert into metadata (name_cn,name_en,min_z,max_z,format,urltype,cp) values (?,?,?,?,?,?,?) ";
		SQLiteUtil.getInstance().insertData(sql,
				new String[] { name_cn, dbName, min_z, max_z, lastName.substring(1), urltype, cp });
		doInBackground(localFilePath);
	}

	/**
	 * 监听线程
	 * 
	 * @param tilesPath
	 *            瓦片路径
	 * @throws IOException
	 * @throws SQLException
	 */
	protected void doInBackground(String tilesPath) throws IOException, SQLException {
		mCompleteNum = 0;
		mThreadPool = Executors.newFixedThreadPool(40);
		new Thread(new Runnable() {
			@Override
			public void run() {
				do {
					if (mThreadPool.isTerminated()) {
						mProgress = 101;
						System.out.println("打包完成");
						SQLiteUtil.getInstance().releaseConnection();
						end = System.currentTimeMillis();
						System.out.println("打包耗时：" + SystemUtil.getInstance().getRunTime(start, end));
					} else {
						mProgress = (int) (Float.valueOf(mCompleteNum) / mSize * 100);
						if (mProgress != mNewProgress) {
							System.out.println(mProgress + "%");
							mNewProgress = mProgress;
						}
					}
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} while (mProgress < 101);
			}
		}).start();
		findFile(new File(tilesPath));
		mThreadPool.shutdown();
	}

	/**
	 * 存储线程池
	 * 
	 * @param tilesPath
	 *            瓦片路径
	 */
	public void storageThreadPool(final String tilesPath) {
		mThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					if (mLocalFile)
						insertLocalTiles(tilesPath);
					else
						insertTiles(tilesPath);
				} catch (IOException | SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 瓦片存储入数据库
	 * 
	 * @param filePath
	 *            单个瓦片完整路径
	 * @throws IOException
	 * @throws SQLException
	 */
	class tile {
		long key;
		byte[] img;

		public tile(long k, byte[] i) {
			key = k;
			img = i;
		}

		public long getKey() {
			return key;
		}

		public byte[] getBytes() {
			return img;
		}

	}

	ArrayList<tile> myList = new ArrayList<>();

	public void insertTiles(String filePath) throws IOException, SQLException {
		String[] zxy = filePath.substring(filePath.indexOf(mDBName + "\\"), filePath.indexOf(mLastName))
				.replace(mDBName + "\\", "").split("\\\\");
		long z = Long.valueOf(zxy[0]);
		long x = Long.valueOf(zxy[1]);
		long y = Long.valueOf(zxy[2]);
		double key = Math.pow(2, 2 * z) * z + Math.pow(2, z) * x + y;
		if (!isTilesExist((long) key, mProvider)) {
			byte[] img = imageToBlob(filePath);
			synchronized (myList) {
				myList.add(new tile((long) key, img));
				mCompleteNum++;
				if (myList.size() % 500 == 0 || mCompleteNum == mSize) {
					String sql = "insert into tiles (key,provider,tile) values (?,?,?) ";
					SQLiteUtil.getInstance().insertTilesData(sql, myList, mProvider);
					myList.clear();
				}
			}
		} else {
			System.out.println("已存在");
		}
	}

	public void insertLocalTiles(String filePath) throws IOException, SQLException {
		String[] zxy = filePath.substring(filePath.indexOf("EPSG"), filePath.indexOf(mLastName)).replace("EPSG_", "")
				.replace("900913_", "").replace("\\", "_").split("_");
		long z = Long.valueOf(zxy[0]);
		long x = Long.valueOf(zxy[3]);
		long y = Long.valueOf(zxy[4]);
		y = (long) Math.sqrt(Math.pow(2, 2 * z)) - y - 1;
//		double key = Math.pow(2, 2 * z) * z + Math.pow(2, z) * x + y;
		double key = ((z << z) + x << z) + y;
		if (!isTilesExist((long) key, mProvider)) {
			byte[] img = imageToBlob(filePath);
			synchronized (myList) {
				myList.add(new tile((long) key, img));
				mCompleteNum++;
				if (myList.size() % 500 == 0 || mCompleteNum == mSize) {
					String sql = "insert into tiles (key,provider,tile) values (?,?,?) ";
					SQLiteUtil.getInstance().insertTilesData(sql, myList, mProvider);
					myList.clear();
				}
			}
		} else {
			System.out.println("已存在");
		}
	}

	public void BatchTest(long k, byte[] i) {

	}

	/**
	 * 判断瓦片是否已经存入数据库
	 * 
	 * @param key
	 *            主键
	 * @param provider
	 *            地图供应源名称
	 * @return 是否存在，true-存在，false-不存在
	 * @throws SQLException
	 */
	public boolean isTilesExist(Long key, String provider) throws SQLException {
		String sql = "select count(key) from tiles where key = ? and provider = ?";
		return SQLiteUtil.getInstance().isDataExsit(sql, key, provider);
	}

	/**
	 * 迭代目录循环查找瓦片文件
	 * 
	 * @param file
	 *            瓦片目录
	 * @throws IOException
	 * @throws SQLException
	 */
	public void findFile(File file) throws IOException, SQLException {
		// 如果它是一个目录
		if (file.isDirectory()) {
			// 声明目录下所有的文件
			File files[] = file.listFiles();
			// 遍历目录下所有的文件
			for (int i = 0; i < files.length; i++) {
				// 把每个文件 用这个方法进行迭代
				findFile(files[i]);
			}
		} else if (file.exists()) {
			storageThreadPool(file.getAbsolutePath());
		}
	}

	/**
	 * 将图片转成byte[]
	 * 
	 * @param tileFilePath
	 *            瓦片文件路径
	 * @return byte[]
	 * @throws IOException
	 */
	public byte[] imageToBlob(String tileFilePath) throws IOException {
		InputStream mInputStream = new FileInputStream(new File(tileFilePath));
		ByteArrayOutputStream mByteArrayOutputStream = new ByteArrayOutputStream();
		int ch;
		while ((ch = mInputStream.read()) != -1) {
			mByteArrayOutputStream.write(ch);
		}
		byte[] b = mByteArrayOutputStream.toByteArray();
		mByteArrayOutputStream.close();
		mInputStream.close();
		return b;
	}

}
