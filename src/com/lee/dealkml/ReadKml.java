package com.lee.dealkml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.zxing.qrcode.decoder.Version.ECB;
import com.lee.twodimensioncode.Creat2DCode;

public class ReadKml {
	private static String kmlPath = "C:\\Users\\Administrator\\Desktop\\kml.txt";
	private static String outPutPath = "C:\\Users\\Administrator\\Desktop";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		readKml(kmlPath);
	}

	public static void readKml(String Path) {
		File file = new File(Path);
		String eachline;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			while ((eachline = br.readLine()) != null) {
				if (eachline.contains("<name>")) {
					eachline.startsWith("<name>");
					System.out.print(eachline.trim());
				} else if (eachline.contains("<coordinates>")) {
					eachline.indexOf(11);
					System.out.println(eachline.trim());
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
