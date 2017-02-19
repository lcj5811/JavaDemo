package com.lee.gis.splicemap;

import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class PJImage {
	static int[] imageArrayFirst;
	static int[] imageArraySecond;

	public static void main(String[] args) {
		xPic();
	}

	public static void xPic() {// 横向处理图片
		try {
			/* 1 读取第一张图片 */
			File fileOne = new File("F:/test2/0.png");
			BufferedImage imageFirst = ImageIO.read(fileOne);
			int width = imageFirst.getWidth();// 图片宽度
			int height = imageFirst.getHeight();// 图片高度
			imageArrayFirst = new int[width * height];// 从图片中读取RGB
			imageArrayFirst = imageFirst.getRGB(0, 0, width, height,
					imageArrayFirst, 0, width);

			/* 1 对第二张图片做相同的处理 */
			File fileTwo = new File("F:/test2/1.png");
			BufferedImage imageSecond = ImageIO.read(fileTwo);
			imageArraySecond = new int[width * height];
			imageArraySecond = imageSecond.getRGB(0, 0, width, height,
					imageArraySecond, 0, width);

			// 生成新图片
			BufferedImage imageResult = new BufferedImage(width * 2, height,
					BufferedImage.TYPE_INT_RGB);
			imageResult.setRGB(0, 0, width, height, imageArrayFirst, 0, width);// 设置左半部分的RGB
			imageResult.setRGB(width, 0, width, height, imageArraySecond, 0,
					width);// 设置右半部分的RGB
			File outFile = new File("F:/out.png");
			ImageIO.write(imageResult, "png", outFile);// 写图片
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void yPic() {// 纵向处理图片
		try {
			/* 1 读取第一张图片 */
			File fileOne = new File("D:\\1.GIF");
			BufferedImage imageFirst = ImageIO.read(fileOne);
			int width = imageFirst.getWidth();// 图片宽度
			int height = imageFirst.getHeight();// 图片高度
			int[] imageArrayFirst = new int[width * height];// 从图片中读取RGB
			imageArrayFirst = imageFirst.getRGB(0, 0, width, height,
					imageArrayFirst, 0, width);

			/* 1 对第二张图片做相同的处理 */
			File fileTwo = new File("D:\\1.GIF");
			BufferedImage imageSecond = ImageIO.read(fileTwo);
			int[] imageArraySecond = new int[width * height];
			imageArraySecond = imageSecond.getRGB(0, 0, width, height,
					imageArraySecond, 0, width);

			// 生成新图片
			BufferedImage imageResult = new BufferedImage(width, height * 2,
					BufferedImage.TYPE_INT_RGB);
			imageResult.setRGB(0, 0, width, height, imageArrayFirst, 0, width);// 设置左半部分的RGB
			imageResult.setRGB(0, height, width, height, imageArraySecond, 0,
					width);// 设置右半部分的RGB
			File outFile = new File("D:\\out.jpg");
			ImageIO.write(imageResult, "jpg", outFile);// 写图片
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
