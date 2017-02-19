package com.lee.mathmodel.geneAnalyze;

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
 * @description 读取处理CSV文件，统计位点基因类型数量
 * @author 凌霄
 * @data 2016年9月17日 上午8:16:15
 */
public class LocusNum {

	static int AA = 0, AT = 0, AC = 0;
	static int TT = 0, TC = 0, TG = 0;
	static int CC = 0, CG = 0;
	static int GG = 0, AG = 0;
	static int lineNumber = 0;

	public static void main(String[] args) throws Exception {
		lineNumber = 0;
		readTxt("D:\\数学建模\\B_基因\\处理数据\\WorkSpace\\Q4\\509-1.csv",
				"D:\\数学建模\\B_基因\\处理数据\\WorkSpace\\Q4\\509-1-output.csv");
	}

	@SuppressWarnings("deprecation")
	public static void readTxt(String inputFilePath, String outputFilePath) throws Exception {

		File file = new File(inputFilePath);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;

		try {
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);

			String line = "";
			String con = "AA" + "," + "TT" + "," + "CC" + "," + "GG" + "," + "AC" + "," + "AT" + "," + "AG" + "," + "TC"
					+ "," + "TG" + "," + "CG" + "\n";

			while (dis.available() != 0) {
				line = dis.readLine();
				for (String a : line.split(",")) {
					judgeWeiDian(a);
				}
				con += outPut();
				System.out.println(con);
				clearNum();
			}
			writeWeiDianMsg(outputFilePath, con);

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

	private static void writeWeiDianMsg(String NewFileName, String con) throws Exception {
		File file = new File(NewFileName);
		FileWriter mFW = null;
		BufferedWriter mBW = null;
		if (!file.exists()) {
			file.createNewFile();
		}
		mFW = new FileWriter(file);
		mBW = new BufferedWriter(mFW);
		mBW.write(con);
		mBW.close();
		mFW.close();
	}

	private static String outPut() throws IOException {
		return AA + "," + TT + "," + CC + "," + GG + "," + AC + "," + AT + "," + AG + "," + TC + "," + TG + "," + CG
				+ "\n";
	}

	private static void clearNum() {
		AA = 0;
		AT = 0;
		AC = 0;
		AG = 0;
		TT = 0;
		TC = 0;
		TG = 0;
		CC = 0;
		CG = 0;
		GG = 0;
	}

	private static void judgeWeiDian(String WeiDian) throws IOException {

		if (WeiDian.equalsIgnoreCase("AA")) {
			AA++;
		} else if (WeiDian.equalsIgnoreCase("AT")) {
			AT++;
		} else if (WeiDian.equalsIgnoreCase("AC")) {
			AC++;
		} else if (WeiDian.equalsIgnoreCase("AG")) {
			AG++;
		} else if (WeiDian.equalsIgnoreCase("TA")) {
			AT++;
		} else if (WeiDian.equalsIgnoreCase("TT")) {
			TT++;
		} else if (WeiDian.equalsIgnoreCase("TC")) {
			TC++;
		} else if (WeiDian.equalsIgnoreCase("TG")) {
			TG++;
		} else if (WeiDian.equalsIgnoreCase("CA")) {
			AC++;
		} else if (WeiDian.equalsIgnoreCase("CT")) {
			TC++;
		} else if (WeiDian.equalsIgnoreCase("CC")) {
			CC++;
		} else if (WeiDian.equalsIgnoreCase("CG")) {
			CG++;
		} else if (WeiDian.equalsIgnoreCase("GA")) {
			AG++;
		} else if (WeiDian.equalsIgnoreCase("GT")) {
			TG++;
		} else if (WeiDian.equalsIgnoreCase("GC")) {
			CG++;
		} else if (WeiDian.equalsIgnoreCase("GG")) {
			GG++;
		}

	}

}
