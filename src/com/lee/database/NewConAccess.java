package com.lee.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class NewConAccess {
	Connection con = null;
	ResultSet rs = null;
	Statement stmt = null;

	public NewConAccess(String DB_PathName) {
		try {
			Class.forName("com.hxtt.sql.access.AccessDriver").newInstance();
			// String url = "jdbc:Access:///e://java//Manager.mdb";
			String url = "jdbc:Access:///" + DB_PathName;
			con = DriverManager.getConnection(url, "", "");
		} catch (Exception e1) {
			e1.printStackTrace();
			System.out.println("加载数据库驱动程序错误！");
		}

		// try {
		// // String dbur1 =
		// //
		// "jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ=e://java//JFManager.mdb";
		// // Connection conn = DriverManager.getConnection(dbur1);
		// stmt = con.createStatement();
		// rs = stmt.executeQuery("select * from 新圩江镇_大埠头村");
		// while (rs.next()) {
		// System.out.println(rs.getInt(1));
		// // System.out.println(rs.getString(1));
		// }
		// rs.close();
		// stmt.close();
		// con.close();
		// } catch (SQLException e) {
		// System.out.println("打开数据库错误！");
		// }
		//
		// System.out.println("类测试成功!");

	}

	public void CloseDB() {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
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
