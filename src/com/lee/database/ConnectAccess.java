package com.lee.database;

import java.sql.*;

/**
 * @ClassName com.lee.database.ConnectAccess
 * @description
 * @author 凌霄
 * @data 2016年11月7日 下午4:45:24
 */
public class ConnectAccess {
	/**
	 * 初学者请注意： 1:先建立一个access文件a1.mdb,并放在D:/下; 2:在数据库文件a1.mdb中建立一个表Table1；
	 * 3：为Table1添加一列，并插入至少一条记录； 4：本文是一个完整的类，直接拿去运行就可以。
	 */
	public static void main(String args[]) throws Exception {
		ConnectAccess ca = new ConnectAccess();
		ca.ConnectAccessFile();
		ca.ConnectAccessDataSource();
	}

	public void ConnectAccessFile() throws Exception {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		/**
		 * 直接连接access文件。
		 */
		String dbur1 = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ=d://a1.mdb";
		Connection conn = DriverManager.getConnection(dbur1, "username", "password");
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from Table1");
		while (rs.next()) {
			System.out.println(rs.getString(1));
		}
		rs.close();
		stmt.close();
		conn.close();
	}

	public void ConnectAccessDataSource() throws Exception {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		/**
		 * 采用ODBC连接方式 如何建立ODBC连接？
		 * 答：在windows下，【开始】->【控制面板】->【性能和维护】->【管理工具】->【数据源】，在数据源这里添加一个指向a1.
		 * mdb文件的数据源。 比如创建名字为dataS1
		 */
		String dbur1 = "jdbc:odbc:dataS1";// 此为ODBC连接方式
		Connection conn = DriverManager.getConnection(dbur1, "username", "password");
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from Table1");
		while (rs.next()) {
			System.out.println(rs.getString(1));
		}
		rs.close();
		stmt.close();
		conn.close();
	}
}
