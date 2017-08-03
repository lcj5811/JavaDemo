package com.lee.dealfile;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;

/**
 * @ClassName com.lee.dealfile.GetFilesMsg
 * @description 获取零散文件总大小及总个数
 * @author 凌霄
 * @data 2017年3月3日 上午9:44:50
 */
public class GetFilesMsg {

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		// System.out.println(GetFilesMsg.getInstance().getFilesSize("C:\\Users\\Lee\\Desktop\\tiles"));
		long[] a = GetFilesMsg.getInstance().getFileSize(new File("C:\\Users\\Lee\\Desktop\\tiles"));
		System.out.println("共计：" + getFormatSize(a[0]) + "(" + a[0] + "Byte)," + a[1] + "个文件");
		long end = System.currentTimeMillis();
		System.out.println("运行时间：" + (end - start) + "毫秒");
	}

	// 单例接口
	private GetFilesMsg() {
	}

	public static GetFilesMsg getInstance() {
		return SingletonHolder.sInstance;
	}

	private static class SingletonHolder {
		private static final GetFilesMsg sInstance = new GetFilesMsg();
	}

	// 取得文件大小
	public long getFileSizes(File f) throws Exception {
		long s = 0;
		if (f.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(f);
			s = fis.available();
			fis.close();
		} else {
			f.createNewFile();
			System.out.println("文件不存在");
		}
		return s;
	}

	/**
	 * 获取文件名
	 * 
	 * @param filePath
	 *            路径+文件名
	 * @param isExtension
	 *            是否需要扩展名
	 * @return
	 */
	public String getFilename(String filePath, boolean isExtension) {
		File file = new File(filePath);
		String filename = file.getName();
		if (isExtension) {
			int index = filename.lastIndexOf(".");
			filename = filename.substring(0, index);
		}
		return file.getName();
	}

	/**
	 * 获取文件大小，单位为byte（若为目录，则包括所有子目录和文件）
	 * 
	 * @param file
	 * @return long[]{文件大小，文件个数}
	 */
	public long[] getFileSize(File file) {
		long cont = 0;
		long size = 0;
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] subFiles = file.listFiles();
				if (subFiles != null) {
					int num = subFiles.length;
					for (int i = 0; i < num; i++) {
						size += getFileSize(subFiles[i])[0];
						cont += getFileSize(subFiles[i])[1];
					}
				}
			} else {
				cont++;
				size += file.length();
			}
		}
		return new long[] { size, cont };
	}

	/**
	 * 获取路径下下所有文件大小
	 * 
	 * @param filesPath
	 *            文件路径
	 * @return
	 */
	public String getFilesSize(String filesPath) {
		return getFormatSize(getFileSize(new File(filesPath))[0]);
	}

	/**
	 * 格式化单位
	 * 
	 * @param size
	 *            Byte
	 * @return
	 */
	public static String getFormatSize(double size) {
		double kiloByte = size / 1024;
		if (kiloByte < 1) {
			return size + "B";
		}

		double megaByte = kiloByte / 1024;
		if (megaByte < 1) {
			BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
			return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
		}

		double gigaByte = megaByte / 1024;
		if (gigaByte < 1) {
			BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
			return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
		}

		double teraBytes = gigaByte / 1024;
		if (teraBytes < 1) {
			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
			return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
		}
		BigDecimal result4 = new BigDecimal(teraBytes);
		return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
	}

}
