package com.lee.gis.downloadmap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lee.gis.compresstile.FileOperator;
import com.lee.gis.downloadmap.SQLiteConst;

/**
 * 
 * @ClassName: SQLiteUtil
 * @Description: SQLite数据库的工具类
 * @author 苦丁茶
 * @date 2014年9月15日 上午10:56:45
 */
public class SQLiteUtil {
	private static Connection conn;

	/**
	 * 构造方法
	 */
	private SQLiteUtil() {
		try {
			Class.forName("org.sqlite.JDBC");
			// System.out.println("加载SQLite驱动成功");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("加载SQLite驱动失败");
			System.exit(0);
		}
	}

	/**
	 * 单例接口
	 * 
	 * @return
	 */
	public static SQLiteUtil getInstance() {
		return SingletonHolder.sInstance;
	}

	private static class SingletonHolder {
		private static final SQLiteUtil sInstance = new SQLiteUtil();
	}

	/**
	 * 创建SQLite数据库
	 * 
	 * @param dbPath
	 *            数据库路径 void
	 */
	public void initSQLiteDatabase(String dbPath) {
		boolean exist = FileOperator.createFile(dbPath, false);
		releaseConnection();

		try {
			String url = "jdbc:sqlite:" + dbPath;
			conn = DriverManager.getConnection(url);
//			System.out.println("连接SQLite数据库成功");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("连接SQLite数据库失败");
			System.exit(0);
		}
		System.out.println("数据库文件所在的位置：" + new File(dbPath).getAbsolutePath() + " exist=" + exist);
		createDatabaseTable();
	}

	/**
	 * 数据库初始化
	 */
	private void createDatabaseTable() {
		Statement st = null;
		try {
			conn.setAutoCommit(false); // 更改JDBC事务的默认提交方式
			st = conn.createStatement();
			for (int i = 0, len = SQLiteConst.TableName.length; i < len; ++i) {
				System.out.println("tableName:" + SQLiteConst.TableName[i]);
				st.executeUpdate(SQLiteConst.SQLCreateTable[i]);
			}
			conn.commit(); // 提交JDBC事务
			conn.setAutoCommit(true); // 恢复JDBC事务的默认提交方式
		} catch (SQLException eSQL) {
			try {
				conn.rollback(); // 回滚JDBC事务
			} catch (SQLException eRollback) {
				eRollback.printStackTrace();
			}
			eSQL.printStackTrace();
		} finally {
			releaseResource(null, st, null);
		}
	}

	/**
	 * 插入瓦片数据
	 * 
	 * @throws IOException
	 */
	public boolean insertTableData(String sql, long key, String provider, byte[] mByte) throws IOException {
		int result = 0;
		PreparedStatement pstmt = null;
		try {
			// 更改JDBC事务的默认提交方式
			// conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, key);
			pstmt.setString(2, provider);
			ByteArrayInputStream b = new ByteArrayInputStream(mByte);
			pstmt.setBinaryStream(3, b, b.available());
			b.close();
			result = pstmt.executeUpdate();
			// conn.commit(); // 提交JDBC事务
			// conn.setAutoCommit(true); // 恢复JDBC事务的默认提交方式
		} catch (SQLException eSQL) {
			try {
				conn.rollback(); // 回滚JDBC事务
			} catch (SQLException eRollback) {
				eRollback.printStackTrace();
			}
			eSQL.printStackTrace();
		} finally {
			releaseResource(pstmt, null, null);
		}
		if (result > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 瓦片数据是否已经存在
	 * 
	 * @param sql
	 *            SQL语句
	 * @param key
	 *            瓦片Key值
	 * @param provider
	 *            瓦片提供源名称
	 * @return 是否存储成功，true-成功,false-失败
	 * @throws SQLException
	 */
	public boolean isDataExsit(String sql, Long key, String provider) throws SQLException {
		boolean isExist = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, key);
			pstmt.setString(2, provider);
			rs = pstmt.executeQuery();
			int mProvider = rs.getInt(1);
			if (mProvider > 0) {
				isExist = true;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.toString());
		} finally {
			releaseResource(pstmt, null, rs);
		}
		return isExist;
	}

	/**
	 * 获得数据库的连接
	 * 
	 * @return
	 */
	public Connection getConn() {
		return conn;
	}

	/**
	 * 释放资源
	 * 
	 * @param st
	 *            SQL语句执行对象
	 * @param rs
	 *            结果集
	 */
	public void releaseResource(PreparedStatement pstmt, Statement st, ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			if (pstmt != null)
				pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 释放连接
	 */
	public void releaseConnection() {
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
