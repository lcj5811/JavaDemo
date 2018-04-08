package com.lee.algorithm.compress.string;

/**
 * 10进制数字字符串压缩
 * 
 * @ClassName com.lee.test
 * @description
 * @author 凌霄
 * @data 2017年5月3日 上午9:13:05
 */
public class test {
	public static void main(String[] args) {
		String min = "200000063000300";
		String loc = "2813626111299226399";
		String max = "56000000163000000";
		String input = loc;
		// String Hex_min = "94CF88B76492";
		// String Hex_max = "BDD7811CFC1D08";
		System.out.println("原始数组："+input);
		byte[] output = castLocationToByte(input);
		String HexString = castBytesToHexString(output);
		String srcString = castHexStringToDcmString(HexString);
		System.out.println(srcString);
	}

	public static byte[] castLocationToByte(String mLatLon) {
		String locStr = castDcmStringToHexString(mLatLon, 1);
		if (locStr.length() % 2 != 0)
			locStr = "0" + locStr;
		System.out.println("16进制数：" + locStr);
		byte[] locationByte = castHexStringToByte(locStr);
		for (byte b : locationByte) {
			System.out.println(b);
		}
		System.out.println("压缩后长度：" + locationByte.length);
		return locationByte;
	}

	/**
	 * 10进制字符串转16进制字符串
	 * 
	 * @param strDcmData
	 * @param intBytes
	 * @return
	 */
	public static final String castDcmStringToHexString(String strDcmData, int intBytes) {
		String strRet = null;
		long intNum = Long.parseLong(strDcmData, 10);
		String strHexData = castDcmIntToHexString(intNum);
		String strTempRet = String.valueOf(strHexData);
		int intLen = strTempRet.length();
		int intTempBytes = intBytes * 2 - intLen;
		String strTempByte = null;
		for (int i = 0; i < intTempBytes; ++i) {
			strTempByte = strTempByte == null ? "0" : strTempByte + "0";
		}
		strRet = strTempByte == null ? strTempRet : strTempByte + strTempRet;
		return strRet;
	}

	/**
	 * 16进制转byte[]
	 * 
	 * @param strHexData
	 * @return
	 */
	public static final byte[] castHexStringToByte(String strHexData) {
		if (strHexData != null) {
			byte[] bye = new byte[strHexData.length() / 2];
			int intLen = strHexData.length();
			int i = 0;
			int j = 0;
			while (i < intLen) {
				byte tmpByte1 = (byte) strHexData.charAt(i);
				byte bye1 = (byte) (castCharToHexByte((char) tmpByte1) << 4);
				byte tmpByte2 = (byte) strHexData.charAt(i + 1);
				byte bye2 = (byte) (castCharToHexByte((char) tmpByte2) & 15);
				bye[j] = (byte) (bye1 + bye2);
				i += 2;
				++j;
			}
			return bye;
		}
		return null;
	}

	/**
	 * 字符型转16进制byte
	 * 
	 * @param chr
	 * @return
	 */
	public static final byte castCharToHexByte(char chr) {
		byte chrRet = -1;
		if (chr >= '0' && chr <= '9') {
			chrRet = (byte) chr;
		} else if (chr >= 'A' && chr <= 'F') {
			chrRet = (byte) (chr - 65 + 10);
		} else if (chr >= 'a' && chr <= 'f') {
			chrRet = (byte) (chr - 97 + 10);
		}
		return chrRet;
	}

	/**
	 * 10进制整型转16进制字符串
	 * 
	 * @param intDcmData
	 * @return
	 */
	public static final String castDcmIntToHexString(long intDcmData) {
		String str = "";
		int yuShu = (int) (intDcmData % 16);
		str = "" + shuZhiToZhiMu(yuShu);
		for (long sun = intDcmData / 16; sun > 0; sun /= 16) {
			yuShu = (int) (sun % 16);
			str = shuZhiToZhiMu(yuShu) + str;
		}
		return str;
	}

	/**
	 * 数字转字母
	 * 
	 * @param intData
	 * @return
	 */
	private static final String shuZhiToZhiMu(int intData) {
		String strRet = "";
		switch (intData) {
		case 10: {
			strRet = "A";
			break;
		}
		case 11: {
			strRet = "B";
			break;
		}
		case 12: {
			strRet = "C";
			break;
		}
		case 13: {
			strRet = "D";
			break;
		}
		case 14: {
			strRet = "E";
			break;
		}
		case 15: {
			strRet = "F";
			break;
		}
		default: {
			strRet = "" + intData;
		}
		}
		return strRet;
	}

	/**
	 * byte[]转16进制字符串
	 * 
	 * @param byeData
	 * @return
	 */
	public static final String castBytesToHexString(byte[] byeData) {
		String strRet = null;
		int intLen = byeData.length;
		for (int i = 0; i < intLen; ++i) {
			byte byeTemp = byeData[i];
			String strHexTemp = Integer.toHexString(byeTemp);
			if (strHexTemp.length() > 2) {
				strHexTemp = strHexTemp.substring(strHexTemp.length() - 2);
			} else if (strHexTemp.length() < 2) {
				strHexTemp = "0" + strHexTemp;
			}
			strRet = i == 0 ? strHexTemp : strRet + strHexTemp;
		}
		strRet = strRet.toUpperCase();
		return strRet;
	}

	/**
	 * 16进制转10进制字符串
	 * 
	 * @param strHexData
	 * @return
	 */
	public static final String castHexStringToDcmString(String strHexData) {
		String strDcmData = null;
		Long lngNum = Long.parseLong(strHexData, 16);
		strDcmData = String.valueOf(lngNum);
		return strDcmData;
	}

}
