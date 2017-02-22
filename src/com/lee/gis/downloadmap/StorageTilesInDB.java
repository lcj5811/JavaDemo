package com.lee.gis.downloadmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.experimental.theories.Theories;

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

	static long start;
	long end;

	public static void main(String[] args) {
		start = System.currentTimeMillis();

		try {
			new StorageTilesInDB(args[0], args[1], args[2], args[2], args[3], args[4]);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

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
	 * @throws IOException
	 * @throws SQLException
	 */
	public StorageTilesInDB(String tilesPath, String dbPath, String dbName, String provider, String mLastName,
			String mSize) throws IOException, SQLException {
		SQLiteUtil.getInstance()
				.initSQLiteDatabase(dbPath + "/" + dbName + "_" + SystemUtil.getInstance().getTimeName() + ".sqlite");
		this.mProvider = provider;
		this.mDBName = dbName;
		this.mLastName = mLastName;
		this.mSize = Integer.valueOf(mSize);
		doInBackground(tilesPath);
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
		mCompleteNum = 1;
		mThreadPool = Executors.newFixedThreadPool(20);
		new Thread(new Runnable() {
			@Override
			public void run() {
				do {
					if (mThreadPool.isTerminated()) {
						mProgress = 101;
						System.out.println("打包完成");
						SQLiteUtil.getInstance().releaseConnection();
						end = System.currentTimeMillis();
						System.out.println("下载耗时："+SystemUtil.getInstance().getRunTime(start, end));
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
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					insertTiles(tilesPath);
				} catch (IOException | SQLException e) {
					e.printStackTrace();
				}
			}
		};
		mThreadPool.execute(runnable);
	}

	/**
	 * 瓦片存储入数据库
	 * 
	 * @param filePath
	 *            单个瓦片完整路径
	 * @throws IOException
	 * @throws SQLException
	 */
	public void insertTiles(String filePath) throws IOException, SQLException {
		// System.out.println(filePath);
		String[] zxy = filePath.substring(filePath.indexOf(mDBName + "\\"), filePath.indexOf(mLastName))
				.replace(mDBName + "\\", "").split("\\\\");
		long z = Long.valueOf(zxy[0]);
		long x = Long.valueOf(zxy[1]);
		long y = Long.valueOf(zxy[2]);
		double key = Math.pow(2, 2 * z) * z + Math.pow(2, z) * x + y;
		String sql = "insert into tiles (key,provider,tile) values (?,?,?) ";
		if (!isTilesExist((long) key, mProvider)) {
			SQLiteUtil.getInstance().insertTableData(sql,(long) key, mProvider,imageToBlob(filePath));// 插入图片
		} else {
			System.out.println("已存在");
		}
		mCompleteNum++;
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
