package com.lee.gis.dealkml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.lee.util.SystemUtil;

public class WriteKml {

	public static void main(String arg[]) {

		File file = new File("D:/test/track");
		final String names[] = file.list();

		String a[] = null;
		File file_kml = null;
		File f_dat = null;
		FileWriter fw = null;
		FileReader fr = null;
		FileReader fr_gj = null;
		BufferedReader br = null;
		BufferedReader br_gj = null;
		BufferedWriter bw = null;
		{
			try {
				for (String f_name : names) {
					SystemUtil.getInstance().copyFile("D:/test/track.kml", "D:/test/kml/" + f_name + ".kml", true);
					createFile("D:/test/kml/", f_name + ".kml");
					file_kml = new File("D:/test/kml/" + f_name + ".kml");
					fw = new FileWriter(file_kml, true);
					fr = new FileReader(file_kml);
					br = new BufferedReader(fr);
					bw = new BufferedWriter(fw);
					String value_gj = "track";
					System.out.println(f_name);
					f_dat = new File("D:/test/track/" + f_name);
					fr_gj = new FileReader(f_dat);
					br_gj = new BufferedReader(fr_gj);

					while (br.readLine() != null) {
					}
					bw.newLine();
					bw.write("<kml>" + "\n\t" + "<Document>" + "\n\t\t" + "<Placemark>" + "\n\t\t\t" + "<name>" + f_name
							+ "</name>" + "\n\t\t\t" + "<styleUrl>#line_style</styleUrl>" + "\n\t\t\t" + "<LineString>"
							+ "\n\t\t\t\t" + "<tessellate>1</tessellate>" + "\n\t\t\t\t" + "<coordinates>"
							+ "\n\t\t\t\t\t");
					while (value_gj != null) {
						value_gj = br_gj.readLine();
						if (value_gj != null) {
							a = value_gj.split(",");
							double[] pt = { Double.valueOf(a[0]), Double.valueOf(a[1]) };
							// System.out.println(pt);
							// Gps pt_s =
							// CoordinateTransform.gcj_To_Gps84(pt[0], pt[1]);
							// System.out.println(pt_s);
							bw.write(pt[1] + "," + pt[0] + ",0 ");
						}
					}

					bw.write(
							"\n\t\t\t\t" + "</coordinates>" + "\n\t\t\t" + "</LineString>" + "\n\t\t" + "</Placemark>");
					bw.write("\n\t" + "</Document>" + "\n" + "</kml>");
					bw.flush();
					fw.flush();
				}
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
					if (fr != null) {
						fr.close();
					}
					if (br != null) {
						br.close();
					}
					if (br_gj != null) {
						br_gj.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 创建文件
	 * 
	 * @param mPath
	 *            文件路径
	 * @param mName
	 *            文件名.扩展名
	 */
	public static void createFile(String mPath, String mName) {

		// path表示你所创建文件的路径
		// String path = "d:/tr/rt";
		File f = new File(mPath);
		if (!f.exists()) {
			f.mkdirs();
		}
		// fileName表示你创建的文件名；为txt类型；
		// String fileName = "test.txt";
		File file = new File(f, mName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}