package com.lee.compresstile;

import java.awt.Color;

/**
 * 
 * @ClassName: ImageColorConvertor
 * @Description: 影像颜色的转换器
 * @author 苦丁茶
 * @date 2015年4月24日 下午4:31:06
 */
public class ImageColorConvertor {

	private static int[] colors = null;
	private static int lastSize = 0;

	/**
	 * 处理RGB彩色图
	 * 
	 * @param colorArray
	 * @param size
	 * @return int[]
	 */
	public static int[] dealRGBImage(int[] colorArray, int size) {
		if (size != lastSize) {
			colors = new int[size];
		}
		Color color = null;
		for (int kr = 0, kg = size, kb = size * 2; kr < size; ++kr, ++kg, ++kb) {
			color = new Color(colorArray[kr] % 256, colorArray[kg] % 256, colorArray[kb] % 256);
			colors[kr] = color.getRGB();
		}
		return colors;
	}

	/**
	 * 处理0-1图或灰度图
	 * 
	 * @param colorArray
	 * @param size
	 * @return int[]
	 */
	public static int[] dealGrayImage(int[] colorArray, int size) {
		if (size != lastSize) {
			colors = new int[size];
		}
		// 颜色值做规一化处理[0,1]
		int min = colorArray[0], max = colorArray[0];
		for (int i = 1; i < size; ++i) {
			if (min > colorArray[i]) {
				min = colorArray[i];
			} else if (max < colorArray[i]) {
				max = colorArray[i];
			}
		}
		max -= min;

		Color color = null;
		for (int i = 0; i < size; ++i) {
			colorArray[i] = (short) ((colorArray[i] - min) * 255 / max);
			color = new Color(colorArray[i], colorArray[i], colorArray[i]);
			colors[i] = color.getRGB();
		}
		return colors;
	}
}
