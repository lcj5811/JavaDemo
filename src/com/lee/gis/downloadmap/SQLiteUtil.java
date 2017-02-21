package com.lee.gis.downloadmap;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

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

	private SQLiteUtil() {
		try {
			Class.forName("org.sqlite.JDBC");
			System.out.println("加载SQLite驱动成功");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("加载SQLite驱动失败");
			System.exit(0);
		}
	}

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
	 *            void
	 */
	public void initSQLiteDatabase(String dbPath) {
		boolean exist = FileOperator.createFile(dbPath, false);
		releaseConnection();

		try {
			String url = "jdbc:sqlite:" + dbPath;
			conn = DriverManager.getConnection(url);
			System.out.println("连接SQLite数据库成功");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("连接SQLite数据库失败");
			System.exit(0);
		}
		System.out.println("数据库文件所在的位置" + new File(dbPath).getAbsolutePath() + " exist=" + exist);

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
	 * 插入瓦片数据
	 * 
	 * @throws IOException
	 */
	public boolean insertTableData(String sql, List<Object> list) throws IOException {
		int result = 0;
		PreparedStatement pstmt = null;
		try {
			// 更改JDBC事务的默认提交方式
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql);

			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					// System.out.println("***************" +
					// list.get(i).getClass().getName());
					if ("java.lang.Long".equals(list.get(i).getClass().getName())) {
						// System.out.println("*******0******" +
						// list.get(i).getClass().getName());
						pstmt.setLong(i + 1, Long.valueOf(list.get(i).toString()));
					} else if ("java.lang.String".equals(list.get(i).getClass().getName())) {
						// 如果是字符串
						// System.out.println("*******1******" +
						// list.get(i).getClass().getName());
						pstmt.setString(i + 1, list.get(i).toString());
					} else if ("[B".equals(list.get(i).getClass().getName())) {
						// 如果是图片
						// System.out.println("******2*******" +
						// list.get(i).getClass());
						ByteArrayInputStream b = new ByteArrayInputStream((byte[]) list.get(i));
						pstmt.setBinaryStream(i + 1, b, b.available());
						b.close();
					}
				}
			}
			result = pstmt.executeUpdate();
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
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		if (result > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获得数据库的连接
	 * 
	 * @return
	 */
	public static Connection getConn() {
		return conn;
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
