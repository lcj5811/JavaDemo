package com.lee.mathmodel.geneAnalyze;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.sound.sampled.Line;

/**
 * @ClassName com.lee.dealfile.Dat_Deal
 * @description 读取基因位点信息
 * @author 凌霄
 * @data 2016年9月18日 下午11:06:48
 */
public class GeneMsg {

	static FileWriter mFW = null;
	
	static BufferedWriter mBW = null;

	static ArrayList<String[]> WeiDian = null;

	static ArrayList<String> WeiDianID = new ArrayList<>();

	public static void main(String[] args) throws Exception {
		// 提取基因位点信息
		// readDir("D:\\数学建模\\B_基因\\处理数据\\WorkSpace\\Q3\\gene_info");
		// 提取关联位点信息
		relSrcData("D:\\数学建模\\B_基因\\处理数据\\WorkSpace\\Q4\\1897T.csv");
	}

	private static int readWeiDianIDMsg(String FilePath, String WeiDianName) throws Exception {
		int lineNum = 0;
		File mFile = new File(FilePath);
		FileInputStream mFIS = new FileInputStream(mFile);
		BufferedInputStream mBIS = new BufferedInputStream(mFIS);
		DataInputStream mDIS = new DataInputStream(mBIS);
		while (mDIS.available() != 0) {
			lineNum++;
			if (mDIS.readLine().contains(WeiDianName)) {
				break;
			}
		}
		mDIS.close();
		mBIS.close();
		mFIS.close();
		return lineNum;
	}

	private static void readDir(String path) throws Exception {

		File files[] = new File(path).listFiles(); // 声明目录下所有的文件 files[];
		for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
			// readDat(files[i].getAbsolutePath(),
			// files[i].getParent() + "\\" + files[i].getName().replace(".dat",
			// ".csv"));
			DelData(files[i].getAbsolutePath(),
					files[i].getParent() + "\\" + files[i].getName().replace("gene", "G") + ".csv");
		}

	}

	private static void DelData(String FilePath, String NewFileName) throws Exception {
		String con = "";
		String line = "";
		String[] ID = new String[] {};
		File mFile = new File(FilePath);
		FileInputStream mFIS = new FileInputStream(mFile);
		BufferedInputStream mBIS = new BufferedInputStream(mFIS);
		DataInputStream mDIS = new DataInputStream(mBIS);

		while (mDIS.available() != 0) {
			line = mDIS.readLine();
			for (String a : ID) {
				if (a.equalsIgnoreCase(line.split(",")[0])) {
					con += line + "\n";
				}
			}

		}
		System.out.println(con);
		writeWeiDianMsg(NewFileName, con);

		mDIS.close();
		mBIS.close();
		mFIS.close();
	}

	private static void readSrcMsg(String FilePath) throws Exception {

		File mFile = new File(FilePath);
		FileInputStream mFIS = new FileInputStream(mFile);
		BufferedInputStream mBIS = new BufferedInputStream(mFIS);
		DataInputStream mDIS = new DataInputStream(mBIS);

		while (mDIS.available() != 0) {
			WeiDianID.add(mDIS.readLine().split(",")[0]);
		}

		mDIS.close();
		mBIS.close();
		mFIS.close();
	}

	private static void writeWeiDianMsg(String NewFileName, String con) throws Exception {
		File file = new File(NewFileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		mFW = new FileWriter(file);
		mBW = new BufferedWriter(mFW);
		mBW.write(con);
		mBW.close();
		mFW.close();
	}

	private static void relSrcData(String FileName) throws Exception {

		readSrcMsg("D:\\数学建模\\B_基因\\处理数据\\WorkSpace\\Q4\\115.csv");

		String line = "";
		String con = "";

		File mFile = new File(FileName);
		FileInputStream mFIS = new FileInputStream(mFile);
		BufferedInputStream mBIS = new BufferedInputStream(mFIS);
		DataInputStream mDIS = new DataInputStream(mBIS);

		while (mDIS.available() != 0) {
			line = mDIS.readLine();
			for (String a : WeiDianID) {
				if (a.equalsIgnoreCase(line.split(",")[0])) {
					con += line + "\n";
				}
			}
		}
		// System.out.println(con);
		writeWeiDianMsg("D:\\数学建模\\B_基因\\处理数据\\WorkSpace\\Q4\\115-output.cs", con);

		mDIS.close();
		mBIS.close();
		mFIS.close();

	}

}
