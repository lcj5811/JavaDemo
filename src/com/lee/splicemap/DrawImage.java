package com.lee.splicemap;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

public class DrawImage {
	/** *Java拼接多张图片 * *@parampics *@paramtype *@paramdst_pic *@return */
	/*
	 * public static boolean merge1(String[] pics,String type,String dst_pic){
	 * int intlen = pics.length; if(intlen<1){ System.out.println("picslen<1");
	 * return false; } }
	 */
	public static void main(String[] args) {
		doing();
	}

	private static void doing() {
		int n = 10000;
		String[] test = null;
		// for (String a : findDir(new File("F:/test/谷歌卫星图/20"))) {
		// ArrayList<String> pic = new ArrayList<String>();
		// test = findFile(new File(a), pic).toArray(new String[] {});
		// merge_y(test, "jpg", "F:/test2/" + n++ + ".jpg");
		// }
		test = findFile(new File("F:/test2"), new ArrayList<String>()).toArray(
				new String[] {});
		merge_x(test, "jpg", "F:/test2/out.jpg");
	}

	public static ArrayList<String> findFile(File file, ArrayList<String> pic) {
		if (file.isDirectory()) { // 否则如果它是一个目录
			File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
			for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
				findFile(files[i], pic); // 把每个文件 用这个方法进行迭代
			}
		} else if (file.exists()) {
			pic.add(file.getPath());
		}
		return pic;
	}

	public static ArrayList<String> findDir(File file) {
		ArrayList<String> name = new ArrayList<String>();
		if (file.isDirectory()) { // 否则如果它是一个目录
			File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
			for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
				name.add(files[i].getPath());
			}
		}
		return name;
	}

	/**
	 * Java拼接多张图片
	 * 
	 * @param pics
	 * @param type
	 * @param dst_pic
	 * @return
	 */
	public static boolean merge_y(String[] pics, String type, String dst_pic) {

		int len = pics.length;
		// 检测路径是否为空
		if (len < 1) {
			System.out.println("pics len < 1");
			return false;
		}

		File[] src = new File[len];
		BufferedImage[] images = new BufferedImage[len];
		int[][] ImageArrays = new int[len][];
		for (int i = 0; i < len; i++) {
			try {
				src[i] = new File(pics[i]);
				images[i] = ImageIO.read(src[i]);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			int width = images[i].getWidth();
			int height = images[i].getHeight();
			ImageArrays[i] = new int[width * height];// 从图片中读取RGB
			ImageArrays[i] = images[i].getRGB(0, 0, width, height,
					ImageArrays[i], 0, width);
		}

		int dst_height = 0;
		int dst_width = images[0].getWidth();
		for (int i = 0; i < images.length; i++) {
			dst_width = dst_width > images[i].getWidth() ? dst_width
					: images[i].getWidth();

			dst_height += images[i].getHeight();
		}
		// System.out.println(dst_width);
		// System.out.println(dst_height);
		if (dst_height < 1) {
			System.out.println("dst_height < 1");
			return false;
		}

		// 生成新图片
		try {
			// dst_width = images[0].getWidth();
			BufferedImage ImageNew = new BufferedImage(dst_width, dst_height,
					BufferedImage.TYPE_INT_RGB);
			int height_i = 0;
			for (int i = 0; i < images.length; i++) {
				ImageNew.setRGB(0, height_i, dst_width, images[i].getHeight(),
						ImageArrays[i], 0, dst_width);
				height_i += images[i].getHeight();
			}

			File outFile = new File(dst_pic);
			ImageIO.write(ImageNew, type, outFile);// 写图片
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean merge_x(String[] pics, String type, String dst_pic) {

		int len = pics.length;
		int dst_width = 0;
		int dst_height = 0;
		// 检测路径是否为空
		if (len < 1) {
			System.out.println("pics len < 1");
			return false;
		}

		File[] src = new File[len];
		BufferedImage[] images = new BufferedImage[len];
		int[][] ImageArrays = new int[len][];
		for (int i = 0; i < len; i++) {
			try {
				src[i] = new File(pics[i]);
				images[0] = ImageIO.read(src[i]);
				System.out.println(pics[i]);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

			if (dst_height == 0) {
				dst_height = images[0].getHeight();
				for (int a = 0; a < len; a++) {
					dst_width += 256;
				}
				System.out.println(dst_width + "," + dst_height);
			}
			BufferedImage ImageNew = new BufferedImage(dst_width, dst_height,
					BufferedImage.TYPE_INT_RGB);
			int width = images[0].getWidth();
			int height = images[0].getHeight();
			ImageArrays[0] = new int[width * height];// 从图片中读取RGB
			ImageArrays[0] = images[0].getRGB(0, 0, width, height,
					ImageArrays[0], 0, width);
		}
		

		// 生成新图片
		try {
			BufferedImage ImageNew = new BufferedImage(dst_width, dst_height,
					BufferedImage.TYPE_INT_RGB);
			int width_i = 0;
			for (int i = 0; i < images.length; i++) {
				ImageNew.setRGB(width_i, 0, 256, dst_height, ImageArrays[i], 0,
						256);
				width_i += images[i].getWidth();
			}

			File outFile = new File(dst_pic);
			ImageIO.write(ImageNew, type, outFile);// 写图片
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
}
