package com.lee.dealfile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileName {
	public static void main(String[] args) {
		ArrayList<String> name = findFile(new File("G:\\东安入库\\01\\mdb表2\\花桥镇"),
				new ArrayList<String>());
		for (String n : name) {
			saveText("G:\\东安入库\\02\\问题NEW\\花桥镇\\" + n + ".CSV", null, null);
		}
	}

	public static ArrayList<String> findFile(File file, ArrayList<String> pic) {
		if (file.isDirectory()) { // 否则如果它是一个目录
			File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
			for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
				findFile(files[i], pic); // 把每个文件 用这个方法进行迭代
			}
		} else if (file.exists()) {
			pic.add(file.getName().replace("确权数据表.mdb", ""));
		}
		return pic;
	}

	public static void saveText(String GJPath, ArrayList<String> line, File f) {
		if (GJPath != null) {
			f = new File(GJPath);
			if (!f.exists()) {
				try {
					File parent = f.getParentFile();
					parent.mkdirs();
					f.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				f.delete();
			}
		}
		if (line != null) {
			if (line.size() == 0) {
				System.out.println("无");
			} else {
				FileWriter fw = null;
				BufferedWriter bw = null;
				try {
					fw = new FileWriter(f, true);
					bw = new BufferedWriter(fw);
					for (int i = 0; i < line.size(); i++) {
						bw.write(line.get(i));
						bw.newLine();
					}
					bw.flush();
					fw.flush();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (bw != null) {
							bw.close();
						}
						if (fw != null) {
							fw.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
