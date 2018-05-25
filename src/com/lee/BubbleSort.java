package com.lee;

/**
 * @ClassName com.lee.S
 * @description 冒泡排序
 * @author 凌霄
 * @data 2017年11月27日 下午9:32:56
 */
public class BubbleSort {
	private int[] a = new int[] { 3, 2, 5, 21, 9, 10, 7, 16, 8, 20 };

	public static void main(String[] args) {
		BubbleSort s = new BubbleSort();
		s.name(s.a);
		for (int c : s.a) {
			System.out.println(c);
		}
	}

	private void name(int[] data) {
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data.length - 1 - i; j++) {
				if (data[j] > data[j + 1]) {
					int tmp = data[j];
					data[j] = data[j + 1];
					data[j + 1] = tmp;
				}
			}
		}
	}

}
