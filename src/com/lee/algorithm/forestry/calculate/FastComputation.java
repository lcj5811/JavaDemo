package com.lee.algorithm.forestry.calculate;

import java.text.DecimalFormat;

//浙江二调蓄积、株数标准速算法
public class FastComputation {

	// 树种
	protected int W;

	// 目估树高、胸径
	protected double H;
	protected double D;

	// 角规绕测断面积、算得每公顷断面积、结果断面积
	protected double G1;
	protected double G2;
	protected double G3;

	// 目估、算得、结果疏密度
	protected double S1;
	protected double S2;
	protected double S3;

	//
	protected double Cm1;
	protected double Cm2;

	protected double Vm1;
	protected double Vm2;
	protected double Vm3;

	public DecimalFormat df1 = new DecimalFormat("#.0");
	public DecimalFormat df = new DecimalFormat("#");

	protected int T;

	public static void main(String[] args) throws Exception {

		for (String a : new FastComputation(12, 7, 7, 1, 1).execute()) {
			System.out.println(a);
		}
	}

	// 树高，胸径，要素值，树种组，算法类型
	public FastComputation(double h, double d, double f, int w, int t) {
		this.D = d;
		this.H = h;
		this.W = w;
		this.T = t;
		switch (t) {
		case 0:
			// 目估疏密度
			this.S1 = f;
			break;
		case 1:
			// 目估蓄积
			this.Vm1 = f;
			break;
		case 2:
			// 角规断面积
			this.G1 = f;
			break;
		}
	};

	public String[] execute() {
		switch (T) {
		case 0:
			return MGSMD();
		case 1:
			return MGMMXJ();
		case 2:
			return JGRCDM();
		default:
			return null;
		}
	};

	protected String[] MGSMD() {
		S3 = S1 / 10;
		Vm3 = S3 * queryData(1);
		Cm2 = S1 / 10 * queryData(0);
		G3 = Vm3 * 15 / XG() / (H + 3);
		return toStingArray(new double[] { S3, Cm2, Vm3, G3 });
	}

	protected String[] MGMMXJ() {
		S3 = Vm1 / queryData(1);
		Cm2 = queryData(0) * Vm1 / queryData(1);
		Vm3 = Vm1;
		G3 = Vm3 * 15 / XG() / (H + 3);
		return toStingArray(new double[] { S3, Cm2, Vm3, G3 });
	}

	protected String[] JGRCDM() {
		Vm3 = G1 * XG() * (H + 3) / 15;
		S3 = Vm3 / queryData(1);
		Cm2 = S3 * queryData(0);
		G3 = G1;
		return toStingArray(new double[] { S3, Cm2, Vm3, G3 });
	}

	protected double XG() {
		switch (W) {
		case 0:
			return 0.39;
		case 1:
			return 0.42;
		case 2:
			return 0.38;
		default:
			return 0.39;
		}
	};

	// protected double BZLFMZS() {
	// return 748;
	// }
	//
	// protected double MMXJ() {
	// return 5.73;
	// }

	protected String[] toStingArray(double[] a) {
		String[] s = new String[4];
		int i = 0;
		for (double b : a) {
			if (i == 1) {
				s[i] = String.valueOf(df.format(b));
			} else {
				s[i] = String.valueOf(df1.format(b));
			}
			i++;
		}
		return s;
	}

	protected double queryData(double type) {
		if (type == 0) {
			int a = (int) this.H - 4;
			int b = (int) (this.D - 5) * 3 + this.W;
			if ((int) this.D < RefData.SGtoXJ[a][0] || (int) this.D > RefData.SGtoXJ[a][1]) {
				type = 0;
			} else {
				type = RefData.BZLFMMZS[a][b];
			}
		} else {
			int a = (int) this.H - 4;
			int b = this.W;
			type = RefData.SMD1MMXJ[a][b];
		}
		System.out.println(type + "check");
		return type;
	}
}
