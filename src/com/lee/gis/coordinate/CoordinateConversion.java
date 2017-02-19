package com.lee.gis.coordinate;

/**
 * @ClassName com.lee.coordinate.CoordinateConversion
 * @description
 * @author 凌霄
 * @data 2016年4月27日 下午4:47:32
 */
public class CoordinateConversion {
	/**
	 * @Title: GeodeticForGauss
	 * @Description: 空间大地坐标转换高斯投影平面直角坐标
	 * @param: @param
	 *             B 纬度
	 * @param: @param
	 *             L 经度
	 * @param: @param
	 *             type 几度分带
	 * @param: @param
	 *             Num 目标坐标系
	 * @return: void
	 */

	public static void GeodeticForGauss(double B, double L, int type, int Num) {

		TuoQiuJiChun tuoQiuJiChun = null;

		switch (Num) {
		case 84:
			// WGS-84坐标系
			tuoQiuJiChun = new TuoQiuJiChun("WGS-84坐标系", 6378137, 6356752.3142);
			break;
		case 80:
			// 西安-80坐标系
			tuoQiuJiChun = new TuoQiuJiChun("西安-80坐标系", 6378140, 6356755.2881575287);
			break;
		case 54:
			// 北京-54坐标系
			tuoQiuJiChun = new TuoQiuJiChun("北京-54坐标系", 6378245, 6356863.0187730473);
			break;
		}

		// 带号
		double beltNum;
		beltNum = Math.ceil((L - (type == 3 ? 1.5 : 0)) / type);

		if (type == 3 && beltNum * 3 == L - 1.5) {
			beltNum += 1;
		}

		double L0 = L - (beltNum * type - (type == 6 ? 3 : 0)); // 中央经线的度数
		System.out.println(L0);
		double rB, tB, m;

		rB = B * Math.PI / 180;
		tB = Math.tan(rB);
		m = Math.cos(rB) * L0 * Math.PI / 180;

		double N = tuoQiuJiChun.getM_Long() / Math.sqrt(1 - tuoQiuJiChun.getFirstE() * Math.sin(rB) * Math.sin(rB));

		double it2 = tuoQiuJiChun.getSecondE() * Math.pow(Math.cos(rB), 2);

		double x = 0.5 * m * m + (double) 1 / 24 * (5 - tB * tB + 9 * it2 + 4 * it2 * it2) * Math.pow(m, 4)
				+ (double) 1 / 720 * (61 - 58 * tB * tB + Math.pow(tB, 4)) * Math.pow(m, 6);

		double m0 = tuoQiuJiChun.getM_Long() * (1 - tuoQiuJiChun.getFirstE());
		double m2 = (double) 3 / 2 * tuoQiuJiChun.getFirstE() * m0;
		double m4 = (double) 5 / 4 * tuoQiuJiChun.getFirstE() * m2;
		double m6 = (double) 7 / 6 * tuoQiuJiChun.getFirstE() * m4;
		double m8 = (double) 9 / 8 * tuoQiuJiChun.getFirstE() * m6;
		double a0, a2, a4, a6, a8;
		a0 = m0 + (double) 1 / 2 * m2 + (double) 3 / 8 * m4 + (double) 5 / 16 * m6 + (double) 35 / 128 * m8;
		a2 = (double) 1 / 2 * m2 + (double) 1 / 2 * m4 + (double) 15 / 32 * m6 + (double) 7 / 16 * m8;
		a4 = (double) 1 / 8 * m4 + (double) 3 / 16 * m6 + (double) 7 / 32 * m8;
		a6 = (double) 1 / 32 * m6 + (double) 1 / 16 * m8;
		a8 = (double) 1 / 128 * m8;
		// 求子午线弧长
		double X1 = a0 * rB - a2 * Math.sin(2 * rB) * 0.5 + (double) 1 / 4 * a4 * Math.sin(4 * rB)
				- (double) 1 / 6 * a6 * Math.sin(6 * rB) + (double) 1 / 8 * a8 * Math.sin(8 * rB);

		double X = X1 + N * x * tB;
		double Y = N
				* (m + (double) 1 / 6 * (1 - tB * tB + it2) * Math.pow(m, 3) + (double) 1 / 720
						* (5 - 18 * tB * tB + Math.pow(tB, 4) + 14 * it2 - 58 * tB * tB * it2) * Math.pow(m, 5))
				+ 500000;
		System.err.println(X + "," + Y);
	}

	/**
	 * @Title: GaussForGeodetic
	 * @Description: 高斯投影平面直角坐标转换空间大地坐标
	 * @param: @param
	 *             X
	 * @param: @param
	 *             Y
	 * @param: @param
	 *             type 几度分带
	 * @param: @param
	 *             Num 目标坐标系
	 * @param: @param
	 *             beltNum 带号
	 * @return: void
	 */
	public static void GaussForGeodetic(double X, double Y, int type, int Num, double beltNum) {

		double Y1 = Y - 500000;

		TuoQiuJiChun tuoQiuJiChun = null;

		switch (Num) {
		case 84:
			// WGS-84坐标系
			tuoQiuJiChun = new TuoQiuJiChun("WGS-84坐标系", 6378137, 6356752.3142);
			break;
		case 80:
			// 西安-80坐标系
			tuoQiuJiChun = new TuoQiuJiChun("西安-80坐标系", 6378140, 6356755.2881575287);
			break;
		case 54:
			// 北京-54坐标系
			tuoQiuJiChun = new TuoQiuJiChun("北京-54坐标系", 6378245, 6356863.0187730473);
			break;
		}

		if (Y1 > 1000000) {
			int beltnum = (int) Math.ceil(Y1 / 1000000) - 1;
			Y1 -= beltnum * 1000000 + 500000;
		}

		double m0 = tuoQiuJiChun.getM_Long() * (1 - tuoQiuJiChun.getFirstE());
		double m2 = (double) 3 / 2 * tuoQiuJiChun.getFirstE() * m0;
		double m4 = (double) 5 / 4 * tuoQiuJiChun.getFirstE() * m2;
		double m6 = (double) 7 / 6 * tuoQiuJiChun.getFirstE() * m4;
		double m8 = (double) 9 / 8 * tuoQiuJiChun.getFirstE() * m6;
		double a0, a2, a4, a6, a8;
		a0 = m0 + 1.0 / 2.0 * m2 + 3.0 / 8.0 * m4 + 5.0 / 16.0 * m6 + 35.0 / 128.0 * m8;
		a2 = 1.0 / 2.0 * m2 + 1.0 / 2.0 * m4 + 15.0 / 32.0 * m6 + 7.0 / 16.0 * m8;
		a4 = 1.0 / 8.0 * m4 + 3.0 / 16.0 * m6 + 7.0 / 32.0 * m8;
		a6 = 1.0 / 32.0 * m6 + 1.0 / 16.0 * m8;
		a8 = 1.0 / 128.0 * m8;
		double B1;
		double Bf = X / a0;
		do {
			B1 = Bf;
			Bf = (X + 1.0 / 2.0 * a2 * Math.sin(2 * B1) - 1.0 / 4.0 * a4 * Math.sin(4 * B1)
					+ 1.0 / 6.0 * a6 * Math.sin(6 * B1)) / a0;

		} while (Math.abs(B1 - Bf) > 0.00000000001);
		double tf = Math.tan(Bf);
		double it2 = tuoQiuJiChun.getSecondE() * Math.cos(Bf);
		double Nf = tuoQiuJiChun.getM_Long() / Math.sqrt(1 - tuoQiuJiChun.getFirstE() * Math.sin(Bf) * Math.sin(Bf));
		double Mf = Nf / (1 + tuoQiuJiChun.getSecondE() * Math.cos(Bf) * Math.cos(Bf));

		double B1f = Bf - (1.0 / 2.0) * tf * Math.pow(Y1, 2) / (Mf * Nf * Math.cos(Bf))
				+ (1.0 / 24.0) * tf * (5 + 3 * tf * tf + it2 - 9 * it2 * tf * tf) * Math.pow(Y1, 4)
						/ (Mf * Math.pow(Nf, 3))
				- (1.0 / 720.0) * tf * (61 + 90 * tf * tf + 45 * tf * tf * tf * tf) * Math.pow(Y1, 6)
						/ (Mf * Math.pow(Nf, 5));
		double l = Y1 / (Nf * Math.cos(Bf))
				- (1.0 / 6.0) * (1 + 2 * tf * tf + it2) * Math.pow(Y1, 3) / (Math.pow(Nf, 3) * Math.cos(Bf))
				+ (1.0 / 120.0) * (5 + 28 * tf * tf + 24 * tf * tf * tf * tf + 6 * it2 + 8 * it2 * tf * tf)
						* Math.pow(Y1, 5) / (Math.pow(Nf, 5) * Math.cos(Bf));
		l = l * 180 / Math.PI;
		double L = l + type * beltNum - ((type == 6) ? 3 : 0);
		double B = B1f * 180 / Math.PI;
		System.err.println(B + "," + L);
	}

	public static void main(String[] args) {
		// System.err.println(Math.ceil(19776507.566 / 1000000));
		// 38479507.695
		GaussForGeodetic(3025597.775, 776510.617, 6, 54, 19);
	}

}