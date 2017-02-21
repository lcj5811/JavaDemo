package com.lee.gis.downloadmap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lee.util.SystemUtil;

public class DownloaderTileMap {
	private int mMapID;
	private final double[] mTopLeftPoint;
	private final double[] mBottomRightPoint;
	private int[] mZoomLevel;
	private String mMapTilesPath;
	private final String mMapDBName;

	private ExecutorService mThreadPool;
	private int mThreadNum;

	public static String mURL, mLastName;
	public static int progress, completeNum, size;

	public static void main(String[] args) {
		double[] mTopLeftPoint = new double[] { 28.16792, 112.97607 };
		double[] mBottomRightPoint = new double[] { 28.12388, 113.02782 };
		int[] mZoomLevel = new int[] {
				// 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
				10, 11, 12, 13, 14, 15, 16, 17 };
		int mMapID = 1;
		String mMapName = "MyMap";
		String mMapTilesPath = "F:/MapTiles/";
		int mThreadNum = 3;

		new DownloaderTileMap(mTopLeftPoint, mBottomRightPoint, mZoomLevel, mMapID, mMapName, mMapTilesPath,
				mThreadNum);
	}

	/**
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
			String mMapDBName, String mMapTilesPath, int mThreadNum) {

		this.mTopLeftPoint = mTopLeftPoint;
		this.mBottomRightPoint = mBottomRightPoint;
		this.mMapID = mMapID;
		this.mZoomLevel = mZoomLevel;
		this.mMapDBName = mMapDBName;
		this.mMapTilesPath = mMapTilesPath;
		this.mThreadNum = mThreadNum;

		onPreExecute();
		doInBackground();
	}

	/**
	 * 监听线程
	 */
	protected void doInBackground() {
		completeNum = 1;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (mThreadPool.isTerminated()) {
						progress = 100;
						System.out.println("下载完成");
						try {
							String name = mMapTilesPath + mMapDBName + "_" + SystemUtil.getInstance().getTimeName();
							// 打包地图瓦片
							// CompressTileMap.createZip(pathname, name +
							// ".zip");
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					} else {
						progress = (int) (Float.valueOf(completeNum) / size * 100);
						System.out.println(progress + "%");
					}
					try {
						Thread.sleep(2000);
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
						p = zm + "/" + x + "/" + reversedY + ".jpg";
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
		mThreadPool = Executors.newFixedThreadPool(mThreadNum);
		switch (mMapID) {
		case 0:
			mMapTilesPath += "GoogleMap/";
			mLastName = ".jpg";
			mURL = "http://mt1.google.cn/vt/lyrs=s,r&hl=zh-CN&gl=cn&x=";
			break;
		case 1:
			mMapTilesPath += "OpenStreetMap/";
			mLastName = ".png";
			mURL = " http://a.tile.opencyclemap.org/cycle/";
			break;
		case 2:
			mMapTilesPath += "Geoserver/";
			mLastName = ".jpg";
			mURL = "http://192.168.1.19:8888/geoserver/gwc/service/tms/1.0.0/cite:changsha@EPSG:900913@jpeg/";
			break;
		}
		for (int zm : mZoomLevel) {
			int[] TRNum = getTileNum(mTopLeftPoint[0], mTopLeftPoint[1], zm);
			int[] BLNum = getTileNum(mBottomRightPoint[0], mBottomRightPoint[1], zm);
			for (int x = TRNum[0]; x <= BLNum[0]; ++x) {
				for (int y = TRNum[1]; y <= BLNum[1]; ++y) {
					size++;
				}
			}
		}
		System.out.println("共计" + size);
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
			completeNum++;
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
