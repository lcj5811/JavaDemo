package com.lee.mathmodel.geneAnalyze;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.naming.InitialContext;

/**
 * @ClassName com.lee.dealfile.ReLief_Gene
 * @description Gene ReLife算法
 * @author 凌霄
 * @data 2016年9月19日 下午3:52:21
 */
public class Relief_Gene {

	static HashMap<Integer, Double> WeiDianQuanZhong = new HashMap<>();
	static HashMap<Integer, Integer> ColID2GeneID = new HashMap<>();
	static HashMap<Integer, Integer> WeiDianID2GeneID = new HashMap<>();

	static ArrayList<Double> QuanZhong = new ArrayList<Double>();
	static ArrayList<String> GeneCon = new ArrayList<String>();
	static ArrayList<Double> QZ = new ArrayList<Double>();

	static ArrayList<Double> DIS = new ArrayList<Double>();

	static ArrayList<String> Nearest = new ArrayList<String>();

	static double Distance = 50000;

	static double dis2 = 50000;

	public static void main(String[] args) throws Exception {
		readWeiDianQuanZhong("D:\\数学建模\\B_基因\\处理数据\\WorkSpace\\Q3\\位点权重202.csv");
		readColID2GeneID("D:\\数学建模\\B_基因\\处理数据\\WorkSpace\\Q3\\CID2GID.csv");
		readGeneFile("D:\\数学建模\\B_基因\\处理数据\\WorkSpace\\Q3\\Gene202.csv");
		// calcDistanceOfGene(GeneCon);
		CalcOutput("D:\\数学建模\\B_基因\\处理数据\\WorkSpace\\Q3\\1000Gene.csv");
	}

	private static void CalcOutput(String FilePath) throws Exception {

		for (int i = 0; i < 26; i++) {
			QuanZhong.add(.0);
		}

		File mFile = new File(FilePath);
		FileInputStream mFIS = new FileInputStream(mFile);
		BufferedInputStream mBIS = new BufferedInputStream(mFIS);
		DataInputStream mDIS = new DataInputStream(mBIS);
		while (mDIS.available() != 0) {
			Nearest.add(mDIS.readLine());
		}
		mDIS.close();
		mBIS.close();
		mFIS.close();

		String[] a;
		int mlineNum = 0;

		for (String string : Nearest) {
			a = string.split(",");
			mlineNum++;
			if (mlineNum < 501) {
				QuanZhong2(GeneCon.get(0).split(","), GeneCon.get(mlineNum).split(","),
						GeneCon.get(Integer.valueOf(a[1])).split(","), GeneCon.get(Integer.valueOf(a[0])).split(","));
			} else {
				QuanZhong2(GeneCon.get(0).split(","), GeneCon.get(mlineNum).split(","),
						GeneCon.get(Integer.valueOf(a[0])).split(","), GeneCon.get(Integer.valueOf(a[1])).split(","));
			}
			QuanZhong = new ArrayList<Double>();
			QuanZhong = QZ;
			QZ = new ArrayList<Double>();
		}
		for (double qz : QuanZhong) {
			System.out.println(qz);
		}
	}

	private static void calcDistanceOfGene(ArrayList<String> FileCon) {
		double dis = 0, lineNum = 1;
		String NearestString = "";
		String WeiDianID[] = FileCon.get(0).split(",");
		for (int i = 1; i < FileCon.size(); i++) {
			for (int j = 1; j < 501; j++) {
				dis = DistanceOfGenes(FileCon.get(i).split(","), FileCon.get(j).split(","), WeiDianID);
				if (dis < Distance && dis != 0) {
					Distance = dis;
					lineNum = j;
				}
			}
			NearestString += lineNum + ",";
			dis = 0;
			lineNum = 0;
			Distance = 5000;
			for (int j = 501; j < 1001; j++) {
				dis = DistanceOfGenes(FileCon.get(i).split(","), FileCon.get(j).split(","), WeiDianID);
				if (dis < Distance && dis != 0) {
					Distance = dis;
					lineNum = j;
				}
			}
			NearestString += lineNum;
			System.out.println(NearestString);
			dis = 0;
			lineNum = 0;
			Distance = 996;
			NearestString = "";
		}

	}

	private static void QuanZhong2(String[] ID, String[] YB, String[] NMYB, String[] NHYB) {
		Double D_NHX = .0, D_NMX = .0;
		for (int i = 0; i < 26; i++) {
			D_NHX = DistanceOfGene(YB, NHYB, ID).get(i);
			D_NMX = DistanceOfGene(YB, NMYB, ID).get(i);
			QZ.add(QuanZhong(QuanZhong.get(i), D_NHX, D_NMX));
		}
	}

	private static double QuanZhong(double SelfQuanZhong, double D_NHX, double D_NMX) {
		return SelfQuanZhong + D_NMX / (26 * 1000) - D_NHX / (26 * 1000);
	}

	private static void readGeneFile(String FilePath) throws Exception {
		File mFile = new File(FilePath);
		FileInputStream mFIS = new FileInputStream(mFile);
		BufferedInputStream mBIS = new BufferedInputStream(mFIS);
		DataInputStream mDIS = new DataInputStream(mBIS);
		while (mDIS.available() != 0) {
			GeneCon.add(mDIS.readLine());
		}
		mDIS.close();
		mBIS.close();
		mFIS.close();
	}

	private static void readWeiDianID2GeneID(String FilePath) throws Exception {
		String[] cont = new String[] {};
		File mFile = new File(FilePath);
		FileInputStream mFIS = new FileInputStream(mFile);
		BufferedInputStream mBIS = new BufferedInputStream(mFIS);
		DataInputStream mDIS = new DataInputStream(mBIS);
		while (mDIS.available() != 0) {
			cont = mDIS.readLine().split(",");
			WeiDianID2GeneID.put(Integer.valueOf(cont[0]), Integer.valueOf(cont[1]));
		}
		mDIS.close();
		mBIS.close();
		mFIS.close();
	}

	private static void readColID2GeneID(String FilePath) throws Exception {
		String[] cont = new String[] {};
		File mFile = new File(FilePath);
		FileInputStream mFIS = new FileInputStream(mFile);
		BufferedInputStream mBIS = new BufferedInputStream(mFIS);
		DataInputStream mDIS = new DataInputStream(mBIS);
		while (mDIS.available() != 0) {
			cont = mDIS.readLine().split(",");
			ColID2GeneID.put(Integer.valueOf(cont[0]), Integer.valueOf(cont[1]));
		}
		mDIS.close();
		mBIS.close();
		mFIS.close();
	}

	private static void readWeiDianQuanZhong(String FilePath) throws Exception {
		String[] cont = new String[] {};
		File mFile = new File(FilePath);
		FileInputStream mFIS = new FileInputStream(mFile);
		BufferedInputStream mBIS = new BufferedInputStream(mFIS);
		DataInputStream mDIS = new DataInputStream(mBIS);
		while (mDIS.available() != 0) {
			cont = mDIS.readLine().split(",");
			WeiDianQuanZhong.put(Integer.valueOf(cont[0]), Double.valueOf(cont[1]));
		}
		mDIS.close();
		mBIS.close();
		mFIS.close();
	}

	private static ArrayList<Double> DistanceOfGene(String[] YB, String[] DBYB, String[] WeiDianQuanID) {
		ArrayList<Double> GeneQZ = new ArrayList<>();
		double dis1 = 0;
		int GenID = 3;
		int line = 0;

		for (int i = 0; i < YB.length; i++) {
			if (GenID == ColID2GeneID.get(i + 1)) {
				// System.out.println(GenID + ",0," + ColID2GeneID.get(i + 1));
				if (!YB[i].equalsIgnoreCase(DBYB[i])) {
					dis1 += WeiDianQuanZhong.get(Integer.valueOf(WeiDianQuanID[i]));
				}
				line++;
				GeneQZ.add(dis1 / (line));
			} else {
				// System.out.println(GenID + ",1," + ColID2GeneID.get(i + 1));
				GeneQZ.add(dis1 / (line));
				// System.out.println(dis1 + "---------");
				dis1 = 0;
				line = 0;
			}
			if (i == 201) {
				// System.out.println(GenID + ",2," + ColID2GeneID.get(i + 1));
				GeneQZ.add(dis1 / (line));
				// System.out.println(dis1 + "---------");
				dis1 = 0;
				line = 0;
			}
			GenID = ColID2GeneID.get(i + 1);
		}
		// System.out.println(dis);
		return GeneQZ;
	}

	private static double DistanceOfGenes(String[] YB, String[] DBYB, String[] WeiDianQuanID) {
		double dis = 0;
		double dis1 = 0;
		int GenID = 3;
		int line = 0;
		for (int i = 0; i < YB.length; i++) {
			if (GenID == ColID2GeneID.get(i + 1)) {
				// System.out.println(GenID + ",0," + ColID2GeneID.get(i + 1));
				if (!YB[i].equalsIgnoreCase(DBYB[i])) {
					dis1 += WeiDianQuanZhong.get(Integer.valueOf(WeiDianQuanID[i]));
				}
				line++;
			} else {
				// System.out.println(GenID + ",2," + ColID2GeneID.get(i + 1));
				dis += dis1 / (line);
				// System.out.println(dis + "---------");
				dis1 = 0;
				line = 0;
			}
			if (i == 201) {
				// System.out.println(GenID + ",7," + ColID2GeneID.get(i + 1));
				dis += dis1 / (line);
				// System.out.println(dis + "---------");
				dis1 = 0;
				line = 0;
			}
			GenID = ColID2GeneID.get(i + 1);
		}
		// System.out.println(dis);
		return dis;
	}

}
