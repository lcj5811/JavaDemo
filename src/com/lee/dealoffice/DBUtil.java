package com.lee.dealoffice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class DBUtil {
	Connection con = null;
	ResultSet rs = null;
	OldConAccess CA = null;
	PreparedStatement pst = null;
	OldConAccess ca = null;

	public DBUtil(String DB_PathName) {
		ca = new OldConAccess(DB_PathName);
		con = ca.con;
		rs = ca.rs;
	}

	public String QueryFBFBM(String value) {
		try {
			String sql = "select 行证区代码 from XZQ where 行证区名称 = ?";
			pst = con.prepareStatement(sql);
			pst.setString(1, value);
			rs = pst.executeQuery();
			if (rs.next()) {
				return rs.getString("行证区代码");
			}
			return "无编码";
		} catch (Exception e) {
			System.out.println("QueryFBFBM");
			System.out.println(value);
			System.out.println(e);
			return QueryFBFBM(value);
		}
	}

	public String QueryGXDM(String GX) {
		try {
			String sql = "select 代码 from List_Relationship where 与户主关系  like ?";
			pst = con.prepareStatement(sql);
			pst.setString(1, "%" + GX + "%");
			rs = pst.executeQuery();
			if (rs.next()) {
				return rs.getString("代码");
			}
			return "97";
		} catch (Exception e) {
			System.out.println("QueryGXDM");
			System.out.println(e);
			return QueryGXDM(GX);
		}

	}

	public String QueryXBDM(String GX) {
		try {
			String sql = "select 性别 from List_Relationship where 与户主关系  like ?";
			pst = con.prepareStatement(sql);
			pst.setString(1, "%" + GX + "%");
			rs = pst.executeQuery();
			if (rs.next()) {
				return rs.getString("性别");
			}
			return "1";
		} catch (Exception e) {
			System.out.println("QueryXBDM");
			System.out.println(e);
			return QueryXBDM(GX);
		}

	}

	public ArrayList<String> QueryZDBH(String Z, String C) {
		ArrayList<String> names = new ArrayList<String>();
		try {
			String sql = "select ZDBH from " + Z + "_" + C;
			pst = con.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				names.add(rs.getString("ZDBH"));
			}
		} catch (Exception e) {
			System.out.println("QueryZDBH");
			System.out.println(e);
		}
		return names;
	}

	public ArrayList<String> QueryDKBM(String Z, String C) {
		ArrayList<String> dkbm = new ArrayList<String>();
		try {
			String sql = "select DKBM from TableB01_CBDKXX";
			pst = con.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				dkbm.add(rs.getString("DKBM"));
			}
		} catch (Exception e) {
			System.out.println("QueryZDBH");
			System.out.println(e);
		}
		return dkbm;
	}

	public ArrayList<String> QueryDKMC(String Z, String C, String ZDBH) {
		ArrayList<String> DKMC = new ArrayList<String>();
		try {
			String sql = "select DKMC,DKDZ,DKXZ,DKNZ,DKBZ,HTMJ_M,DLDJ,JBNT from "
					+ Z + "_" + C + " where ZDBH = ?";
			pst = con.prepareStatement(sql);
			pst.setString(1, ZDBH);
			rs = pst.executeQuery();
			// while (rs.next()) {
			// DKMC.add(rs.getString("DKMC"));
			// DKMC.add(rs.getString("HTMJ_M"));
			// DKMC.add(rs.getString("DKDZ"));
			// DKMC.add(rs.getString("DKXZ"));
			// DKMC.add(rs.getString("DKNZ"));
			// DKMC.add(rs.getString("DKBZ"));
			// DKMC.add(rs.getString("DLDJ"));
			// DKMC.add(rs.getString("JBNT"));
			// }
			if (rs.next()) {
				DKMC.add(rs.getString("DKMC"));
				DKMC.add(rs.getString("HTMJ_M"));
				DKMC.add(rs.getString("DKDZ"));
				DKMC.add(rs.getString("DKXZ"));
				DKMC.add(rs.getString("DKNZ"));
				DKMC.add(rs.getString("DKBZ"));
				DKMC.add(rs.getString("DLDJ"));
				DKMC.add(rs.getString("JBNT"));
			} else {
				DKMC.add("");
				DKMC.add("");
				DKMC.add("");
				DKMC.add("");
				DKMC.add("");
				DKMC.add("");
				DKMC.add("");
				DKMC.add("");
			}
		} catch (Exception e) {
			System.out.println("QueryDKMC");
			e.printStackTrace();
			QueryDKMC(Z, C, ZDBH);
		}
		return DKMC;
	}

	public String QueryHTMJ_M(String Z, String C, String ZDBH) {
		String names = "";
		try {
			String sql = "select HTMJ_M from " + Z + "_" + C
					+ " Where ZDBH = ?";
			pst = con.prepareStatement(sql);
			pst.setString(1, ZDBH);
			rs = pst.executeQuery();
			while (rs.next()) {
				names = rs.getString("HTMJ_M");
			}
		} catch (Exception e) {
			System.out.println("QueryHTMJ_M");
			System.out.println(e);
		}
		return names;
	}

	public String QueryHTMJ(String Z, String C, String ZDBH) {
		String names = "";
		try {
			String sql = "select HTMJ from " + Z + "_" + C + " Where ZDBH = ?";
			pst = con.prepareStatement(sql);
			pst.setString(1, ZDBH);
			rs = pst.executeQuery();
			while (rs.next()) {
				names = rs.getString("HTMJ");
			}
		} catch (Exception e) {
			System.out.println("QueryHTMJ");
			System.out.println(e);
		}
		return names;
	}

	public String QueryCZ(String Z, String C, String ZDBH) {
		String names = "";
		try {
			String sql = "select NAME,ZM from " + Z + "_" + C
					+ " Where ZDBH = ?";
			pst = con.prepareStatement(sql);
			pst.setString(1, ZDBH);
			rs = pst.executeQuery();
			while (rs.next()) {
				String ZM = rs.getString("ZM");
				if (ZM.indexOf("十") != -1) {
					ZM = ZM.replace("十", "1");
				} else if (ZM.indexOf("七") != -1) {
					ZM = ZM.replace("七", "1");
				} else if (ZM.indexOf("九") != -1) {
					ZM = ZM.replace("九", "1");
				} else if (ZM.indexOf("上") != -1) {
					ZM = ZM.replace("上", "");
				} else if (ZM.indexOf("下") != -1) {
					ZM = ZM.replace("下", "");
				}
				names = rs.getString("NAME") + ZM;
			}
		} catch (Exception e) {
			System.out.println("QueryCZ");
			System.out.println(e);
		}
		return names;
	}

	public String QueryCBR(String Z, String C, String ZDBH) {
		String names = "";
		try {
			String sql = "select CBR from " + Z + "_" + C + " Where ZDBH = ?";
			pst = con.prepareStatement(sql);
			pst.setString(1, ZDBH);
			rs = pst.executeQuery();
			while (rs.next()) {
				names = rs.getString("CBR");
			}
		} catch (Exception e) {
			System.out.println("QueryCBR");
			System.out.println(e);
		}
		return names;
	}

	public ArrayList<String> QueryCBFBM() {
		ArrayList<String> CBFBM = new ArrayList<String>();
		try {
//			String sql = "select CBFBM from TableB03_CBF";
			String sql = "select CBFBM from CBF";
			pst = con.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				CBFBM.add(rs.getString("CBFBM"));
			}
		} catch (Exception e) {
			System.out.println("QueryCBFBM");
			System.out.println(e);
		}
		return CBFBM;
	}

	public HashMap<String, String> QueryCBFBMandCBFMC() {
		HashMap<String, String> CBFBMandCBFMC = new HashMap<String, String>();
		try {
//			String sql = "select CBFBM,CBFMC from TableB03_CBF";
			String sql = "select CBFBM,CBFMC from CBF";
			pst = con.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				CBFBMandCBFMC.put(rs.getString("CBFBM"), rs.getString("CBFMC"));
			}
		} catch (Exception e) {
			System.out.println("QueryCBFBMandCBFMC");
			System.out.println(e);
		}
		return CBFBMandCBFMC;
	}

	public String QueryCBFBM_CZ(String CYXM, String CBFDZ) {
		String names = "";
		try {
			String sql = "select CBFBM from TableB03_CBF Where CBFMC = ? and CBFDZ = ?";
			pst = con.prepareStatement(sql);
			pst.setString(1, CYXM);
			pst.setString(2, CBFDZ);
			rs = pst.executeQuery();
			while (rs.next()) {
				names = rs.getString("CBFBM");
			}
		} catch (Exception e) {
			System.out.println("QueryCBFBM_CZ");
			System.out.println(e);
		}
		return names;
	}

	public String QueryCBFBM_CBR(String CYXM) {
		String names = "";
		try {
			String sql = "select CBFBM from TableB03_CBF Where CBFMC = ?";
			pst = con.prepareStatement(sql);
			pst.setString(1, CYXM);
			rs = pst.executeQuery();
			while (rs.next()) {
				names = rs.getString("CBFBM");
			}
		} catch (Exception e) {
			System.out.println("QueryCBFBM_CBR");
			System.out.println(e);
		}
		return names;
	}

	public ArrayList<String> QueryCBHTBM() {
		ArrayList<String> names = new ArrayList<String>();
		try {
			String sql = "select distinct CBHTBM from TableB01_CBDKXX";
			pst = con.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				names.add(rs.getString("CBHTBM"));
			}
		} catch (Exception e) {
			System.out.println("QueryCBHTBM");
			System.out.println(e);
		}
		return names;
	}

	public String QueryFBFBM2(String CBHTBM) {
		String names = "";
		try {
			String sql = "select distinct FBFBM from TableB01_CBDKXX Where CBHTBM = ?";
			pst = con.prepareStatement(sql);
			pst.setString(1, CBHTBM);
			rs = pst.executeQuery();
			while (rs.next()) {
				names = rs.getString("FBFBM");
			}
		} catch (Exception e) {
			System.out.println("QueryFBFBM2");
			System.out.println(e);
		}
		return names;
	}

	public String QueryCBFBM2(String CBHTBM) {
		String names = "";
		try {
			String sql = "select distinct CBFBM from TableB01_CBDKXX Where CBHTBM = ?";
			pst = con.prepareStatement(sql);
			pst.setString(1, CBHTBM);
			rs = pst.executeQuery();
			while (rs.next()) {
				names = rs.getString("CBFBM");
			}
		} catch (Exception e) {
			System.out.println("QueryCBFBM2");
			System.out.println(e);
		}
		return names;
	}

	public void InsertFBF(String[] value) {
		try {
			String sql = "insert into TableB02_FBF (FBFBM, FBFMC) values (?, ?)";
			pst = con.prepareStatement(sql);
			int i = 1;
			for (String a : value) {
				pst.setString(i++, a);
			}
			if (pst.execute()) {
				System.out.println("插入失败");
			}
		} catch (Exception e) {
			System.out.println(e);
			System.out.println(value[0]);
			InsertFBF(value);
		}
	}

	public void InsertCBF(String[] value) throws SQLException {
		String sql = "insert into TableB03_CBF (CBFBM,CBFLX,CBFMC,CBFZJLX,CBFZJHM,CBFDZ,YHTMJ,YHTDKS,CBFCYSL) values (?,?,?,?,?,?,?,?,?)";
		pst = con.prepareStatement(sql);
		int i = 1;
		for (String a : value) {
			switch (i) {
			case 7:
				if (a != null) {
					pst.setDouble(i++, Double.valueOf(a));
				} else {
					pst.setDouble(i++, 0);
				}
				break;
			case 8:
				if (a != null) {
					pst.setInt(i++, Integer.valueOf(a));
				} else {
					pst.setDouble(i++, 0);
				}
				break;
			case 9:
				pst.setInt(i++, 1);
				break;
			default:
				pst.setString(i++, a);
				break;
			}
		}
		if (pst.execute()) {
			System.out.println("插入失败");
		}
	}

	public void InsertJTCY(String[] value) {
		try {
			String sql = "insert into TableB04_CBF_JTCY  (CBFBM,CYXM,CYXB,CYZJLX,CYZJHM,YHZGX,CYBZ,SFGYR) values (?,?,?,?,?,?,?,?)";
			pst = con.prepareStatement(sql);
			int i = 1;
			for (String a : value) {
				pst.setString(i++, a);
			}
			if (pst.execute()) {
				System.out.println("插入失败");
			}
		} catch (Exception e) {
			System.out.println("InsertJTCY");
			System.out.println(value[1]);
			InsertJTCY(value);
		}
	}

	public void InsertCBDKXX(String[] value, String Z, String C) {
		try {
			String sql = "insert into TableB01_CBDKXX  (DKBM,CBFBM,HTMJ_old,HTMJ,FBFBM,CBHTBM) values (?,?,?,?,?,?)";
			pst = con.prepareStatement(sql);
			int i = 1;
			for (String a : value) {
				// System.out.println(a);
				pst.setString(i++, a);
			}
			if (pst.execute()) {
				System.out.println("插入失败");
			}
		} catch (Exception e) {
			System.out.println("InsertCBDKXX");
			System.out.println(e);
			ArrayList<String> a = new ArrayList<String>();
			a.add(value[3]);
			a.add(String.valueOf(e));
			com.lee.dealfile.FileName.saveText("G:/workspace_test/问题/" + Z
					+ "/" + C + "/" + value[0] + ".txt", a, null);
			for (String n : value) {
				System.out.println(n);
			}
		}
	}

	public void InsertCBHT(String[] value, String QS, String JZ) {
		try {
			String sql = "insert into TableB05_CBHT  (CBHTBM,FBFBM,CBFBM,CBQXQ,CBQXZ) values (?,?,?,#"
					+ QS + "#,#" + JZ + "#)";
			pst = con.prepareStatement(sql);
			int i = 1;
			for (String a : value) {
				// System.out.println(a);
				pst.setString(i++, a);
			}
			if (pst.execute()) {
				System.out.println("插入失败");
			}
		} catch (Exception e) {
			System.out.println("InsertCBHT");
			System.out.println(e);
			InsertCBHT(value, QS, JZ);
		}
	}

	public void InsertDK(ArrayList<String> value) {
		try {
			String sql = "insert into TableA10_DK (BSM,DKBM,DKMC,SYQXZ,DKLB,TDYT,SCMJ,DKDZ,DKXZ,DKNZ,DKBZ,DLDJ,BFJBNT) values (?,?,?,'31','10','1',?,?,?,?,?,?,?)";
			pst = con.prepareStatement(sql);
			int i = 1;
			for (String a : value) {
				System.out.println(a);
				if (i == 1) {
					pst.setInt(i++, Integer.valueOf(a));
				} else {
					pst.setString(i++, a);
				}
			}
			if (pst.execute()) {
				System.out.println("插入失败");
			}
		} catch (Exception e) {
			// System.out.println("InsertDK");
			e.printStackTrace();
			InsertDK(value);
		}
	}

	public ArrayList<String> QueryXM() throws SQLException {
		ArrayList<String> names = new ArrayList<String>();
		try {
			String sql = "select CBFMC from TableB03_CBF";
			pst = con.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				names.add(rs.getString("CBFMC"));
			}
		} catch (Exception e) {
			System.out.println("QueryXM");
			System.out.println(e);
		}
		return names;
	}

	public ArrayList<String> QueryFZRXM() {
		ArrayList<String> names = new ArrayList<String>();
		try {
			String sql = "select FBFFZRXM from TableB02_FBF";
			pst = con.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				names.add(rs.getString("FBFFZRXM"));
			}
		} catch (Exception e) {
			System.out.println("QueryFZR");
			System.out.println(e);
		}
		return names;
	}

	public String QueryFBFBM1(String FBFMC) {
		String names = "";
		try {
			String sql = "select FBFBM from TableB02_FBF where FBFMC = ?";
			pst = con.prepareStatement(sql);
			pst.setString(1, FBFMC);
			rs = pst.executeQuery();
			while (rs.next()) {
				names = rs.getString("FBFBM");
			}
		} catch (Exception e) {
			System.out.println("QueryFBFBM1");
			System.out.println(e);
		}
		return names;
	}

	public String QueryFBFFZRZJH(String XM) {
		String names = "";
		try {
			String sql = "select CYZJHM from TableB04_CBF_JTCY where CYXM = ?";
			pst = con.prepareStatement(sql);
			pst.setString(1, XM);
			rs = pst.executeQuery();
			while (rs.next()) {
				names = rs.getString("CYZJHM");
			}
		} catch (Exception e) {
			System.out.println("QueryFBFFZRZJH");
			System.out.println(e);
		}
		return names;
	}

	public String QueryFBFDZ(String XM) {
		String names = "";
		try {
			String sql = "select FBFMC from TableB02_FBF where FBFFZRXM = ?";
			pst = con.prepareStatement(sql);
			pst.setString(1, XM);
			rs = pst.executeQuery();
			while (rs.next()) {
				names = rs.getString("FBFMC");
			}
		} catch (Exception e) {
			System.out.println("QueryFBFMC");
			System.out.println(e);
		}
		return names;
	}

	public String QuerySFZH(String XM, String ZM, String CM) {
		String SFZH = "";
		try {
			String sql = "select SFZ from " + ZM + CM + " where CBR = ?";
			pst = con.prepareStatement(sql);
			pst.setString(1, XM);
			rs = pst.executeQuery();
			if (rs.next()) {
				SFZH = rs.getString(1);
			}
		} catch (Exception e) {
			System.out.println("QuerySFZH");
			System.out.println(XM);
			System.out.println(e);
			QuerySFZH(XM, ZM, CM);
		}
		return SFZH;
	}

	public void updateZJDZ(String SFZHM, String FBFDZ, String FBFFZRXM) {
		try {
			String sql = "update TableB02_FBF set FZRZJLX = ? , FZRZJHM = ? , FBFDZ = ? where FBFFZRXM = ?";
			pst = con.prepareStatement(sql);
			pst.setString(1, "1");
			pst.setString(2, SFZHM);
			pst.setString(3, FBFDZ);
			pst.setString(4, FBFFZRXM);
			if (pst.execute()) {
				System.out.println("插入失败");
			}
		} catch (Exception e) {
			// System.out.println("updateSFZH");
			// System.out.println(XM);
			// System.out.println(e);
			updateZJDZ(SFZHM, FBFDZ, FBFFZRXM);
		}
	}

	public void updateSFZH(String XM, String SFZH) {
		try {
			String sql = "update TableB03_CBF set CBFZJHM = ?  where CBFMC = ?";
			pst = con.prepareStatement(sql);
			pst.setString(1, SFZH);
			pst.setString(2, XM);
			if (pst.execute()) {
				System.out.println("插入失败");
			}
			sql = "update TableB04_CBF_JTCY set CYZJHM = ?  where CYXM = ?";
			pst = con.prepareStatement(sql);
			pst.setString(1, SFZH);
			pst.setString(2, XM);
			if (pst.execute()) {
				System.out.println("插入失败");
			}
		} catch (Exception e) {
			System.out.println("updateSFZH");
			System.out.println(XM);
			System.out.println(e);
			updateSFZH(XM, SFZH);
		}
	}

	public void updateCYSL(String CBFBM) {
		try {
			String CBFCYSL = "";
			String sql = "select count(CBFBM) from TableB04_CBF_JTCY where CBFBM = ?";
			pst = con.prepareStatement(sql);
			pst.setString(1, CBFBM);
			rs = pst.executeQuery();
			if (rs.next()) {
				CBFCYSL = rs.getString(1);
			}

			sql = "update TableB03_CBF set CBFCYSL=? where CBFBM=?";
			pst = con.prepareStatement(sql);
			pst.setInt(1, Integer.valueOf(CBFCYSL));
			pst.setString(2, CBFBM);
			if (pst.execute()) {
				System.out.println("插入失败");
			}
		} catch (Exception e) {
			System.out.println("updateCYSL");
			System.out.println(CBFBM);
			updateCYSL(CBFBM);
		}
	}

	public String[] Query_MJ_DK(String ZM, String CM, String Name) {
		try {
			String[] r = new String[2];
			String sql = "select count(CBMJ) from " + ZM + "_" + CM
					+ " where XM = ?";
			pst = con.prepareStatement(sql);
			pst.setString(1, Name);
			rs = pst.executeQuery();
			if (rs.next()) {
				r[0] = rs.getString(1);
			}
			sql = "select sum(CBMJ) from " + ZM + "_" + CM + " where XM = ?";
			pst = con.prepareStatement(sql);
			pst.setString(1, Name);
			rs = pst.executeQuery();
			if (rs.next()) {
				r[1] = rs.getString(1);
			}
			return r;
		} catch (Exception e) {
			System.out.println("Query_MJ_DK");
			System.out.println(Name);
			System.out.println(e);
			return Query_MJ_DK(ZM, CM, Name);
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
		ca.CloseDB();
	}

}
