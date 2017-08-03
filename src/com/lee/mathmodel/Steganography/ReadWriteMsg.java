package com.lee.mathmodel.Steganography;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * @ClassName com.lee.mathmodel.Steganography.ReadMsg
 * @description 隐写术，读取信息
 * @author 凌霄
 * @data 2017年6月8日 下午2:47:46
 */
public class ReadWriteMsg {

	public static void main(String[] args) throws IOException {
		// String inputFile = "C:\\Users\\Lee\\Desktop\\test.png";
		// String outputFile = "C:\\Users\\Lee\\Desktop\\test2.png";
		String inputFile = "C:\\Users\\Lee\\Desktop\\R0256.png";
		String outputFile = "C:\\Users\\Lee\\Desktop\\R0256_t.png";
		String endName = "png";
		new ReadWriteMsg().readMsgLoc(inputFile, outputFile, endName);
		// new ReadWriteMsg().readMsgLoc(inputFile, outputFile, endName);
	}

	/**
	 * 读取信息
	 * 
	 * @param inputFile
	 * @param outputFile
	 * @param endName
	 * @throws IOException
	 */
	public void readMsg(String inputFile, String outputFile, String endName) throws IOException {
		BufferedImage im = ImageIO.read(new File(inputFile));// 读取图片
		int width = im.getWidth();
		int height = im.getHeight();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int color = im.getRGB(i, j);
				int r = (color >>> 16) & 255;
				int g = (color >>> 8) & 255;
				int b = color & 255;
				im.setRGB(i, j, 0);
				if (b % 2 == 1) {
					System.out.println(i + "," + j + "," + g);
					im.setRGB(i, j, 0x00FF00);
				}
			}
		}
		File outputfile = new File(outputFile);
		ImageIO.write(im, endName, outputfile);
	}

	/**
	 * 读取写入位置
	 * 
	 * @param inputFile
	 * @param outputFile
	 * @param endName
	 * @throws IOException
	 */
	public ArrayList<String> readMsgLoc(String inputFile, String outputFile, String endName) throws IOException {
		ArrayList<String> arrayList = new ArrayList<>();
		BufferedImage im = ImageIO.read(new File(inputFile));// 读取图片
		int width = im.getWidth();
		int height = im.getHeight();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int color = im.getRGB(i, j);
				int r = (color >>> 16) & 255;
				int g = (color >>> 8) & 255;
				int b = color & 255;
				if (g < 220 && g > 180) {
					arrayList.add(i + "," + j);
					// im.setRGB(i, j, new Color(0, 255, 0).getRGB());
				} else {
					// im.setRGB(i, j, 0);
				}
			}
		}
		// File outputfile = new File(outputFile);
		// ImageIO.write(im, endName, outputfile);
		return arrayList;
	}

	public void writeMsg() {

	}

}