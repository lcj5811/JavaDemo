package com.lee.dealoffice;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class NewFolder {

	static DBUtil mDBUtil;

	public static void main(String[] args) {
		try {
			DoTask("G:\\workspace_test\\test\\");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void DoTask(String Path) throws SQLException,
			InterruptedException {
		ArrayList<String> name = com.lee.dealfile.FileName.findFile(new File(
				Path), new ArrayList<String>());
		for (String n : name) {
			System.out.println("\n" + "----------------------------" + n);
			System.out.println(n.replace("确权数据表.mdb", ""));
			ConMDB(n);
			System.out.println("----------------------------" + n);
			Thread.sleep(1000);
		}
	}

	// 连接数据库
	public static void ConMDB(String CunMing) throws InterruptedException {
		String DB_PathName = "G:\\workspace_test\\test\\" + CunMing
				+ "确权数据表.mdb";
		String CunMingPath = "G:\\workspace_test\\01\\";
		String ZunMingPath = "G:\\workspace_test\\01\\";
		mDBUtil = new DBUtil(DB_PathName);
		ArrayList<String> CBFBM = mDBUtil.QueryCBFBM();
		HashMap<String, String> CBFBMandCBFMC = mDBUtil.QueryCBFBMandCBFMC();
		mDBUtil.CloseDB();
		System.out.println(CunMingPath);
		System.out.println(CunMingPath += CBFBM.get(0).substring(0, 12) + "_"
				+ CunMing);
		ZunMingPath = CunMingPath;
		if (!new File(CunMingPath).exists()) {
			new File(CunMingPath).mkdirs();
		}
		for (String n : CBFBM) {
			if (n.substring(12, 14).equals("01")) {
				ZunMingPath += "\\" + n.substring(0, 14) + "_" + "一组";
			} else if (n.substring(12, 14).equals("02")) {
				ZunMingPath += "\\" + n.substring(0, 14) + "_" + "二组";
			} else if (n.substring(12, 14).equals("03")) {
				ZunMingPath += "\\" + n.substring(0, 14) + "_" + "三组";
			} else if (n.substring(12, 14).equals("04")) {
				ZunMingPath += "\\" + n.substring(0, 14) + "_" + "四组";
			} else if (n.substring(12, 14).equals("05")) {
				ZunMingPath += "\\" + n.substring(0, 14) + "_" + "五组";
			} else if (n.substring(12, 14).equals("06")) {
				ZunMingPath += "\\" + n.substring(0, 14) + "_" + "六组";
			} else if (n.substring(12, 14).equals("07")) {
				ZunMingPath += "\\" + n.substring(0, 14) + "_" + "七组";
			} else if (n.substring(12, 14).equals("08")) {
				ZunMingPath += "\\" + n.substring(0, 14) + "_" + "八组";
			} else if (n.substring(12, 14).equals("09")) {
				ZunMingPath += "\\" + n.substring(0, 14) + "_" + "九组";
			} else if (n.substring(12, 14).equals("10")) {
				ZunMingPath += "\\" + n.substring(0, 14) + "_" + "十组";
			} else if (n.substring(12, 14).equals("11")) {
				ZunMingPath += "\\" + n.substring(0, 14) + "_" + "十一组";
			} else if (n.substring(12, 14).equals("12")) {
				ZunMingPath += "\\" + n.substring(0, 14) + "_" + "十二组";
			} else if (n.substring(12, 14).equals("13")) {
				ZunMingPath += "\\" + n.substring(0, 14) + "_" + "十三组";
			} else if (n.substring(12, 14).equals("14")) {
				ZunMingPath += "\\" + n.substring(0, 14) + "_" + "十四组";
			} else if (n.substring(12, 14).equals("15")) {
				ZunMingPath += "\\" + n.substring(0, 14) + "_" + "十五组";
			} else if (n.substring(12, 14).equals("16")) {
				ZunMingPath += "\\" + n.substring(0, 14) + "_" + "十六组";
			} else if (n.substring(12, 14).equals("17")) {
				ZunMingPath += "\\" + n.substring(0, 14) + "_" + "十七组";
			} else {
				System.out.println("发现18组");
				Thread.sleep(10000);
			}
			if (!new File(ZunMingPath).exists()) {
				new File(ZunMingPath).mkdir();
				new File(ZunMingPath + "\\0_发包方调查表").mkdir();
				new File(ZunMingPath += "\\" + n + "_" + CBFBMandCBFMC.get(n))
						.mkdir();
				new File(ZunMingPath + "\\1_摸底表").mkdir();
				new File(ZunMingPath + "\\2_户籍信息").mkdir();
				// new File(ZunMingPath + "\\3_户主声明书").mkdir();
				System.out.println(ZunMingPath);
			} else {
				new File(ZunMingPath += "\\" + n + "_" + CBFBMandCBFMC.get(n))
						.mkdir();
				new File(ZunMingPath + "\\1_摸底表").mkdir();
				new File(ZunMingPath + "\\2_户籍信息").mkdir();
				// new File(ZunMingPath + "\\3_户主声明书").mkdir();
				System.out.println(ZunMingPath);
			}
			ZunMingPath = CunMingPath;
		}
	}
}
