package com.lee.gis.downloadmap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SQLiteJDBC {

	/**
	 * 通用的增删改 付 2013/12/12 20：25
	 * 
	 * @param sql
	 * @param paraMap
	 */
	public static boolean insertSQLite(String sql, List list) throws IOException {
		int result = 0;
		Connection c = null;
		PreparedStatement pstmt = null;
		String fileName = SQLiteJDBC.class.getResource("/") + "../../db/lxzwcj.db";// 数据库文件地址
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + fileName);
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");
			pstmt = c.prepareStatement(sql);
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					System.out.println("***************" + list.get(i).getClass().getName());
					if ("java.lang.String".equals(list.get(i).getClass().getName())) {// 如果是字符串
						System.out.println("*******1******" + list.get(i).getClass().getName());
						pstmt.setString(i + 1, list.get(i).toString());
					} else if ("[B".equals(list.get(i).getClass().getName())) {// 如果是图片
						System.out.println("******2*******" + list.get(i).getClass());
						ByteArrayInputStream b = new ByteArrayInputStream((byte[]) list.get(i));
						pstmt.setBinaryStream(i + 1, b, b.available());
						b.close();
					}
				}
			}
			result = pstmt.executeUpdate();
			c.commit();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (c != null) {
				try {
					c.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
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
	 * 查询 可以仿照insertSQLite(,,)方法写出通用查询，在此不再重复 查询结果存入map 因为公司一直用
	 * ibatis总数据持久层操作，故有此习惯；可灵活修改
	 */
	public static Map selectSQLite(String sql) {
		Map retMap = new HashMap();
		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;
		String fileName = SQLiteJDBC.class.getResource("/") + "../../db/lxzwcj.db";
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + fileName);
			stm = conn.createStatement();
			rs = stm.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();// rs为查询结果集
			int count = rsmd.getColumnCount();
			for (int i = 1; i <= count; i++) {
				String name = rsmd.getColumnName(i);
				if ("ZP".equals(name)) {// 如果是照片的话
					InputStream inputStream = rs.getBinaryStream(name);
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					int ch;
					while ((ch = inputStream.read()) != -1) {
						byteArrayOutputStream.write(ch);
					}
					byte[] b = byteArrayOutputStream.toByteArray();
					byteArrayOutputStream.close();
					retMap.put(name, b);
				} else {// 如果是字符串
					String value = rs.getString(name);
					retMap.put(name, value);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.toString());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (stm != null) {
				try {
					stm.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println("Opened database successfully");
		return retMap;
	}

	public void insertTP() throws IOException {
		/**
		 * 将图片转成byte[] ,这个图片可以来自本地，也可以来自上传，也可以来自form提交，在本例中来自一个本地图片
		 *
		 **/
		InputStream inputStream = new FileInputStream(new File("D:/a.jpeg"));
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		int ch;
		while ((ch = inputStream.read()) != -1) {
			byteArrayOutputStream.write(ch);
		}
		byte[] b = byteArrayOutputStream.toByteArray();
		byteArrayOutputStream.close();

		String sql = "insert into LXCJ_SFZ_ZP_TEMP (SLH,ZP)";// 拼装sql语句
		sql += " values (?,?) ";

		List list = new ArrayList();// 拼装参数
		list.add(0, "12345678");
		list.add(1, b);

		boolean result = insertSQLite(sql, list);// 插入图片
	}

	public static Map selectTP(Map paraMap) {
		Map retMap = new HashMap();
		String sql = "SELECT T.SLH,T.ZP FROM LXCJ_SFZ_ZP_TEMP T WHERE T.SLH= '" + paraMap.get("SLH") + "' ";
		retMap = selectSQLite(sql);
		return retMap;
	}

	public static void main(String[] args) {
		SQLiteJDBC t = new SQLiteJDBC();
		try {
			t.insertTP();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} // 插入图片

		/**
		 * 查询刚插入的照片，重新生成一张新的照片
		 */
		Map paraMap = new HashMap();
		paraMap.put("SLH", "12345678");
		Map retmap = selectTP(paraMap);
		byte[] bytes;
		File file = new File("D:/c.jpeg");
		FileOutputStream fos;
		try {
			bytes = (byte[]) retmap.get("ZP");
			fos = new FileOutputStream(file);
			fos.write(bytes);

			fos.flush();

			fos.close();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
