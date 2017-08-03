package com.lee.mathmodel.netAna;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

/**
 * @ClassName com.lee.mathmodel.MinDistance2
 * @description
 * @author 凌霄
 * @data 2016年11月7日 下午4:14:07
 */
public class MinDistance {

	static String mInputFileUrl = "D://MinDistance//Dis.accdb";
	static String mOutputFileDir = "D://MinDistance//Output";

	// static String mOutputFileDir;
	// static String mInputFileUrl;

	static private ArrayList<Integer[]> Output = new ArrayList<Integer[]>();

	static private String newCSVFileName = "";

	// 最大距离
	static private String maxDis = "";
	// 距离
	static private String mDis = "";
	// 斑块
	static private ArrayList<String> mX = new ArrayList<String>();
	// 距离矩阵
	static int eachLine[][];

	public static void main(String[] args) {

		// mInputFileUrl = args[0];
		// mOutputFileDir = args[1];

		try {
			ConnectAccessFile(mInputFileUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static final Properties prop = new Properties();
	static {
		prop.put("charSet", "GBK"); // 这里是解决中文乱码
		prop.put("user", "root");
		prop.put("password", "root");
	}

	private static void ConnectAccessFile(String mDataBasePath) throws Exception {
		// 连接数据库
		String dbur1 = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=" + mDataBasePath;
		Connection conn = DriverManager.getConnection(dbur1, prop);
		Statement stmt = conn.createStatement();
		ResultSet rs;

		// 从小到大排列元素，并获取
		rs = stmt.executeQuery("select distinct X from Dis order by X");
		while (rs.next()) {
			mX.add(rs.getString(1));
		}

		// 求所有距离的和作为最大距离
		// rs = stmt.executeQuery("select Sum(D) from Dis");
		// while (rs.next()) {
		// maxDis = rs.getString(1);
		// }

		eachLine = new int[mX.size()][mX.size()];

		int i = 0, j = 0;
		for (String eachX : mX) {

			for (String eachY : mX) {

				if (eachX.equalsIgnoreCase(eachY)) {
					mDis = "0";
				} else {
					// mDis = maxDis;
					mDis = "0";
				}

				rs = stmt.executeQuery(
						"select D from Dis where X=" + Integer.valueOf(eachX) + " and Y = " + Integer.valueOf(eachY));
				if (rs.next()) {
					eachLine[i][j] = Integer.valueOf(rs.getString(1));
				} else {
					rs = stmt.executeQuery("select D from Dis where X=" + Integer.valueOf(eachY) + " and Y = "
							+ Integer.valueOf(eachX));
					if (rs.next()) {
						eachLine[i][j] = Integer.valueOf(rs.getString(1));
					} else {
						eachLine[i][j] = Integer.valueOf(mDis);
					}
				}
				j++;
			}
			j = 0;
			i++;
		}

		rs.close();
		stmt.close();
		conn.close();

		String cont = "";

		for (int[] js : eachLine) {
			for (int k : js) {
				cont += k + ",";
			}
			cont += "\n";
		}

		newCSVFileName = getSystemTime("_模糊关系阵") + ".csv";

		createFile(mOutputFileDir, newCSVFileName);

		writeTxt(mOutputFileDir + "//" + newCSVFileName, cont);

		// Window.inTable("模糊关系矩阵输出成功：" + mOutputFileDir + "//" +
		// newCSVFileName);

		calcAllPath();

	}

	/**
	 * 向CSV文件中写入内容
	 * 
	 * @param mCSVPath
	 *            路径+文件
	 * @param mContent
	 *            内容
	 * @throws IOException
	 */
	public static void writeTxt(String mCSVPath, String mContent) throws IOException {
		FileWriter mFileWriter = new FileWriter(mCSVPath, true);
		FileReader mFileReader = new FileReader(mCSVPath);
		BufferedReader mBufferedReader = new BufferedReader(mFileReader);
		BufferedWriter mBufferedWriter = new BufferedWriter(mFileWriter);
		mBufferedWriter.write(mContent);
		mBufferedWriter.flush();
		mFileWriter.flush();
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

		File f = new File(mPath);
		if (!f.exists()) {
			f.mkdirs();
		}

		File file = new File(f, mName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获得文件名称
	 * 
	 * @param mLastName
	 *            后缀名
	 * @return 时间+后缀名
	 */
	public static String getSystemTime(String mLastName) {
		// 设置日期格式
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
		// new Date()为获取当前系统时间
		return df.format(new Date()) + mLastName;
	}

	private static void calcAllPath() throws IOException {
		AllPath instance = new AllPath(eachLine, eachLine.length);
		instance.start();
	}

}
