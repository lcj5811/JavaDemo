package com.lee.dealoffice;

import java.io.ObjectInputStream.GetField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Properties;

public class OldConAccess {
	Connection con = null;
	ResultSet rs = null;

	
	// public static Connection getMdbConnection(String path){
	// Properties prop = new Properties();
	// prop.put("charSet", "gb2312"); //这里是解决中文乱码
	// prop.put("user", "root");
	// prop.put("password", "root");
	// //String
	// url="jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ="+path;
	// //文件地址
	// String
	// url="jdbc:odbc:driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ= "+path;
	// //文件地址
	// try {
	// Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
	// Connection conn=DriverManager.getConnection(url,prop);
	// System.out.println("s");
	// return conn;
	// }catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// }

	public OldConAccess(String DB_PathName) {
		try {
			String url = "jdbc:odbc:Driver={MicroSoft Access Driver (*.mdb, *.accdb)};DBQ="
					+ DB_PathName/* "G:\\东安入库\\xzqdm.mdb" */;
			con = DriverManager.getConnection(url, "", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void CloseDB() {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
