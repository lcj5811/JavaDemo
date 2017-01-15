package com.lee.dealfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @ClassName com.lee.dealfile.DealTxt
 * @description 读取并按要求处理txt类型文件
 * @author 凌霄
 * @data 2016年9月17日 上午12:55:27
 */
public class DealTxt {

	public static void main(String[] args) {
		readTxt("C:\\Users\\Lee\\Desktop\\论文\\测试\\SQLite.txt");
	}

	public static void readTxt(String Path) {
		File file = new File(Path);
		String eachline;
		Float Start = 0f;
		Float End = 0f;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			while ((eachline = br.readLine()) != null) {
				if (!eachline.contains("MB") && eachline.length() != 0) {
					// System.err.println(eachline);
					if (Start == 0) {
						Start = Float.valueOf(String.valueOf(eachline.subSequence(12, 17)));
					} else {
						End = Float.valueOf(String.valueOf(eachline.substring(12, 17)));
						System.err.println((int) ((End - Start) * 1000));
						Start = 0f;
						End = 0f;
					}
				} else if (eachline.contains("MB")) {
					// System.err.println(eachline.replace("MB", ""));
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
