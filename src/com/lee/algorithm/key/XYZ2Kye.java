package com.lee.algorithm.key;

import com.lee.util.SystemUtil;

/**
 * @ClassName com.lee.algorithm.key.XYZ2Kye
 * @description
 * @author 凌霄
 * @data 2018年5月8日 上午11:19:45
 */
public class XYZ2Kye {

	public static void main(String[] args) {
		XYZ2Kye test = new XYZ2Kye();
		long start = System.currentTimeMillis();
		for (int x = 0; x < 100; x++) {
			for (int y = 0; y < 100; y++) {
				for (int z = 0; z < 10000; z++) {
					// test.getkey1(x, y, z);
					// test.getkey2(x, y, z);
					// test.getkey3(3, 2, 2);
				}
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("耗时：" + SystemUtil.getInstance().getRunTime(start, end));
	}

	public long getkey1(long x, long y, long z) {
		return ((z << z) + x << z) + y;
	}

	public long getkey2(long x, long y, long z) {
		return (long) (Math.pow(2, 2 * z) * z + Math.pow(2, z) * x + y);
	}

	public long getkey3(long x, long y, long z) {
		return (long) (Math.pow(2, z) * (Math.pow(2, z) * z + x) + y);
	}

}
