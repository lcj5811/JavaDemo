package com.lee.gis.compresstile;

/**
 * @ClassName com.lee.compresstile.Entry
 * @description
 * @author 凌霄
 * @data 2016年9月7日 下午3:32:30
 */
public class Entry {
	public static void main(String[] args) {
		String imagePath = "I:\\图形图像\\测试用图\\PC sharpen2.tif";
		String tileArrayPath = "I:\\图形图像\\测试用图\\test.ltt";
		RasterImageUtil instance = RasterImageUtil.getInstance();
		boolean res = instance.initRasterImage(imagePath);
		if (!res) {
			System.out.println("===初始化影像出错===");
			return;
		}
		int scaleBitmapWidth = 2560;
		int scaleBitmapHeight = 1280;
		instance.imageToTileArray(tileArrayPath, scaleBitmapWidth, scaleBitmapHeight);
	}
}
