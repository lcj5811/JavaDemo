package com.lee.mathmodel.netAna;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @ClassName com.lee.mathmodel.MinDistance
 * @description 求两元素间的最短路径和最短距离
 * @author 凌霄
 * @data 2016年9月26日 下午8:26:21
 */
/**
 * @author Lee
 *
 */
public class MinDistance1 {
	static ArrayList<String[]> SRCFile = new ArrayList<>();
	static ArrayList<String[]> SRCFile_T = new ArrayList<>();

	static ArrayList<String> newBiJiao = new ArrayList<>();
	static ArrayList<String> newBiJiao_1 = new ArrayList<>();
	static HashMap<Integer, String> newPanelPoints = new HashMap<>();
	static int i = 0, j = 0;
	static boolean flag = false;

	static int[][] minDisMatrix;

	public static void main(String[] args) throws IOException {
		SRCFile = readSRC("D:\\MinDistance\\MyTest.csv");
		SRCFile_T = SRCFile;
		calcMinDitance_0(SRCFile, SRCFile_T);
	}

	public MinDistance1(String mCSVPath) throws IOException {
		SRCFile = readSRC(mCSVPath);
		SRCFile_T = SRCFile;
		calcMinDitance_0(SRCFile, SRCFile_T);
	}

	public int[][] getMinDisMatrix() {
		return minDisMatrix;
	}

	private static void calcMinDitance_0(ArrayList<String[]> srcFile, ArrayList<String[]> srcFile_T) {

		minDisMatrix = new int[srcFile.size()][srcFile.size()];
		int r = 0, l = 0;

		for (String[] a : srcFile) {
			i++;
			newBiJiao = new ArrayList<>();
			newBiJiao_1 = new ArrayList<>();

			for (String[] b : srcFile_T) {
				j++;
				// System.out.print(i + "--------");
				newBiJiao.add(calcMinDitance_1(a, b) + "");
			}
			j = 0;
			// System.out.println("---------------------------------------");
			for (String acd : calcMinDitance_2(newBiJiao, srcFile_T)) {
				// System.out.print(r + ",");
				// System.out.println(l+"->"+acd);
				minDisMatrix[r][l] = Integer.valueOf(acd);
				l++;
			}
			j = 0;
			l = 0;
			r++;
			// System.out.println();
			// System.out.println("=======================================");
		}

		for (int[] a : minDisMatrix) {
			for (int i : a) {
				System.out.print(i + " ");
			}
			System.out.println();
		}

	}

	/**
	 * 
	 * 第一次，两列数据比较，相加取最小
	 * 
	 * @param srcFile
	 *            列
	 * @param srcFile_T
	 *            比较列
	 * @return
	 */
	private static int calcMinDitance_1(String[] srcFile, String[] srcFile_T) {
		int BiJiao = Integer.MAX_VALUE;
		for (int i = 0; i < srcFile.length; i++) {
			if (Integer.valueOf(srcFile[i]) + Integer.valueOf(srcFile_T[i]) <= BiJiao) {
				BiJiao = Integer.valueOf(srcFile[i]) + Integer.valueOf(srcFile_T[i]);
			}
		}
		for (int i = 0; i < srcFile.length; i++) {
			if (Integer.valueOf(srcFile[i]) + Integer.valueOf(srcFile_T[i]) == BiJiao) {
				if (i + 1 != j && i + 1 != MinDistance1.i) {
					if (flag)
						// System.out.print(",");
						flag = true;
					// System.out.print(i + 1);
					// newPanelPoints.put(j, i+1+",");

				}
			}
		}

		// System.out.println("--------" + j + "-->" + BiJiao);
		flag = false;

		return BiJiao;
	}

	/**
	 * 重复第一次算法判断是否达到最小值
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private static ArrayList<String> calcMinDitance_2(ArrayList<String> a, ArrayList<String[]> b) {
		String[] aa = ArrayListToStringArray(a);
		j = 0;
		for (String[] bb : b) {
			j++;
			// System.out.print(i + "--------");
			newBiJiao_1.add(calcMinDitance_1(aa, bb) + "");
		}
		// System.out.println("---------------------------------------");
		while (!JudgeArrayList(newBiJiao, newBiJiao_1)) {
			newBiJiao = newBiJiao_1;
			newBiJiao_1 = new ArrayList<>();
			calcMinDitance_2(newBiJiao, b);
		}
		return newBiJiao_1;
	}

	/**
	 * 读取文件到内存
	 * 
	 * @param FilePath
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	private static ArrayList<String[]> readSRC(String FilePath) throws IOException {
		ArrayList<String[]> mSRCFile = new ArrayList<String[]>();
		File file = new File(FilePath);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;

		try {
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);

			while (dis.available() != 0) {
				mSRCFile.add(dis.readLine().split(","));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			fis.close();
			bis.close();
			dis.close();
		}

		return mSRCFile;
	}

	/**
	 * ArrayList转String[]
	 * 
	 * @param input
	 *            输入对象
	 * @return
	 */
	private static String[] ArrayListToStringArray(ArrayList<String> input) {
		String[] v = new String[input.size()];
		v = input.toArray(v);
		return v;
	}

	/**
	 * 判断两列是否相等
	 * 
	 * @param first
	 *            第一列
	 * @param second
	 *            第二列
	 * @return
	 */
	private static boolean JudgeArrayList(ArrayList<String> first, ArrayList<String> second) {
		for (int i = 0; i < first.size(); i++) {
			if (Integer.valueOf(first.get(i)) != Integer.valueOf(second.get(i))) {
				return false;
			}
		}
		return true;
	}

}
