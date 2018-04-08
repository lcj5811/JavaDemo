package com.lee.algorithm.compress.string;

import java.util.HashMap;
import java.util.Map;

public class Lz77 {
	public static void main(String[] args) {
		char[] a = "中文".toCharArray();
		System.out.println(Lz77Decompress(a));
	}

	public static char[] NC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~!@#$%^&*()-=[];',./_+{}|:\"<>?"
			.toCharArray();
	public static Map<Character, Integer> CN = new HashMap<Character, Integer>();
	static {
		for (int i = 0; i < NC.length; i++) {
			CN.put(NC[i], i);
		}
	}

	public static String Lz77Decompress(char[] input) {
		/* LZ77解压缩算法 - Hutia - JS版 */

		/* 变量声明 */
		int p = 0; // 扫描指针
		int len = input.length; // 输入字符串的长度
		StringBuffer output = new StringBuffer();
		int match_off = 0; // 匹配位置的偏移量
		int match_len = 0; // 发生匹配的长度

		/* 循环扫描 */
		for (p = 0; p < len; p++) {
			if (input[p] == '`') // 如果发现前缀标记
			{
				if (input[p + 1] == '`') // 如果是转义前缀
				{
					output.append("`"); // 直接输出字符 "`"
					p++; // 指针后移，跳过下一个字符
				} else // 如果是压缩编码
				{
					match_off = C2N(input[p + 1] + "" + input[p + 2] + input[p + 3]); // 取出其
																						// 1-3
																						// 个字符，算出偏移量
					match_len = C2N(input[p + 4] + "" + input[p + 5]); // 取出其
																		// 4-5
																		// 字符，算出匹配长度
					output.append(output.substring(match_off, match_off + match_len));
					p += 5; // 指针后移，跳过下5个字符
				}
			} else // 如果没有发现前缀标记
			{
				output.append(input[p]); // 直接输出相应的字符
			}
		}

		/* 输出 */
		return output.toString();
	}

	public static int C2N(String c) // 将 92 进制字符串（高位在右）转换为 10 进制数字
	{
		char[] ct = c.toCharArray();
		int len = ct.length;
		int re = 0;
		for (int i = 0; i < len; i++) {
			re += CN.get(ct[i]) * Math.pow(92, i);
		}
		return re;
	}

}
