package com.lee.mathmodel.lockset;

/**
 * @ClassName com.lee.mathmodel.lockset.Lockset
 * @description 锁具装箱问题
 * @author 凌霄
 * @data 2017年3月23日 上午10:00:54
 */
public class Lockset {

	public static void main(String[] args) {
		int i = 0;
		for (int a = 0; a < 6; a++) {
			for (int b = 0; b < 6; b++) {
				for (int c = 0; c < 6; c++) {
					for (int d = 0; d < 6; d++) {
						for (int e = 0; e < 6; e++) {
							if (FourJudge(a, b, c, d, e) || ThreeJudge(a, b, c, d, e) || absFiveJudge(a, b, c, d, e)) {
								System.out.println(a + "," + b + "," + c + "," + d + "," + e);
								System.out.println(++i);
							}
						}
					}
				}
			}
		}
		System.err.println((7776 - i));
	}

	/**
	 * 判断五个值中四个是否相等
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param e
	 * @return
	 */
	private static boolean FourJudge(int a, int b, int c, int d, int e) {
		if (a == b & b == c & c == d) {
			return true;
		} else if (a == c & c == d & d == e) {
			return true;
		} else if (a == b & b == c & c == e) {
			return true;
		} else if (a == b & b == d & d == e) {
			return true;
		} else if (b == c & c == d & d == e) {
			return true;
		}
		return false;
	}

	/**
	 * 判断五个值中三个是否相等以及另外两个值是否相等
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param e
	 * @return
	 */
	private static boolean ThreeJudge(int a, int b, int c, int d, int e) {
		if (a == b & b == c) {
			if (d == e) {
				return true;
			} else {
				return false;
			}
		} else if (a == c & c == d) {
			if (b == e) {
				return true;
			} else {
				return false;
			}
		} else if (b == c & c == e) {
			if (a == d) {
				return true;
			} else {
				return false;
			}
		} else if (a == b & b == d) {
			if (c == e) {
				return true;
			} else {
				return false;
			}
		} else if (b == c & c == d) {
			if (a == e) {
				return true;
			} else {
				return false;
			}
		} else if (c == d & d == e) {
			if (a == b) {
				return true;
			} else {
				return false;
			}
		} else if (a == d & d == e) {
			if (b == c) {
				return true;
			} else {
				return false;
			}
		} else if (b == d & d == e) {
			if (a == c) {
				return true;
			} else {
				return false;
			}
		} else if (a == c & c == e) {
			if (d == b) {
				return true;
			} else {
				return false;
			}
		} else if (a == b & b == e) {
			if (c == d) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * 判断绝对值是否等于五
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param e
	 * @return
	 */
	private static boolean absFiveJudge(int a, int b, int c, int d, int e) {
		if (Math.abs(a - b) == 5) {
			return true;
		} else if (Math.abs(b - c) == 5) {
			return true;
		} else if (Math.abs(c - d) == 5) {
			return true;
		} else if (Math.abs(d - e) == 5) {
			return true;
		}
		return false;
	}

}
