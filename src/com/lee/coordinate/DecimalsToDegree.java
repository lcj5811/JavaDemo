package com.lee.coordinate;

/**
 * @Description 小数度数转换为度分秒
 * @Author 凌霄
 * @Date 2016-12-9 下午2:31:17
 */
public class DecimalsToDegree {

	public static void main(String[] args) {
		System.out.println(trandu2m(27.314246));
	}

	public static String trandu2m(double d) { // gisoracle 编号
		try {
			// double dd = Convert.ToDouble(str);

			String str = "" + d;

			int p = str.indexOf(".");
			int dt = Integer.parseInt(str.substring(0, p));
			d = d - dt;
			double M = d * 60;
			int mt = (int) M;
			M = (M - mt) * 60;
			if (Math.abs(M - 60) < 0.001) {
				M = 0;
				mt = mt + 1;

			}
			if (mt == 60) {
				dt = dt + 1;
				mt = 0;
			}

			return "" + dt + "°" + mt + "′" + M + "″";
		} catch (Exception e) {
			return e.getMessage();
		}

	}

	// 加载矢量图层
	// public static void initMapVectorDisplayLayer(String filePath, RectF
	// mRectF) {
	// shapeFileOperator = new ShapeFileOperator();
	// String shapeFilePath = ShapeFileOperator.isShapeFile(filePath);
	//
	// if (SystemUtil.getInstance().IsValid(shapeFilePath)) {
	// try {
	// boolean res = shapeFileOperator.init(shapeFilePath);
	// if (res) {
	// mPath = shapeFileOperator.queryWithinShape(mRectF, false);
	// mAreas = new ArrayList<Polyline>();
	// for (Path path : mPath) {
	// if (SystemUtil.getInstance().IsValid(path)) {
	// Polyline mArea = new Polyline(
	// ContextUtil.getInstance());
	//
	// List<GeoPoint> mPoints = new ArrayList<GeoPoint>();
	// for (FlaotPoint FP : getPoints(path)) {
	// mPoints.add(new GeoPoint(FP.getY(), FP.getX()));
	// }
	// mPoints.add(new GeoPoint(getPoints(path)[0].getY(),
	// getPoints(path)[0].getX()));
	// mArea.setPoints(mPoints);
	// mAreas.add(mArea);
	// mOverlayManager.add(mArea);
	// }
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// public static void initMapVectorDisplayLayer() {
	// Polyline mArea = new Polyline(ContextUtil.getInstance());
	// mAreas = new ArrayList<Polyline>();
	// List<GeoPoint> mPoints = new ArrayList<GeoPoint>();
	// mPoints.add(new GeoPoint(28.480, 113.781));
	// mPoints.add(new GeoPoint(28.481, 113.783));
	// mArea.setPoints(mPoints);
	// mAreas.add(mArea);
	// mOverlayManager.add(mArea);
	// }

}
