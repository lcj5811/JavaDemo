package com.lee.gis.downloadmap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloaderTileMap {
	private int ws;

	private final double[] TL;
	private final double[] BR;
	private int[] z;
	private final String MN;
	private ExecutorService threadPool;
	public static String p, pathname, lastname, maptype, url;
	public static int progress, completeNum, size;

	public static void main(String[] args) {
		double[] aTL = new double[] { 26.88319, 112.44713 };
		double[] aBR = new double[] { 26.69839, 112.55836 };
		// new MapDownloader(aTR, aBL, 1, "fushoushan");
		new DownloaderTileMap(aTL, aBR, 0, "t");
	}

	public DownloaderTileMap(double[] aTL, double[] aBR, int aw, String MapName) {
		TL = aTL;
		BR = aBR;
		ws = aw;
		MN = MapName;
		onPreExecute();
		doInBackground();
	}

	protected void doInBackground() {
		completeNum = 1;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (threadPool.isTerminated()) {
						progress = 100;
						System.out.println("下载完成");
						try {
							String name = "F:/test2/" + MN + "_" + maptype
									+ getTimeName();
							CompressTileMap.createZip(pathname, name + ".zip");
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
		for (int zm : z) {
			int[] TRNum = getTileNum(TL[0], TL[1], zm);
			int[] BLNum = getTileNum(BR[0], BR[1], zm);
			for (int x = TRNum[0]; x <= BLNum[0]; ++x) {
				String path = pathname + zm + "/" + x + "/";
				for (int y = TRNum[1]; y <= BLNum[1]; ++y) {
					String p = null;
					switch (ws) {
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
					loadImage(url + p, String.valueOf(y), path);
				}
				// try {
				// Thread.sleep(200);
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
			}
		}
		threadPool.shutdown();
	}

	protected void onPreExecute() {
		threadPool = Executors.newFixedThreadPool(40);
		switch (ws) {
		case 0:
			pathname = "F:/test3/谷歌卫星图/";
			lastname = ".jpg";
			maptype = "GM";
			url = "http://mt1.google.cn/vt/lyrs=s,r&hl=zh-CN&gl=cn&x=";
			z = new int[] { 11,13,15,17,19 };
			break;
		case 1:
			pathname = "F:/test2/OSM地形图/";
			lastname = ".png";
			maptype = "OSM";
			url = " http://a.tile.opencyclemap.org/cycle/";
			z = new int[] { 10, 12, 14, 16, 18, 20 };
			break;
		}
		for (int zm : z) {
			int[] TRNum = getTileNum(TL[0], TL[1], zm);
			int[] BLNum = getTileNum(BR[0], BR[1], zm);
			for (int x = TRNum[0]; x <= BLNum[0]; ++x) {
				for (int y = TRNum[1]; y <= BLNum[1]; ++y) {
					size++;
				}
			}
		}
		System.out.println("共计" + size);
	}

	public void loadImage(final String url, final String name, final String path) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				Download(path, url, name);
			}
		};
		threadPool.execute(runnable);
	}

	private void Download(String path, String url, String name) {
		try {
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			InputStream mInputStream = new URL(url).openStream();
			BufferedInputStream in = new BufferedInputStream(mInputStream);
			String sName = name + lastname;
			File img = new File(path + sName);
			if (!img.exists()) {
				BufferedOutputStream out = new BufferedOutputStream(
						new FileOutputStream(img));
				byte[] buf = new byte[2048];
				int length = in.read(buf);
				while (length != -1) {
					out.write(buf, 0, length);
					length = in.read(buf);
				}
				in.close();
				out.close();
//				System.out.println("已下载" + name);
			} else {
				// System.out.println("已存在" + name);
			}
			completeNum++;
		} catch (Exception e) {
			Download(path, url, name);
			System.out.println("重新下载" + name);
		}
	}

	public static int[] getTileNum(final double lat, final double lon,
			final int zoom) {
		int xtile = (int) Math.floor((lon + 180) / 360 * (1 << zoom));
		int ytile = (int) Math
				.floor((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1
						/ Math.cos(Math.toRadians(lat)))
						/ Math.PI)
						/ 2 * (1 << zoom));
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

	public static String getTimeName() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
		df.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
		return (df.format(new Date()));
	}

	public void savePoint(String Path, String point) {
		File f = new File(Path);
		if (!f.exists()) {
			try {
				File parent = f.getParentFile();
				parent.mkdirs();
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(f, true);
			bw = new BufferedWriter(fw);
			bw.write(point);
			bw.newLine();
			bw.flush();
			fw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) {
					bw.close();
				}
				if (fw != null) {
					fw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
