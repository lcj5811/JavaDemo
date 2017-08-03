package com.lee.gis.downloadmap;

/**
 * 数据库构建参数
 * 
 * @author Lee
 *
 */
public class SQLiteConst {

	// 主键
	public static final String F_PK = "key";
	// 瓦片表名
	public static final String T_FIELD_DEFINE = "tiles";
	public static final String T_METADATA_DEFINE = "metadata";

	public static final String[] TableName = { T_FIELD_DEFINE, T_METADATA_DEFINE };
	// 瓦片表字段
	public static final String[] SQLCreateTable = {
			"create table if not exists " + T_FIELD_DEFINE + "(" + F_PK
					+ " integer primary key, provider text, tile blob)",
			"create table if not exists " + T_METADATA_DEFINE
					+ "(name_cn text, name_en text, min_z text, max_z text, format text, urltype text, cp text)" };

	public static final String[] SQLInsertData = {};

}
