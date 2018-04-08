package com.lee.algorithm.geneAnalyze;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @ClassName com.lee.dealfile.ReLief
 * @description ReLief算法
 * @author 凌霄
 * @data 2016年9月17日 下午9:25:29
 */
public class Relief {

	static int Distance = 996;
	static ArrayList<Double> QuanZhong = new ArrayList<Double>();
	static ArrayList<Double> QZ = new ArrayList<Double>();

	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 115; i++) {
			QuanZhong.add(.0);
		}
		readTxt("D:\\数学建模\\B_基因\\处理数据\\WorkSpace\\Q4\\115-output.csv");
	}

	@SuppressWarnings("deprecation")
	public static void readTxt(String inputFilePath) throws Exception {

		File file = new File(inputFilePath);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;

		ArrayList<String[]> mLine = null;

		try {
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);

			mLine = new ArrayList<String[]>();

			while (dis.available() != 0) {
				mLine.add(dis.readLine().split(","));
			}
			calcOutput(mLine);

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

	private static void calcOutput(ArrayList<String[]> mLine) throws Exception {
		int dis = 0, lineNum = 1;
		String NearestString = "";
		ArrayList<String> Nearest = new ArrayList<String>();
		for (String a[] : mLine) {
			for (int j = 0; j < 500; j++) {
				dis = Distance(a, mLine.get(j));
				if (dis < Distance && dis != 0) {
					Distance = dis;
					lineNum = j;
				}
			}
			NearestString += lineNum + ",";
			// System.out.println(i + ":" + lineNum + ":" + Distance);
			dis = 0;
			lineNum = 0;
			Distance = 996;
			for (int j = 500; j < 1000; j++) {
				// System.out.println(j);
				dis = Distance(a, mLine.get(j));
				if (dis < Distance && dis != 0) {
					Distance = dis;
					lineNum = j;
				}
			}
			NearestString += lineNum;
			System.out.println(NearestString);
			Nearest.add(NearestString);
			// System.out.println(NearestString);
			// System.out.println(Nearest.size());
			// System.out.println(i++ + ":" + lineNum + ":" + Distance);
			dis = 0;
			lineNum = 0;
			Distance = 996;
			NearestString = "";
		}

		String[] a;
		int mlineNum = 0;
		String con = "";

		File file = new File("D:\\数学建模\\B_基因\\处理数据\\WorkSpace\\Q4\\权重115.csv");
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter mFW = new FileWriter(file);
		BufferedWriter mBW = new BufferedWriter(mFW);

		for (String string : Nearest) {
			a = string.split(",");
			if (mlineNum < 500) {
				QuanZhong2(mLine.get(mlineNum++), mLine.get(Integer.valueOf(a[1])), mLine.get(Integer.valueOf(a[0])));
			} else {
				QuanZhong2(mLine.get(mlineNum++), mLine.get(Integer.valueOf(a[0])), mLine.get(Integer.valueOf(a[1])));
			}
			QuanZhong = new ArrayList<Double>();
			QuanZhong = QZ;
			QZ = new ArrayList<Double>();
			for (double qz : QuanZhong) {
				con += qz + ",";
			}
			System.err.println("");
			mBW.write(con + "\n");
			con = "";
		}

		for (double string : QuanZhong) {
			con += string + ",";
		}

		mBW.write(con);
		mBW.close();
		mFW.close();

	}

	/**
	 * @param YB样本
	 * @param NMYB不同类别样本
	 * @param NHYB相同类别样本
	 */
	private static void QuanZhong2(String[] YB, String[] NMYB, String[] NHYB) {
		Double D_NHX = .0, D_NMX = .0;
		for (int i = 0; i < YB.length; i++) {
			D_NHX = YB[i].equalsIgnoreCase(NHYB[i]) ? .0 : 1.0;
			D_NMX = YB[i].equalsIgnoreCase(NMYB[i]) ? .0 : 1.0;
			QZ.add(QuanZhong(QuanZhong.get(i), D_NHX, D_NMX));
		}
	}

	private static int Distance(String[] YB, String[] ComYB) {
		int dis = 0;
		for (int i = 0; i < YB.length; i++) {
			if (!YB[i].equalsIgnoreCase(ComYB[i])) {
				dis++;
			}
		}
		return dis;
	}

	private static double QuanZhong(double SelfQuanZhong, double D_NHX, double D_NMX) {
		// System.out.println(SelfQuanZhong + D_NMX / (115 * 1000) - D_NHX /
		// (115 * 1000) + "===========");
		return SelfQuanZhong + D_NMX / (115 * 1000) - D_NHX / (115 * 1000);
	}

}
