package com.lee.gis.downloadmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName com.lee.gis.downloadmap.StorageTilesInDB
 * @description 将瓦片存入SQLite数据库
 * @author 凌霄
 * @data 2017年2月20日 下午3:53:36
 */
public class StorageTilesInDB {

	ArrayList<String> tilesList = new ArrayList<String>();

	public static void main(String[] args) {
		// SQLiteUtil.getInstance().initSQLiteDatabase("F:/MyMap.sqlite");
		try {
			new StorageTilesInDB("F:/MapTiles/Geoserver", "F:/MapTiles", "MapTiles", "ChangSha");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public StorageTilesInDB(String tilesPath, String dbPath, String dbName, String provider) throws IOException {
		SQLiteUtil.getInstance().initSQLiteDatabase(dbPath + "/" + dbName + ".sqlite");
		tilesList = findFile(new File(tilesPath), tilesList);
		insertTiles(tilesList, provider);
	}

	public void insertTiles(ArrayList<String> tilesList, String provider) throws IOException {
		for (String string : tilesList) {
			String[] zxy = string.substring(string.indexOf("r\\"), string.indexOf(".jpg")).replace("r\\", "")
					.split("\\\\");
			long z = Long.valueOf(zxy[0]);
			long x = Long.valueOf(zxy[1]);
			long y = Long.valueOf(zxy[2]);
			double key = Math.pow(2, 2 * z) * z + Math.pow(2, z) * x + y;
			String sql = "insert into tiles (key,provider,tile) values (?,?,?) ";
			List<Object> list = new ArrayList<Object>();// 拼装参数
			System.out.println((long) key);
			list.add(0, (long) key);
			list.add(1, provider);
			list.add(2, imageToBlob(string));
			SQLiteUtil.getInstance().insertTableData(sql, list);// 插入图片
		}
	}

	public ArrayList<String> findFile(File file, ArrayList<String> arrayList) {
		if (file.isDirectory()) { // 否则如果它是一个目录
			File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
			for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
				findFile(files[i], arrayList); // 把每个文件 用这个方法进行迭代
			}
		} else if (file.exists()) {
			arrayList.add(file.getAbsolutePath());
		}
		return arrayList;
	}

	public byte[] imageToBlob(String tileFilePath) throws IOException {
		// 将图片转成byte[]
		// InputStream inputStream = new FileInputStream(new File("D:/a.jpeg"));
		InputStream inputStream = new FileInputStream(new File(tileFilePath));
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		int ch;
		while ((ch = inputStream.read()) != -1) {
			byteArrayOutputStream.write(ch);
		}
		byte[] b = byteArrayOutputStream.toByteArray();
		byteArrayOutputStream.close();
		return b;
	}

}
