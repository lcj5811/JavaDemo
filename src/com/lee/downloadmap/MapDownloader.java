package com.lee.downloadmap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MapDownloader {
	private double[] TR = new double[] { 28.170353, 112.961396 };
	private double[] BL = new double[] { 28.129864, 113.049338 };
	private int[] z = new int[] { 13 };
	private ExecutorService threadPool;
	private Hashtable<String, Integer> taskCollection;
	private static final int IMAGE_DOWNLOAD_FAIL_TIMES = 3;

	public MapDownloader() {
		threadPool = Executors.newFixedThreadPool(10);
		taskCollection = new Hashtable<String, Integer>();
		for (int az : z) {
			int[] TRNum = getTileNum(TR[0], TR[1], az);
			int[] BLNum = getTileNum(BL[0], BL[1], az);
			for (int x = TRNum[0]; x <= BLNum[0]; ++x) {
				String path = "D:/test2/" + az + "/" + x + "/";
				for (int y = TRNum[1]; y <= BLNum[1]; ++y) {
					String a = "http://mt1.googl.cn/vt/lyrs=s,r&hl=zh-CN&gl=cn&x=" + x + "&y=" + y + "&z=" + az;
					loadImage(a, String.valueOf(y), path);
					System.out.println(path + y);
				}
			}
		}
	}

	public int[] getTileNum(final double lat, final double lon, final int zoom) {
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

	public static void main(String[] args) {
		new MapDownloader();
	}

	public void loadImage(final String url, final String name, final String path) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					File dir = new File(path);
					if (!dir.exists()) {
						dir.mkdirs();
					}
					// 创建流
					BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
					// 生成图片名
					// int index = imgUrl.lastIndexOf("/");
					String sName = name + ".jpg";
					// 存放地址
					File img = new File(path + sName);
					// 生成图片
					BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(img));
					byte[] buf = new byte[2048];
					int length = in.read(buf);
					while (length != -1) {
						out.write(buf, 0, length);
						length = in.read(buf);
					}
					in.close();
					out.close();
				} catch (Exception e) {
					// e.printStackTrace();
				}
				// if (taskCollection.get(url) != null) {
				// int times = taskCollection.get(url);
				// System.out.println("Re-" + url + ":" + times);
				// if (!new File(path + name).exists()
				// && times < IMAGE_DOWNLOAD_FAIL_TIMES) {
				// times++;
				// taskCollection.put(url, times);
				// loadImage(url, name, path);
				// System.out.println("Re-download " + url + ":" + times);
				// }
				//// for (int i = 0; i < taskCollection.size(); i++) {
				//// System.out.println(taskCollection.get(i));
				//// }
				// }
			}
		};
		taskCollection.put(url, 0);
		threadPool.execute(runnable);
	}
}