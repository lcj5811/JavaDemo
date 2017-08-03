package com.lee.mathmodel.key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @ClassName com.lee.mathmodel.key.TriDES
 * @description
 * @author 凌霄
 * @data 2017年4月17日 上午11:34:45
 */
public class TriDES {

	private static final String Algorithm = "DESede";

	// 加密
	public static String encryptMode(byte[] keybyte, String src) {
		try {
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			return byte2Hex(c1.doFinal(src.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
			// e.getMessage();
		}
		return null;
	}

	// 解密
	public static String decryptMode(byte[] keybyte, String src) {
		try {
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return new String(c1.doFinal(hex2Byte(src)));
		} catch (Exception e) {
			e.printStackTrace();
			// e.getMessage();
		}
		return null;
	}

	public static String byte2Hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}

	public static byte[] hex2Byte(String str) {
		if (str == null)
			return null;
		str = str.trim();
		int len = str.length();
		if (len == 0 || len % 2 == 1)
			return null;
		byte[] b = new byte[len / 2];
		try {
			for (int i = 0; i < str.length(); i += 2) {
				b[i / 2] = (byte) Integer.decode("0x" + str.substring(i, i + 2)).intValue();
			}
			return b;
		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String[] args) {
		final byte[] keyBytes = { 0x11, 0x22, 0x4F, 0x58, (byte) 0x88, 0x10, 0x40, 0x38, 0x28, 0x25, 0x79, 0x51,
				(byte) 0xCB, (byte) 0xDD, 0x55, 0x66, 0x77, 0x29, 0x74, (byte) 0x98, 0x30, 0x40, 0x36, (byte) 0xE2 };
		String szSrc = "20AE4BB23CC89EAB1FF380C0225EF848";
		System.out.println("加密前的字符串:\n" + szSrc + "\n");

		String encoded = encryptMode(keyBytes, szSrc);
		System.out.println("加密后的字符串:\n" + encoded + "\n");

		String srcBytes = decryptMode(keyBytes, encoded);
		System.out.println("解密后的字符串:\n" + srcBytes + "\n");
	}
}
