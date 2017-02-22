package com.lee.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.JOptionPane;

/**
 * @ClassName com.lee.Util
 * @description 工具类
 * @author 凌霄
 * @data 2017年2月20日 下午4:11:37
 */
public class SystemUtil {
	private SystemUtil() {
	}

	public static SystemUtil getInstance() {
		return SingletonHolder.sInstance;
	}

	private static class SingletonHolder {
		private static final SystemUtil sInstance = new SystemUtil();
	}

	private static String MESSAGE = "";

	/**
	 * 复制单个文件
	 * 
	 * @param srcFileName
	 *            待复制的文件名
	 * @param descFileName
	 *            目标文件名
	 * @param overlay
	 *            如果目标文件存在，是否覆盖
	 * @return 如果复制成功返回true，否则返回false
	 */
	public boolean copyFile(String srcFileName, String destFileName, boolean overlay) {
		File srcFile = new File(srcFileName);

		// 判断源文件是否存在
		if (!srcFile.exists()) {
			MESSAGE = "源文件：" + srcFileName + "不存在！";
			JOptionPane.showMessageDialog(null, MESSAGE);
			return false;
		} else if (!srcFile.isFile()) {
			MESSAGE = "复制文件失败，源文件：" + srcFileName + "不是一个文件！";
			JOptionPane.showMessageDialog(null, MESSAGE);
			return false;
		}

		// 判断目标文件是否存在
		File destFile = new File(destFileName);
		if (destFile.exists()) {
			// 如果目标文件存在并允许覆盖
			if (overlay) {
				// 删除已经存在的目标文件，无论目标文件是目录还是单个文件
				new File(destFileName).delete();
			}
		} else {
			// 如果目标文件所在目录不存在，则创建目录
			if (!destFile.getParentFile().exists()) {
				// 目标文件所在目录不存在
				if (!destFile.getParentFile().mkdirs()) {
					// 复制文件失败：创建目标文件所在目录失败
					return false;
				}
			}
		}

		// 复制文件
		int byteread = 0; // 读取的字节数
		InputStream in = null;
		OutputStream out = null;

		try {
			in = new FileInputStream(srcFile);
			out = new FileOutputStream(destFile);
			byte[] buffer = new byte[1024];

			while ((byteread = in.read(buffer)) != -1) {
				out.write(buffer, 0, byteread);
			}
			return true;
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		} finally {
			try {
				if (out != null)
					out.close();
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 复制整个目录的内容
	 * 
	 * @param srcDirName
	 *            待复制目录的目录名
	 * @param destDirName
	 *            目标目录名
	 * @param overlay
	 *            如果目标目录存在，是否覆盖
	 * @return 如果复制成功返回true，否则返回false
	 */
	public boolean copyDirectory(String srcDirName, String destDirName, boolean overlay) {
		// 判断源目录是否存在
		File srcDir = new File(srcDirName);
		if (!srcDir.exists()) {
			MESSAGE = "复制目录失败：源目录" + srcDirName + "不存在！";
			JOptionPane.showMessageDialog(null, MESSAGE);
			return false;
		} else if (!srcDir.isDirectory()) {
			MESSAGE = "复制目录失败：" + srcDirName + "不是目录！";
			JOptionPane.showMessageDialog(null, MESSAGE);
			return false;
		}

		// 如果目标目录名不是以文件分隔符结尾，则加上文件分隔符
		if (!destDirName.endsWith(File.separator)) {
			destDirName = destDirName + File.separator;
		}
		File destDir = new File(destDirName);
		// 如果目标文件夹存在
		if (destDir.exists()) {
			// 如果允许覆盖则删除已存在的目标目录
			if (overlay) {
				new File(destDirName).delete();
			} else {
				MESSAGE = "复制目录失败：目的目录" + destDirName + "已存在！";
				JOptionPane.showMessageDialog(null, MESSAGE);
				return false;
			}
		} else {
			// 创建目的目录
			System.out.println("目的目录不存在，准备创建。。。");
			if (!destDir.mkdirs()) {
				System.out.println("复制目录失败：创建目的目录失败！");
				return false;
			}
		}

		boolean flag = true;
		File[] files = srcDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 复制文件
			if (files[i].isFile()) {
				flag = copyFile(files[i].getAbsolutePath(), destDirName + files[i].getName(), overlay);
				if (!flag)
					break;
			} else if (files[i].isDirectory()) {
				flag = copyDirectory(files[i].getAbsolutePath(), destDirName + files[i].getName(), overlay);
				if (!flag)
					break;
			}
		}
		if (!flag) {
			MESSAGE = "复制目录" + srcDirName + "至" + destDirName + "失败！";
			JOptionPane.showMessageDialog(null, MESSAGE);
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 单线程文件复制最快的方法
	 * 
	 * @param source
	 *            源文件
	 * @param target
	 *            目标文件
	 */
	public void nioTransferCopy(File source, File target) {
		FileChannel in = null;
		FileChannel out = null;
		FileInputStream inStream = null;
		FileOutputStream outStream = null;
		try {
			inStream = new FileInputStream(source);
			outStream = new FileOutputStream(target);
			in = inStream.getChannel();
			out = outStream.getChannel();
			in.transferTo(0, in.size(), out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inStream.close();
				in.close();
				outStream.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取系统时间
	 * 
	 * @return yyyyMMdd_HHmmss
	 */
	public String getTimeName() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
		df.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
		return (df.format(new Date()));
	}

	public String getRunTime(long start, long end) {
		long time = end - start;
		if (time < 1000) {
			return time + "毫秒";
		} else if (time > 1000 && time < 60000) {
			return time / 1000 + "秒";
		} else if (time > 60000 && time < 3600000) {
			return time / 60000 + "分";
		} else {
			return time / 3600000 + "小时";
		}
	}
}
