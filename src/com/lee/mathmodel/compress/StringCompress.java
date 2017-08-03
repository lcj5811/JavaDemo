package com.lee.mathmodel.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @ClassName com.lee.mathmodel.compress.StringCompress
 * @description
 * @author 凌霄
 * @data 2017年5月29日 下午3:53:14
 */
public class StringCompress {

	public static void main(String[] args) {

		byte acc = 114;
		byte bdd = 3;
		System.out.println(acc | bdd);

		
		
//		byte[] a = hexStringToBytes("B5E624B5CDC0");
//		System.out.println(a.length);
//
//		System.out.println("-----------");
//		char ab = '1';
//		byte ac = charToByte(ab);
//
//		System.out.println(ac);
//		byte b = (byte) (charToByte(ab) << 4);
//		System.out.println(b);

	}

	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase().replace(" ", "");
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			System.out.println((charToByte(hexChars[pos]) << 4) + "," + (charToByte(hexChars[pos + 1])));
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
			System.out.println(d[i]);
		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public static final byte[] compress(String paramString) {
		if (paramString == null)
			return null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		ZipOutputStream zipOutputStream = null;
		byte[] arrayOfByte;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
			zipOutputStream.putNextEntry(new ZipEntry("0"));
			zipOutputStream.write(paramString.getBytes());
			zipOutputStream.closeEntry();
			arrayOfByte = byteArrayOutputStream.toByteArray();
		} catch (IOException localIOException5) {
			arrayOfByte = null;
		} finally {
			if (zipOutputStream != null)
				try {
					zipOutputStream.close();
				} catch (IOException localIOException6) {
				}
			if (byteArrayOutputStream != null)
				try {
					byteArrayOutputStream.close();
				} catch (IOException localIOException7) {
				}
		}
		return arrayOfByte;
	}

	@SuppressWarnings("unused")
	public static final String decompress(byte[] paramArrayOfByte) {
		if (paramArrayOfByte == null)
			return null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		ByteArrayInputStream byteArrayInputStream = null;
		ZipInputStream zipInputStream = null;
		String str;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			byteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
			zipInputStream = new ZipInputStream(byteArrayInputStream);
			ZipEntry localZipEntry = zipInputStream.getNextEntry();
			byte[] arrayOfByte = new byte[1024];
			int i = -1;
			while ((i = zipInputStream.read(arrayOfByte)) != -1)
				byteArrayOutputStream.write(arrayOfByte, 0, i);
			str = byteArrayOutputStream.toString();
		} catch (IOException localIOException7) {
			str = null;
		} finally {
			if (zipInputStream != null)
				try {
					zipInputStream.close();
				} catch (IOException localIOException8) {
				}
			if (byteArrayInputStream != null)
				try {
					byteArrayInputStream.close();
				} catch (IOException localIOException9) {
				}
			if (byteArrayOutputStream != null)
				try {
					byteArrayOutputStream.close();
				} catch (IOException localIOException10) {
				}
		}
		return str;
	}
}
