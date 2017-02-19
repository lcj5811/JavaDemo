package com.lee.mathmodel.geneAnalyze;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @ClassName com.lee.dealoffice.XlsFile
 * @description 卡方计算
 * @author 凌霄
 * @data 2016年9月17日 上午8:16:15
 */
public class ChiSquare {

	static int AA = 0, AT = 0, AC = 0, AG = 0;
	static int TA = 0, TT = 0, TC = 0, TG = 0;
	static int CA = 0, CT = 0, CC = 0, CG = 0;
	static int GA = 0, GT = 0, GC = 0, GG = 0;
	static int lineNumber = 0;

	public static void main(String[] args) throws IOException {
		lineNumber = 0;
		readTxt("D:\\数学建模\\B_基因\\处理数据\\WorkSpace\\Q4\\1000.csv",
				"D:\\数学建模\\B_基因\\处理数据\\WorkSpace\\Q4\\1000-output.csv");
	}

	@SuppressWarnings("deprecation")
	public static void readTxt(String inputFilePath, String outputFilePath) throws IOException {

		File file = new File(inputFilePath);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;

		FileWriter fw = null;
		BufferedWriter bw = null;

		try {
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);

			fw = new FileWriter(new File(outputFilePath));
			if (!file.exists()) {
				file.createNewFile();
			}
			bw = new BufferedWriter(fw);

			while (dis.available() != 0) {
				System.out.print(++lineNumber + "------");
				bw.write(calcOutput(dis.readLine().split(",")) + "\n");
				System.out.println("");
			}
			bw.close();
			fw.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			fis.close();
			bis.close();
			dis.close();
		}
	}

	// 卡方分布，计算卡方值
	private static String calcOutput(String[] line) {
		ArrayList<Double> X = new ArrayList<Double>();
		if (lineNumber == 1257) {
			X.add(382.0);
			X.add(4.0);
			X.add(105.0);
			X.add(423.0);
			X.add(0.0);
			X.add(86.0);
			X.add(6212.0);
		} else if (lineNumber == 1814) {
			X.add(319.0);
			X.add(5.0);
			X.add(95.0);
			X.add(418.0);
			X.add(0.0);
			X.add(91.0);
			X.add(9052.0);
		} else {
			for (String a : line) {
				if (!a.equalsIgnoreCase("0") && a.length() != 4) {
					X.add(Double.valueOf(a));
				} else if (a.length() == 4) {
					X.add(Double.valueOf(a));
				}
			}
		}

		// private static String calcOutput(String[] line) {
		// ArrayList<Double> X = new ArrayList<Double>();
		// // if (lineNumber == 6789) {
		// // X.add(6.0);
		// // X.add(99.0);
		// // X.add(395.0);
		// // X.add(.0);
		// // X.add(111.0);
		// // X.add(389.0);
		// // X.add(8496.0);
		// // }else
		// if (lineNumber == 1650) {
		// X.add(404.0);
		// X.add(91.0);
		// X.add(5.0);
		// X.add(412.0);
		// X.add(88.0);
		// X.add(0.0);
		// X.add(8258.0);
		// } else {
		// for (String a : line) {
		// if (!a.equalsIgnoreCase("0") && a.length() != 4) {
		// X.add(Double.valueOf(a));
		// } else if (a.length() == 4) {
		// X.add(Double.valueOf(a));
		// }
		// }
		// }

		for (

		Double double1 : X) {
			System.out.print(double1 + ",");
		}
		double a = X.get(0), b = X.get(1), c = X.get(2), d = X.get(3), e = X.get(4), f = X.get(5), output = 0.0;
		output = (2 * Math.pow((a - (a + d) / 2), 2)) / (a + d) + (2 * Math.pow((b - (b + e) / 2), 2)) / (b + e)
				+ (2 * Math.pow((c - (c + f) / 2), 2)) / (c + f) + (2 * Math.pow((d - (d + a) / 2), 2)) / (d + a)
				+ (2 * Math.pow((e - (e + b) / 2), 2)) / (e + b) + (2 * Math.pow((f - (f + c) / 2), 2)) / (f + c);
		String out = output + "," + X.get(6);
		// b += e;
		// a += d;
		// c += f;
		// output = (Math.pow((((2 * a + b) / 2 * (2 * a + b) / 2000 - a)), 2))
		// / (2 * a + b) * (2 * a + b) / 4000
		// + (Math.pow((((2 * a + b) / 2 * (2 * c + b) / 2000 - b)), 2)) / (2 *
		// a + b) * (2 * c + b) / 4000
		// + (Math.pow((((2 * c + b) / 2 * (2 * c + b) / 2000 - c)), 2)) / (2 *
		// c + b) * (2 * c + b) / 4000;
		// String out = output + "," + X.get(6);
		X.clear();
		return out;
	}

}
