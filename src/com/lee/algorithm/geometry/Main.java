package com.lee.algorithm.geometry;

import java.text.DecimalFormat;
import java.util.Scanner;

/**
 * @ClassName com.lee.mathmodel.geometry.Main
 * @description 求两个圆形相交面积
 * @author 凌霄
 * @data 2017年11月14日 下午4:15:00
 */
public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while (sc.hasNext()) {
			double PI = Math.acos(-1.0);
			double x1 = sc.nextDouble(); //
			double y1 = sc.nextDouble(); //
			double r1 = sc.nextDouble();

			double x2 = sc.nextDouble();
			double y2 = sc.nextDouble();
			double r2 = sc.nextDouble();
			double alpha, area;
			double d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));// 两圆心距离

			if (d >= r1 + r2 || r1 == 0 || r2 == 0) {// 相切、相离、考虑等于0
				area = 0;
			} else if (d + r1 <= r2) {// 包含
				area = PI * r1 * r1;
			} else if (d + r2 <= r1) {// 包含
				area = PI * r2 * r2;
			} else {
				alpha = Math.acos((d * d + r1 * r1 - r2 * r2) / (2 * d * r1));// 余弦定理取得相交弧所对本圆的圆心角
				area = alpha * r1 * r1;// 本圆扇形面积
				alpha = Math.acos((d * d + r2 * r2 - r1 * r1) / (2 * d * r2));// 余弦定理取得相交弧所对另一圆的圆心角
				area += alpha * r2 * r2;// 另一圆的扇形面积
				double s = (d + r1 + r2) / 2;// 海伦公式之s
				area -= Math.sqrt(s * (s - d) * (s - r1) * (s - r2)) * 2;// 两扇形面积减去两三角形面积即为交集
			}
			DecimalFormat df = new DecimalFormat("0.000");
			System.out.println(df.format(area));
		}
	}
}
