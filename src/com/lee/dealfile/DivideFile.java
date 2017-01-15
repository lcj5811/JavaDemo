package com.lee.dealfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DivideFile {

	public static void main(String[] args) throws IOException {
		// 需要分割的文件放置的路径
		String path = "E:/";
		// 需要分割的文件的文件名称
		String base = "ty";
		// 需要分割的文件的后缀名
		String ext = ".gemf";
		// 以每个小文件1024*1024字节即1M的标准来分割
		int split = 1024 * 1024;
		byte[] buf = new byte[1024];
		int num = 1;
		// 建立输入流
		File inFile = new File(path + base + ext);
		FileInputStream fis = new FileInputStream(inFile);
		while (true) {
			// 以"demo"+num+".db"方式来命名小文件即分割后为demo1.db，demo2.db，。。。。。。
			FileOutputStream fos = new FileOutputStream(new File(path + base
					+ num + ext));
			for (int i = 0; i < split / buf.length; i++) {
				int read = fis.read(buf);
				fos.write(buf, 0, read);
				// 判断大文件读取是否结束
				if (read < buf.length) {
					fis.close();
					fos.close();
					return;
				}
			}
			fos.close();
			num++;
		}
	}
}
