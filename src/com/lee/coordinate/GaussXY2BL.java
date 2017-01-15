package com.lee.coordinate;

public class GaussXY2BL {

	public static int Beijing54 = 0;
	public static int Xian80 = 1;
	public static int CGCS2000 = 2;

	/**
	 * 选择坐标参数
	 * 
	 * @param coordinateName
	 * @return double[]
	 */
	public static double[] selectCoordinateParam(int coordinateName) {

		double[] output = new double[2];

		switch (coordinateName) {
		// Beijing54
		case 0:
			output[0] = 6378245.0;
			output[1] = 1.0 / 298.30;
			break;
		// Xian80
		case 1:
			output[0] = 6378140.0;
			output[1] = 1.0 / 298.257;
			break;
		// CGCS2000
		case 2:
			output[0] = 6378137.0;
			output[1] = 1.0 / 298.257222101;
			break;
		}

		return output;
	}

	/**
	 * 由高斯投影坐标反算成经纬度
	 * 
	 * @param X_longitude
	 * @param Y_latitude
	 * @return
	 */
	public static double[] GaussToBL(double X, double Y, int coordinateName, int mZoneWide) {

		// 带号
		int ProjNo;
		// 带宽
		int ZoneWide;

		double[] output = new double[2];
		double[] coordinateParam = selectCoordinateParam(coordinateName);
		double longitude1, latitude1, longitude0, X0, Y0, xval, yval;// latitude0,
		double e1, e2, ee, NN, T, C, M, D, R, u, fai, iPI;

		// 3.1415926535898/180.0;
		iPI = 0.0174532925199433;

		// 6度带宽
		ZoneWide = mZoneWide;
		// 查找带号
		ProjNo = (int) (X / 1000000L);

		// 中央经线
		if (ZoneWide == 3) {
			longitude0 = ProjNo * 3;
		} else {
			longitude0 = (ProjNo - 1) * ZoneWide + ZoneWide / 2;
		}
		// 中央经线转换为弧度
		longitude0 = longitude0 * iPI;

		// 平面坐标原点
		X0 = ProjNo * 1000000L + 500000L;
		Y0 = 0;

		// 带内大地坐标
		xval = X - X0;
		yval = Y - Y0;

		// e2--->第一偏心率的平方
		e2 = 2 * coordinateParam[1] - coordinateParam[1] * coordinateParam[1];
		e1 = (1.0 - Math.sqrt(1 - e2)) / (1.0 + Math.sqrt(1 - e2));
		// ee--->第二偏心率的平方
		ee = e2 / (1 - e2);

		// M = Y
		M = yval;
		u = M / (coordinateParam[0] * (1 - e2 / 4 - 3 * e2 * e2 / 64 - 5 * e2 * e2 * e2 / 256));
		fai = u + (3 * e1 / 2 - 27 * e1 * e1 * e1 / 32) * Math.sin(2 * u)
				+ (21 * e1 * e1 / 16 - 55 * e1 * e1 * e1 * e1 / 32) * Math.sin(4 * u)
				+ (151 * e1 * e1 * e1 / 96) * Math.sin(6 * u) + (1097 * e1 * e1 * e1 * e1 / 512) * Math.sin(8 * u);
		C = ee * Math.cos(fai) * Math.cos(fai);
		T = Math.tan(fai) * Math.tan(fai);

		NN = coordinateParam[0] / Math.sqrt(1.0 - e2 * Math.sin(fai) * Math.sin(fai));

		R = coordinateParam[0] * (1 - e2) / Math.sqrt((1 - e2 * Math.sin(fai) * Math.sin(fai))
				* (1 - e2 * Math.sin(fai) * Math.sin(fai)) * (1 - e2 * Math.sin(fai) * Math.sin(fai)));
		D = xval / NN;

		// 计算经度(Longitude) 纬度(Latitude)
		longitude1 = longitude0 + (D - (1 + 2 * T + C) * D * D * D / 6
				+ (5 - 2 * C + 28 * T - 3 * C * C + 8 * ee + 24 * T * T) * D * D * D * D * D / 120) / Math.cos(fai);
		latitude1 = fai
				- (NN * Math.tan(fai) / R) * (D * D / 2 - (5 + 3 * T + 10 * C - 4 * C * C - 9 * ee) * D * D * D * D / 24
						+ (61 + 90 * T + 298 * C + 45 * T * T - 256 * ee - 3 * C * C) * D * D * D * D * D * D / 720);

		// 转换为度 DD
		// longitude = longitude1 / iPI;
		// latitude = latitude1 / iPI;
		output[0] = longitude1 / iPI;
		output[1] = latitude1 / iPI;
		return output;
	}

	/**
	 * 由经纬度反算成高斯投影坐标
	 * 
	 * @param longitude
	 * @param latitude
	 */
	public static double[] BLToGauss(double longitude, double latitude, int coordinateName) {

		// 带号
		int ProjNo = 0;
		// 带宽
		int ZoneWide;

		double[] output = new double[2];
		double[] coordinateParam = selectCoordinateParam(coordinateName);
		double longitude1, latitude1, longitude0, X0, Y0, xval, yval;
		double e2, ee, NN, T, C, A, M, iPI;

		// 3.1415926535898/180.0;
		iPI = 0.0174532925199433;

		// 6度带宽
		ZoneWide = 6;

		ProjNo = (int) (longitude / ZoneWide);
		longitude0 = ProjNo * ZoneWide + ZoneWide / 2;
		longitude0 = longitude0 * iPI;
		// latitude0 = 0;
		// 经度转换为弧度
		longitude1 = longitude * iPI;
		// 纬度转换为弧度
		latitude1 = latitude * iPI;
		e2 = 2 * coordinateParam[1] - coordinateParam[1] * coordinateParam[1];
		ee = e2 * (1.0 - e2);
		NN = coordinateParam[0] / Math.sqrt(1.0 - e2 * Math.sin(latitude1) * Math.sin(latitude1));
		T = Math.tan(latitude1) * Math.tan(latitude1);
		C = ee * Math.cos(latitude1) * Math.cos(latitude1);
		A = (longitude1 - longitude0) * Math.cos(latitude1);

		M = coordinateParam[0] * ((1 - e2 / 4 - 3 * e2 * e2 / 64 - 5 * e2 * e2 * e2 / 256) * latitude1
				- (3 * e2 / 8 + 3 * e2 * e2 / 32 + 45 * e2 * e2 * e2 / 1024) * Math.sin(2 * latitude1)
				+ (15 * e2 * e2 / 256 + 45 * e2 * e2 * e2 / 1024) * Math.sin(4 * latitude1)
				- (35 * e2 * e2 * e2 / 3072) * Math.sin(6 * latitude1));
		xval = NN
				* (A + (1 - T + C) * A * A * A / 6 + (5 - 18 * T + T * T + 72 * C - 58 * ee) * A * A * A * A * A / 120);
		yval = M + NN * Math.tan(latitude1) * (A * A / 2 + (5 - T + 9 * C + 4 * C * C) * A * A * A * A / 24
				+ (61 - 58 * T + T * T + 600 * C - 330 * ee) * A * A * A * A * A * A / 720);
		X0 = 1000000L * (ProjNo + 1) + 500000L;
		Y0 = 0;
		xval = xval + X0;
		yval = yval + Y0;

		// *X = xval;
		// *Y = yval;
		output[0] = xval;
		output[1] = yval;
		return output;
	}

	/**
	 * 由经纬度反算成高斯投影坐标
	 * 
	 * @param longitude
	 * @param latitude
	 * @return
	 */
	public static double[] BLToGauss2(double longitude, double latitude, int coordinateName) {
		int ProjNo = 0;
		int ZoneWide; // //带宽

		double[] output = new double[2];
		double[] coordinateParam = selectCoordinateParam(coordinateName);
		double longitude1, latitude1, longitude0, X0, Y0, xval, yval;
		double e2, ee, NN, T, C, A, M, iPI;

		// 3.1415926535898/180.0;
		iPI = 0.0174532925199433;
		// 6度带宽
		ZoneWide = 6;

		ProjNo = (int) (longitude / ZoneWide);
		longitude0 = ProjNo * ZoneWide + ZoneWide / 2;
		longitude0 = longitude0 * iPI;
		longitude1 = longitude * iPI; // 经度转换为弧度
		latitude1 = latitude * iPI; // 纬度转换为弧度
		e2 = 2 * coordinateParam[1] - coordinateParam[1] * coordinateParam[1];
		ee = e2 / (1.0 - e2);
		NN = coordinateParam[0] / Math.sqrt(1.0 - e2 * Math.sin(latitude1) * Math.sin(latitude1));
		T = Math.tan(latitude1) * Math.tan(latitude1);
		C = ee * Math.cos(latitude1) * Math.cos(latitude1);
		A = (longitude1 - longitude0) * Math.cos(latitude1);
		M = coordinateParam[0] * ((1 - e2 / 4 - 3 * e2 * e2 / 64 - 5 * e2 * e2 * e2 / 256) * latitude1
				- (3 * e2 / 8 + 3 * e2 * e2 / 32 + 45 * e2 * e2 * e2 / 1024) * Math.sin(2 * latitude1)
				+ (15 * e2 * e2 / 256 + 45 * e2 * e2 * e2 / 1024) * Math.sin(4 * latitude1)
				- (35 * e2 * e2 * e2 / 3072) * Math.sin(6 * latitude1));
		// 因为是以赤道为Y轴的，与我们南北为Y轴是相反的，所以xy与高斯投影的标准xy正好相反;
		xval = NN
				* (A + (1 - T + C) * A * A * A / 6 + (5 - 18 * T + T * T + 14 * C - 58 * ee) * A * A * A * A * A / 120);
		yval = M + NN * Math.tan(latitude1) * (A * A / 2 + (5 - T + 9 * C + 4 * C * C) * A * A * A * A / 24
				+ (61 - 58 * T + T * T + 270 * C - 330 * ee) * A * A * A * A * A * A / 720);
		X0 = 1000000L * (ProjNo + 1) + 500000L;
		Y0 = 0;
		xval = xval + X0;
		yval = yval + Y0;
		output[0] = xval;
		output[1] = yval;
		return output;
	}

	public static void Beijing54ToWgs84(double Lat, double Lon) {
		double[] coordinateParam = selectCoordinateParam(GaussXY2BL.Beijing54);
		double X, Y, Z, N, e2, B, L, H, O, iPI;
		iPI = 0.0174532925199433;

		Lat *= iPI;
		Lon *= iPI;

		TuoQiuJiChun tuoQiuJiChun = new TuoQiuJiChun("WGS-84坐标系", 6378137, 6356752.3142);

		e2 = 2 * coordinateParam[1] - coordinateParam[1] * coordinateParam[1];

		N = coordinateParam[0] / Math.sqrt(1 - e2 * Math.pow(Math.sin(Lat), 2));

		X = (N) * Math.cos(Lat) * Math.cos(Lon) + 31.4;
		Y = (N) * Math.cos(Lat) * Math.sin(Lon) - 144.3;
		Z = (N * (1 - e2)) * Math.sin(Lat) - 74.8;
		System.err.println(X + "," + Y + "," + Z);

		// ------------------

		O = Math.atan(tuoQiuJiChun.getM_Long() * Z / (6356752.3142 * Math.sqrt(X * X + Y * Y)));

		B = Math.atan((Z + 6356752.3142 * tuoQiuJiChun.getSecondE() * Math.pow(Math.sin(O), 3))
				/ (Math.sqrt(X * X + Y * Y) - tuoQiuJiChun.getM_Long() * e2 * Math.cos(O)));
		L = Math.atan(Y / X);
		H = (Math.sqrt(X * X + Y * Y) / Math.cos(B)) - N;
		B = B / iPI;
		L = L / iPI;
		System.err.println(B + "," + L + "," + H);

	}

	public static void main(String[] args) {

		// lonLat2Mercatro(27.3142754928356, 113.79296366506799);

		// 19776507.566, 3025599.095
		// double BL[] = GaussToBL(38479507.695, 3022524.674,
		// GaussXY2BL.Beijing54, 3);
		// for (double a : BL) {
		// System.err.println(a);
		// }
		// 113.793596 27.314217
		// Beijing54ToWgs84(27.3142754928356, 113.79296366506799);

		// BLToGauss(longitude, latitude);
		// double XY[] = BLToGauss(113.79292586738798, 27.314272816111806,
		// GaussXY2BL.Xian80);
		// for (double b : XY) {
		// System.out.println(b);
		// }

		// GaussToBLToGauss(longitude, latitude)
		// double XY1[] = GaussToBLToGauss(113.79292586738798,
		// 27.314272816111806, GaussXY2BL.Xian80);
		// for (double c : XY1) {
		// System.out.println(c);
		// }

	}
}
