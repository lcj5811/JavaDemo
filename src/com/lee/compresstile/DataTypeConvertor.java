package com.lee.compresstile;

/**
 * 
 * @ClassName: DataTypeConvertor
 * @Description: 数据类型转换器
 * @author 苦丁茶
 * @date 2015年4月24日 下午3:39:27
 */
public class DataTypeConvertor {
	// 求得最低一个字节
	private final static int lowestDigit = 0x00FF;
	// 一个字节的位数、整型的字节数、长整形的字节数
	private final static int bitNumInByte = 8;
	public final static int intByteNum = Integer.SIZE / bitNumInByte;
	public final static int longByteNum = Long.SIZE / bitNumInByte;

	/**
	 * 整型转字节数组
	 * 
	 * @param value
	 * @return byte[]
	 */
	public static byte[] intToByteArray(int value) {
		byte[] byteArray = new byte[intByteNum];
		int temp = value;
		for (int i = byteArray.length - 1; i >= 0; --i) {
			byteArray[i] = new Integer(temp & lowestDigit).byteValue();
			temp >>= bitNumInByte;
		}
		return byteArray;
	}

	/**
	 * 长整型转字节数组
	 * 
	 * @param value
	 * @return byte[]
	 */
	public static byte[] longToByteArray(long value) {
		byte[] byteArray = new byte[longByteNum];
		long temp = value;
		for (int i = byteArray.length - 1; i >= 0; --i) {
			byteArray[i] = new Long(temp & lowestDigit).byteValue();
			temp >>= bitNumInByte;
		}
		return byteArray;
	}

	/**
	 * 
	 * 双精度型转字节数组
	 * 
	 * @param value
	 * @return byte[]
	 */
	public static byte[] doubleToByteArray(double value) {
		long longBits = Double.doubleToLongBits(value);
		return longToByteArray(longBits);
	}

	/**
	 * 字节数组转整型
	 * 
	 * @param byteArray
	 * @return int
	 */
	public static int byteArrayToInt(byte[] byteArray) {
		int len = byteArray.length;
		int value = 0;
		if (len != intByteNum) {
			return value;
		}
		for (int i = 0; i < len; ++i) {
			value <<= bitNumInByte;
			value += (byteArray[i] & lowestDigit);
		}
		return value;
	}

	/**
	 * 字节数组转长整型
	 * 
	 * @param byteArray
	 * @return long
	 */
	public static long byteArrayToLong(byte[] byteArray) {
		int len = byteArray.length;
		long value = 0;
		if (len != longByteNum) {
			return value;
		}
		for (int i = 0; i < len; ++i) {
			value <<= bitNumInByte;
			value += (byteArray[i] & lowestDigit);
		}
		return value;
	}

	/**
	 * 字节数组转双精度型
	 * 
	 * @param byteArray
	 * @return double
	 */
	public static double byteArrayToDouble(byte[] byteArray) {
		long longBits = byteArrayToLong(byteArray);
		return Double.longBitsToDouble(longBits);
	}
}
