package com.lee.dealfile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import mediautil.gen.Rational;
import mediautil.image.jpeg.Entry;
import mediautil.image.jpeg.Exif;
import mediautil.image.jpeg.LLJTran;
import mediautil.image.jpeg.LLJTranException;

/**
 * @ClassName com.lee.dealfile.DealPhotoInfo
 * @description 处理照片地理位置等信息
 * @author 凌霄
 * @data 2016年8月18日 上午10:31:15
 */

public class DealPhotoInfo {
	public static void main(String[] args) throws Exception {
		// readPhotoInfo("D:\\20160728_131007.jpg");
		writePhotoInfo("D:\\20160728_131007.jpg");
		// DuFenMiaoToDu("119°34′7.91″");
		// DuFenMiaoToDu("27°40′7.08″");
		// DuToDuFenMiao(130.03145632);
	}

	/**
	 * 读取照片里面的信息
	 * 
	 * @param file
	 * @throws ImageProcessingException
	 * @throws Exception
	 */
	public static void readPhotoInfo(String FilePath) throws ImageProcessingException, Exception {
		File mFile = new File(FilePath);
		Metadata metadata = ImageMetadataReader.readMetadata(mFile);
		for (Directory directory : metadata.getDirectories()) {
			for (Tag tag : directory.getTags()) {
				String tagName = tag.getTagName(); // 标签名
				String desc = tag.getDescription(); // 标签信息
				// if (tagName.equals("Image Height")) {
				// System.out.println("图片高度: " + desc);
				// } else if (tagName.equals("Image Width")) {
				// System.out.println("图片宽度: " + desc);
				// } else if (tagName.equals("Date/Time Original")) {
				// System.out.println("拍摄时间: " + desc);
				// } else if (tagName.equals("GPS Latitude")) {
				// System.err.println("纬度 : " + desc);
				// // System.err.println("纬度(度分秒格式) : "+pointToLatlong(desc));
				// } else if (tagName.equals("GPS Longitude")) {
				// System.err.println("经度: " + desc);
				// // System.err.println("经度(度分秒格式): "+pointToLatlong(desc));
				// }
				System.out.println(tagName + "---------" + desc);
			}
		}
	}

	/**
	 * 将照片中的信息进行重写
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void writePhotoInfo(String InputFilePath) throws Exception {
		// 原文件
		InputStream fip = new BufferedInputStream(new FileInputStream(InputFilePath)); // No
																						// buffer
		LLJTran llj = new LLJTran(fip);
		try {
			llj.read(LLJTran.READ_INFO, true);
		} catch (LLJTranException e) {
			e.printStackTrace();
		}
		Exif exif = (Exif) llj.getImageInfo();

		/* Set some values directly to gps IFD */

		Entry e;
		// Set Latitude
		e = new Entry(Exif.ASCII);
		e.setValue(0, 'N');
		exif.setTagValue(Exif.GPSLatitudeRef, -1, e, true);
		// 设置具体的纬度
		e = new Entry(Exif.RATIONAL);
		e.setValue(0, new Rational(28, 1));
		e.setValue(1, new Rational(21, 1));
		e.setValue(2, new Rational(323, 1));
		exif.setTagValue(Exif.GPSLatitude, -1, e, true);

		// Set Longitude
		e = new Entry(Exif.ASCII);
		e.setValue(0, 'E');
		exif.setTagValue(Exif.GPSLongitudeRef, -1, e, true);

		// 设置具体的经度
		e = new Entry(Exif.RATIONAL);
		e.setValue(0, new Rational(121, 1));
		e.setValue(1, new Rational(58, 1));
		e.setValue(2, new Rational(531, 1));
		exif.setTagValue(Exif.GPSLongitude, -1, e, true);

		llj.refreshAppx(); // Recreate Marker Data for changes done
		// 改写后的文件，文件必须存在
		OutputStream out = new BufferedOutputStream(new FileOutputStream("D:\\20160728_1310071.jpg"));
		// Transfer remaining of image to output with new header.
		llj.xferInfo(null, out, LLJTran.REPLACE, LLJTran.REPLACE);
		fip.close();
		out.close();
		llj.freeMemory();
	}

	/**
	 * 度分秒 转 十进制度
	 * 
	 * @param point
	 *            度分秒坐标
	 * @return
	 */
	public static String DuFenMiaoToDu(String point) {
		Double du = Double.parseDouble(point.substring(0, point.indexOf("°")).trim());
		Double fen = Double.parseDouble(point.substring(point.indexOf("°") + 1, point.indexOf("′")).trim());
		Double miao = Double.parseDouble(point.substring(point.indexOf("′") + 1, point.indexOf("″")).trim());
		Double duStr = du + fen / 60 + miao / 60 / 60;
		System.out.println(new DecimalFormat("#.000000").format(duStr));
		return duStr.toString();
	}

	/**
	 * 十进制度 转 度分秒
	 * 
	 * @param d
	 *            十进制度坐标
	 * @return
	 */
	public static int[] DuToDuFenMiao(double d) {
		try {
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
			System.out.println(dt + "°" + mt + "′" + new DecimalFormat("#.00").format(M) + "″");
			// return dt + "°" + mt + "′" + new DecimalFormat("#.00").format(M)
			// + "″";
			return new int[] { dt, mt, (int) M };
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}

	}
}
