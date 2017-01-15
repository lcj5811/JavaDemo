package com.lee.compresstile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

/**
 * 
 * @ClassName: FileOperator
 * @Description: 文件操作器
 * @author 苦丁茶
 * @date 2015年4月24日 下午3:19:29
 */
public class FileOperator {
	/**
	 * 创建文件
	 * 
	 * @param filePath
	 * @param deleteExist
	 *            void
	 */
	public static boolean createFile(String filePath, boolean deleteExist) {
		File file = new File(filePath);
		try {
			File parentFile = file.getParentFile();
			if (parentFile != null && !parentFile.exists()) {
				parentFile.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
				return false;
			} else if (deleteExist) {
				file.delete();
				file.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 写一个长整型数据到指定的文件流中
	 * 
	 * @param out
	 * @param value
	 * @throws IOException
	 *             void
	 */
	public static void writeLongToFile(OutputStream out, long value) throws IOException {
		byte[] byteArray = DataTypeConvertor.longToByteArray(value);
		writeByteArraytoFile(out, byteArray);
	}

	/**
	 * 写一个双精度型数据到指定的文件流中
	 * 
	 * @param out
	 * @param value
	 * @throws IOException
	 *             void
	 */
	public static void writeDoubleToFile(OutputStream out, double value) throws IOException {
		byte[] byteArray = DataTypeConvertor.doubleToByteArray(value);
		writeByteArraytoFile(out, byteArray);
	}

	/**
	 * 写一个整型数据到指定的文件流中
	 * 
	 * @param out
	 * @param value
	 * @throws IOException
	 *             void
	 */
	public static void writeIntToFile(OutputStream out, int value) throws IOException {
		byte[] byteArray = DataTypeConvertor.intToByteArray(value);
		writeByteArraytoFile(out, byteArray);
	}

	/**
	 * 写一个字节数组到输出流中
	 * 
	 * @param out
	 * @param byteArray
	 * @throws IOException
	 *             void
	 */
	public static void writeByteArraytoFile(OutputStream out, byte[] byteArray) throws IOException {
		out.write(byteArray);
		out.flush();
		byteArray = null;
	}

	/**
	 * 写一个文件结束标志符到文件流中
	 * 
	 * @param out
	 * @throws IOException
	 *             void
	 */
	public static void writeEOFToFile(OutputStream out) throws IOException {
		byte[] byteArray = new byte[] { 31, 3, -108, 80 };
		writeByteArraytoFile(out, byteArray);
	}

	public static void copyShapeArray(String aimShpPath) {
		String path = aimShpPath.substring(0, aimShpPath.lastIndexOf("."));
		copyFile(SystemConst.assetsFolder + "ex.shp", path + SystemConst.shpShapeSuffix);
		copyFile(SystemConst.assetsFolder + "ex.shx", path + SystemConst.shxShapeSuffix);
		copyFile(SystemConst.assetsFolder + "ex.dbf", path + SystemConst.dbfShapeSuffix);
	}

	public static boolean copyFile(String srcPath, String aimPath) {
		File file = new File(srcPath);
		if (!file.exists()) {
			return false;
		}

		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			createFile(aimPath, true);

			fis = new FileInputStream(srcPath);
			fos = new FileOutputStream(aimPath);
			in = fis.getChannel();// 得到对应的文件通道
			out = fos.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
			fos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (fis != null) {
					fis.close();
				}
				if (out != null) {
					out.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

}
