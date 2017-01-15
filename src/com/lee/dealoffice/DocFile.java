package com.lee.dealoffice;

import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

public class DocFile {

	static int HS = 0;
	static String Fbfbm = "";
	static DBUtil db_1;
	static DBUtil db_2;
	static DBUtil db_3;
	static DBUtil db_4;
	static DBUtil db_5;
	static DBUtil db_6;
	// static int id;
	static ArrayList<String> prob;

	public static void main(String[] args) throws Exception {
		// DoTask("花桥镇");
		// findFile("花桥镇", "花山村", null);
		// DoTask("新圩江镇", "茶山村", "2组");
		// updateSFZHM("新圩江镇", "大浪村");
		// update_FBFSFZHM_CBDKXX_CBHT("新圩江镇", "白米寨村");
		// insertDK("新圩江镇", "大埠头村");
		Task("新圩江镇", "竹冲村");
		System.out.println("完成" /* + id */);
		System.exit(0);
	}

	public static void Task(String z, String c) {
		update_FBFSFZHM_CBDKXX_CBHT(z, c);
		insertDK(z, c);
	}

	// 循环执行任务
	public static void DoTask(String Z) throws SQLException,
			InterruptedException {
		ArrayList<String> name = com.lee.dealfile.FileName.findFile(new File(
				"G:\\东安入库\\02\\test\\" + Z), new ArrayList<String>());
		for (String n : name) {
			System.out.println("\n" + Z + "----------------------------" + n);
			update_FBFSFZHM_CBDKXX_CBHT(Z, n);
			System.out.println(Z + "----------------------------" + n);
			// updateSFZHM(Z, n);
			Thread.sleep(3000);
		}
	}

	// TableA10_DK表中插入信息
	public static void insertDK(String Z, String C) {
		ArrayList<String> value = new ArrayList<String>();
		String DB_P1 = "G:\\workspace_test\\test\\" + C + "确权数据表.mdb";
		String DB_P2 = "G:\\workspace_test\\xxj.mdb";
		db_1 = new DBUtil(DB_P1);
		db_2 = new DBUtil(DB_P2);
		prob = db_1.QueryDKBM(Z, C);
		System.out.println(prob.size());
		int i = 0;
		for (String n : prob) {
			value = new ArrayList<String>();
			value.add(String.valueOf(i++));
			value.add(n);
			for (String a : db_2.QueryDKMC(Z, C,
					n.substring(n.length() - 5, n.length()))) {
				value.add(a);
			}
			db_1.InsertDK(value);
		}
		// db_1.InsertDK(value);
		db_1.CloseDB();
		db_2.CloseDB();
	}

	// 更新发包方身份证号、插入承包地块信息表、插入承包合同表
	public static void update_FBFSFZHM_CBDKXX_CBHT(String Z, String C) {
		String QQSJ_DB_Path = "G:\\workspace_test\\test\\" + C + "确权数据表.mdb";
		String CZSJ_DB_Path = "G:\\workspace_test\\xxj.mdb";
		prob = new ArrayList<String>();
		prob.add("FBFBM,ZDBH,CBR");
		// updateFBFSFZHM(Z, C, QQSJ_DB_Path);
		insertCBDKXX(Z, C, QQSJ_DB_Path, CZSJ_DB_Path);
		insertCBHT(Z, C, QQSJ_DB_Path);
		com.lee.dealfile.FileName.saveText("G:\\workspace_test\\问题\\" + Z + "\\" + C
				+ ".CSV", prob, null);
	}

	public static void updateFBFSFZHM(String Z, String C, String QQSJ_DB_Path) {
		db_5 = new DBUtil(QQSJ_DB_Path);
		for (String n : db_5.QueryFZRXM()) {
			System.out.println(db_5.QueryFBFFZRZJH(n) + ","
					+ db_5.QueryFBFDZ(n) + "," + n);
			db_5.updateZJDZ(db_5.QueryFBFFZRZJH(n), db_5.QueryFBFDZ(n), n);
		}
		db_5.CloseDB();
	}

	public static void insertCBDKXX(String Z, String C, String QQSJ_DB_Path,
			String CZSJ_DB_Path) {
		db_6 = new DBUtil(CZSJ_DB_Path);
		ArrayList<String> list = db_6.QueryZDBH(Z, C);
		db_6.CloseDB();
		for (String n : list) {
			db_6 = new DBUtil(CZSJ_DB_Path);
			db_5 = new DBUtil(QQSJ_DB_Path);
			if (n != null) {
				// id++;
				String zdbm = n;
				String htmj_m = db_6.QueryHTMJ_M(Z, C, n);
				String htmj = db_6.QueryHTMJ(Z, C, n);
				String CZ = db_6.QueryCZ(Z, C, n);
				String fbfbm = db_5.QueryFBFBM1(CZ);
				String CBR = db_6.QueryCBR(Z, C, n);
				String cbfbm = "";
				if (CBR.indexOf("集体") != -1) {
					CBR = "集体田";
					cbfbm = db_5.QueryCBFBM_CZ(CBR, CZ);
				} else if (CBR.indexOf("机动") != -1) {
					CBR = "机动田";
					cbfbm = db_5.QueryCBFBM_CZ(CBR, CZ);
				} else {
					cbfbm = db_5.QueryCBFBM_CBR(CBR);
				}
				String cbhtbm = cbfbm;
				if (CBR.indexOf("集体") != -1 && !cbhtbm.equalsIgnoreCase("")) {
					cbhtbm += "Q";
				} else if (!cbhtbm.equalsIgnoreCase("")) {
					cbhtbm += "J";
				} else if (CBR.indexOf("机动") != -1
						&& !cbhtbm.equalsIgnoreCase("")) {
					cbhtbm += "Q";
				}
				if (cbfbm.equalsIgnoreCase("")) {
					prob.add("'" + fbfbm + "," + zdbm + "," + CBR);
					cbfbm = "000000000000000000";
				}
				System.out.println(CZ + "," + fbfbm + zdbm + "," + cbfbm + ","
						+ htmj_m + "," + htmj + "," + fbfbm + "," + cbhtbm
						+ "," + CBR);
				db_5.InsertCBDKXX(new String[] { fbfbm + zdbm, cbfbm, htmj_m,
						htmj, fbfbm, cbhtbm }, Z, C);
			}
			db_5.CloseDB();
			db_6.CloseDB();
		}
	}

	public static void insertCBHT(String Z, String C, String QQSJ_DB_Path) {
		String QS = "1994/01/01";
		String JZ = "2024/01/01";
		db_6 = new DBUtil(QQSJ_DB_Path);
		ArrayList<String> list = db_6.QueryCBHTBM();
		db_6.CloseDB();
		for (String n : list) {
			db_6 = new DBUtil(QQSJ_DB_Path);
			if (!n.equalsIgnoreCase("")) {
				String fbfbm = db_6.QueryFBFBM2(n);
				String cbfbm = db_6.QueryCBFBM2(n);
				System.out.println(n + "," + fbfbm + "," + cbfbm);
				db_6.InsertCBHT(new String[] { n, fbfbm, cbfbm }, QS, JZ);
			}
			db_6.CloseDB();
		}
	}

	public static void updateSFZHM(String Z, String C) throws SQLException {
		String QQSJ_DB_Path = "G:\\workspace_test\\test\\" + C + "确权数据表.mdb";
		String CZSJ_DB_Path = "G:\\workspace_test\\xxj.mdb";
		db_5 = new DBUtil(QQSJ_DB_Path);
		ArrayList<String> list = db_5.QueryXM();
		db_5.CloseDB();
		for (String n : list) {
			db_5 = new DBUtil(QQSJ_DB_Path);
			db_6 = new DBUtil(CZSJ_DB_Path);
			String SFZH = db_6.QuerySFZH(n, Z, C);
			System.out.println(n + "," + SFZH);
			db_5.updateSFZH(n, SFZH);
			db_5.CloseDB();
			db_6.CloseDB();
		}
	}

	public static void findFile(String Z, String C, String Zu) throws Exception {
		String URL = "G:\\东安入库\\东安家庭成员信息表\\" + Z + "\\" + C;
		File file = new File(URL);
		if (Zu != null) {
			URL += "\\" + Zu;
		}
		if (file.isDirectory()) { // 否则如果它是一个目录
			File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
			for (File f : files) { // 遍历目录下所有的文件
				DoTask(Z, C, f.getName());// 把每个文件 用这个方法进行迭代
			}
		}
	}

	public static void OpenDB(String Z, String C) {
		db_1 = new DBUtil("G:\\东安入库\\毛家村确权数据表.mdb");

		String DB_P = "G:\\东安入库\\东安县数据\\xxjz.mdb";
		if ("花桥镇".equals(Z)) {
			DB_P = "G:\\东安入库\\东安县数据\\hqz.mdb";
		}
		db_2 = new DBUtil(DB_P);

		String DB_PathName = "G:\\东安入库\\output\\" + C + "确权数据表.mdb";
		db_3 = new DBUtil(DB_PathName);

		db_4 = new DBUtil("G:\\东安入库\\xzqdm.mdb");
	}

	public static void CloseDB() {
		db_1.CloseDB();
		db_2.CloseDB();
		db_3.CloseDB();
		db_4.CloseDB();
	}

	public static void DoTask(String Z, String C, String Zu) throws Exception {
		OpenDB(Z, C);
		Fbfbm = QueryFBFBM(C, Zu);
		Insert_FBF(Z, C, Zu);
		CloseDB();
		findFile(Z, C, Zu, null);
	}

	public static void CountJTCY(String CBFBM) throws SQLException,
			InterruptedException {
		System.out.println(CBFBM);
		db_3.updateCYSL(CBFBM);
	}

	public static void Insert_FBF(String Z, String C, String Zu)
			throws SQLException, InterruptedException {
		System.out.println(Fbfbm);
		String value[] = new String[2];
		value[0] = Fbfbm;
		value[1] = Z + C + Zu;
		db_3.InsertFBF(value);
		// 发包方编码, 发包方名称
	}

	public static void Insert_CBF(String Z, String C, String Zu, String[] V,
			int HS) throws SQLException, InterruptedException {
		String value[] = new String[9];
		if (V != null) {
			value[0] = CBFBM(C, Zu, HS);
			value[1] = "1";
			value[2] = V[0];
			value[3] = "1";
			value[4] = V[2];
			value[5] = Z + C + Zu;
			value[6] = CBMJ_DK(Z, C, V[0])[1];
			value[7] = CBMJ_DK(Z, C, V[0])[0];
			value[8] = "";
		} else {
			value[0] = CBFBM(C, Zu, HS);
			value[1] = "";
			value[2] = "集体田";
			value[3] = "";
			value[4] = "";
			value[5] = "";
			value[6] = CBMJ_DK(Z, C, "集体")[1];
			value[7] = CBMJ_DK(Z, C, "集体")[0];
			value[8] = "";
			System.out.println(value[0]);
		}
		db_3.InsertCBF(value);
		// 承包方编码,承包方类型,承包方（代表）名称,承包方(代表)证件类型,承包方(代表)证件号码,承包方地址,原合同面积,原合同地块数,承包方成员数量
	}

	//
	public static void Insert_CBF_JTCY(String C, String Zu, String[] V)
			throws SQLException, InterruptedException {
		String value[] = new String[8];
		value[0] = CBFBM(C, Zu, HS);
		value[1] = V[0];
		value[2] = CYXB(V[1]);
		if (V[3] != "") {
			value[3] = "1";
			value[4] = V[2];
		} else {
			value[3] = "";
			value[4] = "";
		}
		value[5] = HZGX(V[1]);
		value[6] = V[3];
		value[7] = "1";
		db_3.InsertJTCY(value);
		// 承包方编码,成员姓名,成员性别,成员证件类型,成员证件号码,与户主关系,成员备注,是否共有人

	}

	public static void findFile(String Z, String C, String Zu, String FileName)
			throws Exception {
		String URL = "G:\\东安入库\\东安家庭成员信息表\\" + Z + "\\" + C + "\\" + Zu;
		if (FileName != null) {
			URL += "\\" + FileName;
		}
		File file = new File(URL);
		if (file.isDirectory()) { // 否则如果它是一个目录
			File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
			for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
				findFile(Z, C, Zu, files[i].getName()); // 把每个文件
			}
			HS = 0;
		} else if (file.exists()) {
			OpenDB(Z, C);
			// System.out.println(file.getPath());
			if (file.getName().indexOf("集体") != -1) {
				System.out.println("发现集体田");
				Insert_CBF(Z, C, Zu, null, -1);
				System.out.println("=================================");
			} else {
				System.out.println(file.getName());
				getNewContent(file, Z, C, Zu, ++HS);
			}
			CloseDB();
		}
	}

	public static String CBFBM(String C, String Zu, int HS) throws SQLException {
		String CBFBM = "";
		if (HS > 9) {
			CBFBM += "00" + HS;
		} else if (HS <= 9 && HS >= 0) {
			CBFBM += "000" + HS;
		} else if (HS < 0) {
			CBFBM += "8001";
		}
		return Fbfbm + CBFBM;
	}

	public static String[] CBMJ_DK(String Z, String C, String Name)
			throws SQLException, InterruptedException {
		String[] MJDK = db_2.Query_MJ_DK(Z, C, Name);
		return MJDK;
	}

	public static String QueryFBFBM(String C, String Zu) throws SQLException,
			InterruptedException {
		String XZQDM = "";
		if (Fbfbm.equals("")) {
			XZQDM = db_4.QueryFBFBM(C);
		} else {
			XZQDM = Fbfbm.substring(0, Fbfbm.length() - 2);
		}
		if (Integer.valueOf(Zu.substring(0, Zu.length() - 1)) > 9) {
			XZQDM += Integer.valueOf(Zu.substring(0, Zu.length() - 1));
		} else {
			XZQDM += "0" + Integer.valueOf(Zu.substring(0, Zu.length() - 1));
		}
		return XZQDM;
	}

	public static String CYXB(String GX) throws SQLException,
			InterruptedException {
		String XBDM = "";
		// DBUtil dbu1 = new DBUtil("G:\\东安入库\\毛家村确权数据表.mdb");
		XBDM = db_1.QueryXBDM(GX);
		return XBDM;
	}

	public static String HZGX(String GX) throws SQLException,
			InterruptedException {
		// DBUtil dbu1 = new DBUtil("G:\\东安入库\\毛家村确权数据表.mdb");
		String GXDM = db_1.QueryGXDM(GX);
		return GXDM;
	}

	public static void getNewContent(File file, String Z, String C, String Zu,
			int HS) throws Exception {
		String a = getContent(file);
		int size1 = a.indexOf("成员备注");
		int size2 = a.indexOf("调查记事");
		a = a.substring(size1, size2).replace("", " ");
		a = a.substring(4, a.length());
		String b = "";
		a = a.trim();
		for (char c : a.toCharArray()) {
			if (c == '\r') {
				continue;
			} else if (" ".equals(String.valueOf(c))) {
				b += ",";
			} else {
				b += c;
			}
		}
		System.out.println(b);
		b = b.replace("7", "");
		b = b.replace("、", "");
		b = b.replace("·", "");
		b = b.replace("户主,,,", "户主,");
		b = b.replace("户主,,", "户主,");
		b = b.replace(",,,户主", ",户主");
		b = b.replace(",,户主", ",户主");
		b = b.replace(",,孙女", ",孙女");
		b = b.replace("孙女,,", "孙女,");
		b = b.replace(",,孙子", ",孙子");
		b = b.replace("孙子,,", "孙子,");
		b = b.replace(",,儿子", ",儿子");
		b = b.replace("儿子,,", "儿子,");
		b = b.replace(",,长女", ",长女");
		b = b.replace("长女,,", "长女,");
		b = b.replace(",,次子", ",次子");
		b = b.replace("次子,,", "次子,");
		b = b.replace(",,长子", ",长子");
		b = b.replace("长子,,", "长子,");
		b = b.replace(",,女儿", ",女儿");
		b = b.replace("女儿,,", "女儿,");
		b = b.replace(",,二女", ",二女");
		b = b.replace("二女,,", "二女,");
		b = b.replace(",,三女", ",三女");
		b = b.replace("三女,,", "三女,");
		b = b.replace(",,侄子", ",侄子");
		b = b.replace("侄子,,", "侄子,");
		b = b.replace(",,妻子", ",妻子");
		b = b.replace("妻子,,", "妻子,");
		b = b.replace(",,儿媳", ",儿媳");
		b = b.replace("儿媳,,", "儿媳,");
		b = b.replace(",,父", ",父");
		b = b.replace("父,,", "父,");
		b = b.replace(",,母", ",母");
		b = b.replace("母,,", "母,");
		b = b.replace(",,,,,,,,,,,,,,,,,,,,,,,", ",");
		b = b.replace(",,,,,,,,,,,,,,,,,,,,,,", ",");
		b = b.replace(",,,,,,,,,,,,,,,,,,,,,", ",");
		b = b.replace(",,,,,,,,,,,,,,,,,,,,", ",");
		b = b.replace(",,,,,,,,,,,,,,,,,,,", ",");
		b = b.replace(",,,,,,,,,,,,,,,,,,", ",");
		b = b.replace(",,,,,,,,,,,,,,,,,", ",");
		b = b.replace(",,,,,,,,,,,,,,,,", ",");
		b = b.replace(",,,,,,,,,,,,,,,", ",");
		b = b.replace(",,,,,,,,,,,,,,", ",");
		b = b.replace(",,,,,,,,,,,,,", ",");
		b = b.replace(",,,,,,,,,,,,", ",");
		b = b.replace(",,,,,,,,,,,", ",");
		b = b.replace(",,,,,,,,,,", ",");
		b = b.replace(",,,,,,,,,", ",");
		b = b.replace(",,,,,,,,", ",");
		b = b.replace(",,,,,,,", ",");
		b = b.replace(",,,,,,", ",");
		b = b.replace(",,,,,", ",");
		b = b.replace(",,,,", ",");
		b = b.replace(",,,", ",");
		b = b.replace(",,", "");
		a = "";
		char f = 0;
		for (char c : b.toCharArray()) {
			if (f != c) {
				a += c;
				f = c;
			} else if (!",".equals(String.valueOf(c))) {
				a += c;
			}
		}
		b = "";
		int count = 0;
		boolean flag = true;
		for (char c : a.substring(0, a.length()).toCharArray()) {
			if (",".equals(String.valueOf(c))) {
				count++;
			}
			if (flag && count == 3) {
				b += "|";
				count = 0;
				flag = false;
			} else if (!flag && count == 2) {
				b += "|";
				count = 0;
			} else {
				b += c;
			}
		}
		if (b.length() < 10) {
			b += ",";
		}
		b += "|";
		int count2 = 0, count3 = 0;
		String[] ab = new String[4];
		b = b.replace(" ", "");
		// System.out.println(b);
		a = "";
		for (char c : b.toCharArray()) {
			if (",".equals(String.valueOf(c))) {
				ab[count2] = a;
				a = "";
				count2++;
			} else if ("|".equals(String.valueOf(c))) {
				ab[count2] = a;
				if (count3 == 0) {
					Insert_CBF(Z, C, Zu, ab, HS);
					Insert_CBF_JTCY(C, Zu, ab);
				} else {
					Insert_CBF_JTCY(C, Zu, ab);
				}
				ab = new String[4];
				a = "";
				count2 = 0;
				count3++;
			} else {
				a += c;
			}
		}
		CountJTCY(CBFBM(C, Zu, HS));
		System.out.println("=================================");
	}

	public static String getContent(File f) throws Exception {
		FileInputStream fis = new FileInputStream(f);
		HWPFDocument doc = new HWPFDocument(fis);
		Range rang = doc.getRange();
		String text = rang.text();
		fis.close();
		return text;
	}
}
