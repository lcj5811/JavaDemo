package com.lee.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.lee.gis.compresstile.FileOperator;
import com.lee.gis.downloadmap.SQLiteConst;

/**
 * 
 * @ClassName: SQLiteUtil
 * @Description: SQLite数据库的工具类
 * @author 苦丁茶
 * @date 2014年9月15日 上午10:56:45
 */
public class ConnectSQLite {
	private Connection conn;

	private static ConnectSQLite sqlite = new ConnectSQLite();

	private ConnectSQLite() {
		// 连接SQLite的JDBC
		try {
			Class.forName("org.sqlite.JDBC");
			System.out.println("加载SQLite驱动成功");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("加载SQLite驱动失败");
			System.exit(0);
		}
	}

	/**
	 * 创建SQLite数据库
	 * 
	 * @param dbPath
	 *            void
	 */
	public static void initSQLiteDatabase(String dbPath) {
		boolean exist = FileOperator.createFile(dbPath, false);
		sqlite.releaseConnection();

		try {
			String url = "jdbc:sqlite:" + dbPath;
			sqlite.conn = DriverManager.getConnection(url);
			System.out.println("连接SQLite数据库成功");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("连接SQLite数据库失败");
			System.exit(0);
		}
		System.out.println("数据库文件所在的位置" + new File(dbPath).getAbsolutePath() + " exist=" + exist);

		sqlite.createDatabaseTable();
		if (!exist) {
			sqlite.insertTableData();
		}
	}

	/**
	 * 数据库初始化
	 */
	private void createDatabaseTable() {
		Statement st = null;
		try {
			this.conn.setAutoCommit(false); // 更改JDBC事务的默认提交方式
			st = this.conn.createStatement();
			for (int i = 0, len = SQLiteConst.TableName.length; i < len; ++i) {
				System.out.println("tableName:" + SQLiteConst.TableName[i]);
				st.executeUpdate(SQLiteConst.SQLCreateTable[i]);
			}
			this.conn.commit(); // 提交JDBC事务
			this.conn.setAutoCommit(true); // 恢复JDBC事务的默认提交方式
		} catch (SQLException eSQL) {
			try {
				this.conn.rollback(); // 回滚JDBC事务
			} catch (SQLException eRollback) {
				eRollback.printStackTrace();
			}
			eSQL.printStackTrace();
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 为基础表插入数据
	 */
	private void insertTableData() {
		Statement st = null;
		try {
			this.conn.setAutoCommit(false); // 更改JDBC事务的默认提交方式
			st = this.conn.createStatement();
			for (int i = 0, len = SQLiteConst.SQLInsertData.length; i < len; ++i) {
				st.executeUpdate(SQLiteConst.SQLInsertData[i]);
			}
			this.conn.commit(); // 提交JDBC事务
			this.conn.setAutoCommit(true); // 恢复JDBC事务的默认提交方式
		} catch (SQLException eSQL) {
			try {
				this.conn.rollback(); // 回滚JDBC事务
			} catch (SQLException eRollback) {
				eRollback.printStackTrace();
			}
			eSQL.printStackTrace();
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 获得数据库的连接
	 * 
	 * @return
	 */
	public static Connection getConn() {
		return sqlite.conn;
	}

	public static void releaseResource(Statement st, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (st != null) {
				st.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void releaseConnection() {
		try {
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
