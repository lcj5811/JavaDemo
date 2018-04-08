package com.lee.gis.downloadmap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lee.util.SystemUtil;

/**
 * @description 从网络或本地下载地图瓦片
 * @author 凌霄
 * @data 2017年2月20日 下午3:53:36
 */
public class DownloaderTileMap {
	private int mMapID;
	private final double[] mTopLeftPoint;
	private final double[] mBottomRightPoint;
	private int[] mZoomLevel;
	private String mMapTilesPath;
	private String mMapDBPath;
	private String mMapDBName;
	private String mMapEndName;
	private String mName_cn;
	private String mMin_z;
	private String mMax_z;
	private String mCenterPoint;
	private String mUrlType;

	private ExecutorService mThreadPool;
	private int mThreadNum;

	private String mURL, mLastName, mLayerName, mIP;
	private int mProgress, mNewProgress, mCompleteNum, mSize;

	static long start;
	long end;

	public static void main(String[] args) {
		start = System.currentTimeMillis();
		// 下载路径
		String mMapTilesPath = "E:/MapTiles/";
		// 本地目录 false & 在线地图 true
		boolean isOnlineFile = true;

		// MapProviderName
		String mMapName = "qxdh";

		// .jpg & .png & .jpeg
		String mEndName = ".jpg";

		String mCenterPoint = "";

		String mName_cn = "测试图";

		String mMin_z = "7";
		String mMax_z = "16";

		// Google & Geoserver
		String mUrlType = "Google";

		if (isOnlineFile) {
			// 地图类别,0-谷歌,1-OpenStreetMap,2-Geoserver,3-TianDi
			int mMapID = 0;
			// Geoserver中图层名称
			String mLayerName = "raster:UTM";
			// ip地址
			String mIP = "localhost:8080";
			// 左上角坐标
			double[] mTopLeftPoint = new double[] { 27.47134543127417, 111.358710512276 };
			// 右下角坐标
			double[] mBottomRightPoint = new double[] { 27.34996749852017, 111.5317640939628 };
			// 中心坐标
			mCenterPoint = (mTopLeftPoint[0] + mBottomRightPoint[0]) / 2.0 + ","
					+ (mTopLeftPoint[1] + mBottomRightPoint[1]) / 2.0;
			// 下载级别
			int[] mZoomLevel = new int[] {
					/* 0, 1, 2, 3, 4, 5, 6 */
					7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17 };
			mMin_z = mZoomLevel[0] + "";
			mMax_z = mZoomLevel[mZoomLevel.length - 1] + "";
			new DownloaderTileMap(mTopLeftPoint, mBottomRightPoint, mZoomLevel, mMapID, mLayerName, mIP, mMapName,
					mMapTilesPath, mEndName, mName_cn, mMin_z, mMax_z, mCenterPoint, mUrlType).start();
		} else {
			// 如果为本地目录 目录名
			String mFolderName = "Raster_quanxian";
			// Geoserver路径+切片路径
			String mLocalFile = "D:\\Program Files (x86)\\GeoServer 2.10.2\\data_dir\\gwc\\" + mFolderName;
			String mSize = SystemUtil.getInstance().getFileNum(new File(mLocalFile)) + "";
			System.out.println("共计" + mSize + "个文件");
			try {
				new StorageTilesInDB(mLocalFile, mMapTilesPath, mMapName, mSize, mEndName, mName_cn, mMin_z, mMax_z,
						mCenterPoint, mUrlType);
			} catch (IOException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 构造方法
	 * 
	 * @param mTopLeftPoint
	 *            左上角坐标
	 * @param mBottomRightPoint
	 *            右下角坐标
	 * @param mZoomLevel
	 *            地图缩放级别
	 * @param mMapID
	 *            地图类别,0-谷歌,1-OpenStreetMap,2-Geoserver
	 * @param mMapDBName
	 *            地图名称
	 * @param mTilePath
	 *            瓦片路径
	 */
	public DownloaderTileMap(double[] mTopLeftPoint, double[] mBottomRightPoint, int[] mZoomLevel, int mMapID,
			String mLayerName, String mIP, String mMapDBName, String mMapTilesPath, String mEndName, String mName_cn,
			String mMin_z, String mMax_z, String mCenterPoint, String mURLType) {

		this.mTopLeftPoint = mTopLeftPoint;
		this.mBottomRightPoint = mBottomRightPoint;
		this.mMapID = mMapID;
		this.mLayerName = mLayerName;
		this.mIP = mIP;
		this.mZoomLevel = mZoomLevel;
		this.mMapDBName = mMapDBName;
		this.mMapTilesPath = mMapTilesPath;
		this.mMapDBPath = mMapTilesPath;
		this.mMapEndName = mEndName;
		this.mName_cn = mName_cn;
		this.mMin_z = mMin_z;
		this.mMax_z = mMax_z;
		this.mCenterPoint = mCenterPoint;
		this.mUrlType = mURLType;
	}

	public void start() {
		onPreExecute();
		doInBackground();
	}

	/**
	 * 监听线程
	 */
	protected void doInBackground() {
		mCompleteNum = 1;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (mThreadPool.isTerminated()) {
						mProgress = 100;
						System.out.println("下载完成");
						end = System.currentTimeMillis();
						System.out.println("下载耗时：" + SystemUtil.getInstance().getRunTime(start, end));
						try {
							// 打包地图瓦片为ZIP,已经弃用
							// CompressTileMap.createZip(mMapTilesPath,
							// mMapDBName + ".zip");
							// 打包地图瓦片为SQLite
							new StorageTilesInDB(mMapTilesPath, mMapDBPath, mMapDBName, mMapDBName, mLastName,
									String.valueOf(mSize), mName_cn, mMin_z, mMax_z, mCenterPoint, mUrlType);
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
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
				}
			}
		}).start();
		for (int zm : mZoomLevel) {
			int[] TRNum = getTileNum(mTopLeftPoint[0], mTopLeftPoint[1], zm);
			int[] BLNum = getTileNum(mBottomRightPoint[0], mBottomRightPoint[1], zm);
			for (int x = TRNum[0]; x <= BLNum[0]; ++x) {
				String path = mMapTilesPath + zm + "/" + x + "/";
				for (int y = TRNum[1]; y <= BLNum[1]; ++y) {
					String p = null;
					switch (mMapID) {
					case 0:
						p = x + "&y=" + y + "&z=" + zm;
						break;
					case 1:
						p = zm + "/" + x + "/" + y + ".png";
						break;
					case 2:
						int reversedY = (1 << zm) - y - 1;
						p = zm + "/" + x + "/" + reversedY + mMapEndName;
					case 3:
						p = "&x=" + x + "&y=" + y + "&l=" + zm;
					}
					loadImage(mURL + p, String.valueOf(y), path);
					// System.out.println(mURL + p);
				}
			}
		}
		mThreadPool.shutdown();
	}

	/**
	 * 下载预处理
	 */
	protected void onPreExecute() {
		switch (mMapID) {
		case 0:
			mThreadNum = 2;
			mMapTilesPath += "GoogleMap/" + mMapDBName + "/";
			mLastName = mMapEndName;
			// 混合卫星图-有偏移
			// mURL = "http://mt1.google.cn/vt/lyrs=s,r&hl=zh-CN&gl=cn&x=";
			// 卫星图-无偏移
			mURL = "http://mt2.google.cn/vt/lyrs=s&hl=zh-CN&gl=cn&x=";
			break;
		case 1:
			mThreadNum = 3;
			mMapTilesPath += "OpenStreetMap/" + mMapDBName + "/";
			mLastName = ".png";
			mURL = "http://a.tile.opencyclemap.org/cycle/";
			break;
		case 2:
			mThreadNum = 10;
			mMapTilesPath += "Geoserver/" + mMapDBName + "/";
			mLastName = mMapEndName;
			// mURL =
			// "http://192.168.1.19:8888/geoserver/gwc/service/tms/1.0.0/cite:changsha@EPSG:900913@jpeg/";
			mURL = "http://" + mIP + "/geoserver/gwc/service/tms/1.0.0/" + mLayerName + "@EPSG:900913@jpeg/";
			break;
		case 3:
			mThreadNum = 5;
			mMapTilesPath += "TianDiMap/" + mMapDBName + "/";
			mLastName = ".jpg";
			mURL = "http://t1.tianditu.cn/DataServer?T=img_w";
			break;
		}
		for (int zm : mZoomLevel) {
			int[] TRNum = getTileNum(mTopLeftPoint[0], mTopLeftPoint[1], zm);
			int[] BLNum = getTileNum(mBottomRightPoint[0], mBottomRightPoint[1], zm);
			for (int x = TRNum[0]; x <= BLNum[0]; ++x) {
				for (int y = TRNum[1]; y <= BLNum[1]; ++y) {
					mSize++;
				}
			}
		}
		mThreadPool = Executors.newFixedThreadPool(mThreadNum);
		System.out.println("共计" + mSize);
	}

	/**
	 * 下载线程池
	 * 
	 * @param url
	 *            瓦片地址
	 * @param name
	 *            地图名称
	 * @param path
	 *            地图路径
	 */
	public void loadImage(final String url, final String name, final String path) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				Download(path, url, name);
			}
		};
		mThreadPool.execute(runnable);
	}

	/**
	 * 下载瓦片方法
	 * 
	 * @param path
	 *            地图路径
	 * @param url
	 *            瓦片地址
	 * @param name
	 *            地图名称
	 */
	private void Download(String path, String url, String name) {
		try {
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String sName = name + mLastName;
			File img = new File(path + sName);
			if (!img.exists()) {
				InputStream mInputStream = new URL(url).openStream();
				BufferedInputStream in = new BufferedInputStream(mInputStream);
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(img));
				byte[] buf = new byte[2048];
				int length = in.read(buf);
				while (length != -1) {
					out.write(buf, 0, length);
					length = in.read(buf);
				}
				in.close();
				out.close();
				// System.out.println("已下载" + name);
			} else {
				// System.out.println("已存在" + name);
			}
			mCompleteNum++;
		} catch (Exception e) {
			Download(path, url, name);
			System.out.println("重新下载" + name);
		}
	}

	/**
	 * 获取瓦片坐标
	 * 
	 * @param lat
	 *            纬度
	 * @param lon
	 *            经度
	 * @param zoom
	 *            级别
	 * @return int[]{X,Y}
	 */
	public static int[] getTileNum(final double lat, final double lon, final int zoom) {
		int xtile = (int) Math.floor((lon + 180) / 360 * (1 << zoom));
		int ytile = (int) Math
				.floor((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2
						* (1 << zoom));
		if (xtile < 0)
			xtile = 0;
		if (xtile >= (1 << zoom))
			xtile = ((1 << zoom) - 1);
		if (ytile < 0)
			ytile = 0;
		if (ytile >= (1 << zoom))
			ytile = ((1 << zoom) - 1);
		return new int[] { xtile, ytile };
	}

}
