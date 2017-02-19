package com.lee.gis.coordinate;

/**
 * @ClassName com.lee.coordinate.tuoQiuJiChun
 * @description
 * @author 凌霄
 * @data 2016年4月27日 下午5:48:16
 */
public class TuoQiuJiChun {
	String coordinateName;
	long a;
	double b;

	public TuoQiuJiChun(String coordinateName, long a, double b) {
		this.a = a;
		this.b = b;
		this.coordinateName = coordinateName;
	}

	public double getFirstE() {
		System.out.println(Math.pow(Math.sqrt(a * a - b * b) / a, 2));
		return Math.pow(Math.sqrt(a * a - b * b) / a, 2);
	}

	public double getSecondE() {
		System.out.println(Math.pow(Math.sqrt(a * a - b * b) / b, 2));
		return Math.pow(Math.sqrt(a * a - b * b) / b, 2);
	}

	public double getM_Long() {
		return a;
	}

}
