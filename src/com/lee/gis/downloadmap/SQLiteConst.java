package com.lee.gis.downloadmap;

public class SQLiteConst {

	// 主键
	public static final String F_PK = "key";
	// 瓦片表名
	public static final String T_FIELD_DEFINE = "tiles";
	public static final String[] TableName = { T_FIELD_DEFINE };
	// 瓦片表字段
	public static final String[] SQLCreateTable = { "create table if not exists " + T_FIELD_DEFINE + "(" + F_PK
			+ " integer primary key, provider text, tile blob)", };

	public static final String[] SQLInsertData = {};

}
