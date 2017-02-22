package com.lee.gis.downloadmap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @ClassName com.lee.gis.downloadmap.CompressTileMap
 * @description 将瓦片压缩为ZIP
 * @author 凌霄
 * @data 2017年2月20日 下午3:53:36
 */
public class CompressTileMap {
	/**
	 * zip压缩功能测试. 将d:\\temp\\zipout目录下的所有文件连同子目录压缩到d:\\temp\\out.zip.
	 * 
	 * @param baseDir
	 *            所要压缩的目录名（包含绝对路径）
	 * @param objFileName
	 *            压缩后的文件名
	 * @throws Exception
	 */

	public static void main(String[] args) throws Exception {
		createZip("F:/test2/谷歌卫星图/", "F:/test2/JZ_GM20150907_2213.zip");
		// createZip("F:/OSM地形图", "F:/20150628Cycle.zip");
		// releaseZipToFile("F:/t/8888.zip", "F:/t");
	}

	public static void createZip(String baseDir, String objFileName) throws Exception {
		File folderObject = new File(baseDir);
		if (folderObject.exists()) {
			List<File> fileList = getSubFiles(new File(baseDir));
			// 压缩文件名
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(objFileName));
			ZipEntry ze = null;
			byte[] buf = new byte[1024];
			int readLen = 0;
			for (int i = 0; i < fileList.size(); i++) {
				File f = (File) fileList.get(i);
				// System.out.println("Adding: " + f.getPath());
				// 创建一个ZipEntry，并设置Name和其它的一些属性
				ze = new ZipEntry(getAbsFileName(baseDir, f));
				ze.setSize(f.length());
				ze.setTime(f.lastModified());
				// 将ZipEntry加到zos中，再写入实际的文件内容
				zos.putNextEntry(ze);
				InputStream is = new BufferedInputStream(new FileInputStream(f));
				while ((readLen = is.read(buf, 0, 1024)) != -1) {
					zos.write(buf, 0, readLen);
				}
				is.close();
			}
			zos.close();
			System.out.println("压缩完成");
		} else {
			throw new Exception("this folder isnot exist!");
		}
	}

	private static List<File> getSubFiles(File baseDir) {
		List<File> ret = new ArrayList<File>();
		// File base=new File(baseDir);
		File[] tmp = baseDir.listFiles();
		for (int i = 0; i < tmp.length; i++) {
			if (tmp[i].isFile()) {
				ret.add(tmp[i]);
				// System.out.println(tmp[i]);
			}
			if (tmp[i].isDirectory()) {
				ret.addAll(getSubFiles(tmp[i]));
				// System.out.println(tmp[i]);
			}
		}
		return ret;
	}

	private static String getAbsFileName(String baseDir, File realFileName) {
		File real = realFileName;
		File base = new File(baseDir).getParentFile();
		String ret = real.getName();
		while (true) {
			real = real.getParentFile();
			if (real == null)
				break;
			if (real.equals(base))
				break;
			else {
				ret = real.getName() + "/" + ret;
			}
		}
		// System.out.println("add:"+ret);
		return ret;
	}

	/**
	 * 测试解压缩功能. 将d:\\download\\test.zip连同子目录解压到d:\\temp\\zipout目录下.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static void releaseZipToFile(String sourceZip, String outFileName) throws IOException {
		ZipFile zfile = new ZipFile(sourceZip);
		Enumeration zList = zfile.entries();
		ZipEntry ze = null;
		byte[] buf = new byte[1024];
		while (zList.hasMoreElements()) {
			// 从ZipFile中得到一个ZipEntry
			ze = (ZipEntry) zList.nextElement();
			if (ze.isDirectory()) {
				continue;
			}
			// 以ZipEntry为参数得到一个InputStream，并写到OutputStream中
			OutputStream os = new BufferedOutputStream(
					new FileOutputStream(getRealFileName(outFileName, ze.getName())));
			InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
			int readLen = 0;
			while ((readLen = is.read(buf, 0, 1024)) != -1) {
				os.write(buf, 0, readLen);
			}
			is.close();
			os.close();
		}
		zfile.close();
	}

	/**
	 * 给定根目录，返回一个相对路径所对应的实际文件名.
	 * 
	 * @param baseDir
	 *            指定根目录
	 * @param absFileName
	 *            相对路径名，来自于ZipEntry中的name
	 * @return java.io.File 实际的文件
	 */
	private static File getRealFileName(String baseDir, String absFileName) {
		String[] dirs = absFileName.split("/");
		// System.out.println(dirs.length);
		File ret = new File(baseDir);
		// System.out.println(ret);
		if (dirs.length > 1) {
			for (int i = 0; i < dirs.length - 1; i++) {
				ret = new File(ret, dirs[i]);
			}
		}
		if (!ret.exists()) {
			ret.mkdirs();
		}
		ret = new File(ret, dirs[dirs.length - 1]);
		return ret;
	}

}