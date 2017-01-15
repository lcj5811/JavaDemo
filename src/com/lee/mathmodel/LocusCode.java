package com.lee.mathmodel;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @ClassName com.lee.dealoffice.XlsFile
 * @description 读取处理CSV文件,将碱基对进行数值编码
 * @author 凌霄
 * @data 2016年9月17日 上午8:16:15
 */
public class LocusCode {

	static int AA = 0, AT = 0, AC = 0, AG = 0;
	static int TA = 0, TT = 0, TC = 0, TG = 0;
	static int CA = 0, CT = 0, CC = 0, CG = 0;
	static int GA = 0, GT = 0, GC = 0, GG = 0;
	static int lineNumber = 0;

	public static void main(String[] args) throws IOException {
		lineNumber = 0;
		readTxt("D:\\数学建模\\B_基因\\处理数据\\WorkSpace\\27wd.csv", "D:\\数学建模\\B_基因\\处理数据\\WorkSpace\\output.csv");
	}

	@SuppressWarnings("deprecation")
	public static void readTxt(String inputFilePath, String outputFilePath) throws IOException {

		File file = new File(inputFilePath);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;

		FileWriter fw = null;
		BufferedWriter bw = null;

		try {
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);

			File mfile = new File(outputFilePath);
			if (!file.exists()) {
				file.createNewFile();
			}

			fw = new FileWriter(mfile);
			bw = new BufferedWriter(fw);

			String con = "";

			while (dis.available() != 0) {
				for (String WeiDian : dis.readLine().split(",")) {
					con += judgeWeiDian(WeiDian) + ",";
				}
				bw.write(con.substring(0, con.length() - 1) + "\n");
				con = "";
				clearNum();
			}
			bw.close();
			fw.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			fis.close();
			bis.close();
			dis.close();
		}
	}


	private static void clearNum() {
		AA = 0;
		AT = 0;
		AC = 0;
		AG = 0;
		TA = 0;
		TT = 0;
		TC = 0;
		TG = 0;
		CA = 0;
		CT = 0;
		CC = 0;
		CG = 0;
		GA = 0;
		GT = 0;
		GC = 0;
		GG = 0;
	}

	private static int judgeWeiDian(String WeiDian) throws IOException {
		if (WeiDian.equalsIgnoreCase("AA")) {
			return AA = 0;
		} else if (WeiDian.equalsIgnoreCase("AT")) {
			return AT = 4;
		} else if (WeiDian.equalsIgnoreCase("AC")) {
			return AC = 5;
		} else if (WeiDian.equalsIgnoreCase("AG")) {
			return AG = 6;
		} else if (WeiDian.equalsIgnoreCase("TA")) {
			return TA = 4;
		} else if (WeiDian.equalsIgnoreCase("TT")) {
			return TT = 1;
		} else if (WeiDian.equalsIgnoreCase("TC")) {
			return TC = 7;
		} else if (WeiDian.equalsIgnoreCase("TG")) {
			return TG = 9;
		} else if (WeiDian.equalsIgnoreCase("CA")) {
			return CA = 5;
		} else if (WeiDian.equalsIgnoreCase("CT")) {
			return CT = 7;
		} else if (WeiDian.equalsIgnoreCase("CC")) {
			return CC = 2;
		} else if (WeiDian.equalsIgnoreCase("CG")) {
			return CG = 8;
		} else if (WeiDian.equalsIgnoreCase("GA")) {
			return GA = 6;
		} else if (WeiDian.equalsIgnoreCase("GT")) {
			return GT = 9;
		} else if (WeiDian.equalsIgnoreCase("GC")) {
			return GC = 8;
		} else if (WeiDian.equalsIgnoreCase("GG")) {
			return GG = 3;
		}
		return -1;
	}

}
