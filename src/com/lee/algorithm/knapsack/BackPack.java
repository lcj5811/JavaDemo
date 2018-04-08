package com.lee.algorithm.knapsack;

/**
 * 背包问题（动态规划）
 * 
 */
public class BackPack {
	public int[] v;// 价值数组
	public int[] w;// 重量数组
	public int c;// 背包容量
	public int[][] m;// m(i,j)是背包容量为j，可选物品为，i，i+1，i+2，i+3,...,n 时0-1背包问题的最优值
	public int[] x;// 保存第i个物品是否放入背包，1：放入；0：未放入

	public BackPack(int[] vv, int[] ww, int cc) {
		this.v = vv;
		this.w = ww;
		this.c = cc;
		this.x = new int[vv.length];
		// 获取j的最大值，从而通过new关键字创建m数组，在内存中维数组分配相应的存储空间
		int max = maxM(w, c);
		m = new int[v.length][max + 1];
	}

	/**
	 * 获取重量和背包容量的最大值，从而为m创建相应的存储空间
	 * 
	 * @param w
	 * @param c
	 * @return
	 */
	public int maxM(int[] w, int c) {
		int max = c;
		for (int i = 1; i < w.length; i++) {
			if (w[i] > c)
				max = w[i];
		}
		return max;
	}

	public void knapsack(int[] v, int[] w, int c, int[][] m) {
		int n = v.length - 1;
		int jMax = Math.min(w[n] - 1, c);
		for (int j = 0; j <= jMax; j++)
			m[n][j] = 0;
		for (int j = w[n]; j <= c; j++)
			m[n][j] = v[n];
		for (int i = n - 1; i > 1; i--) {
			jMax = Math.min(w[i] - 1, c);
			for (int j = 0; j <= jMax; j++)
				m[i][j] = m[i + 1][j];
			for (int j = w[i]; j <= c; j++)
				m[i][j] = Math.max(m[i + 1][j], m[i + 1][j - w[i]] + v[i]);
		}
		// m[1][c]=m[2][c];
		// 对于i=1时的两种情况
		if (c >= w[1])
			m[1][c] = Math.max(m[2][c], m[2][c - w[1]] + v[1]);
		else
			m[1][c] = m[2][c];

	}

	public void traceback(int[][] m, int[] w, int c, int[] x) {
		int n = w.length - 1;
		for (int i = 1; i < n; i++) {
			// 第i个包未放入
			if (m[i][c] == m[i + 1][c])
				x[i] = 0;
			else {
				x[i] = 1;
				// c是全局变量
				c -= w[i];
			}
		}
		x[n] = (m[n][c] > 0) ? 1 : 0;
	}

	public static void main(String[] args) {
		int v[] = { 1, 1, 1, 1 };// 输入参数1：航线价值
		int w[] = { 0, 96880, 67246, 90310 };// 输入参数2：飞行时间
		int c = 175000;
		BackPack pa = new BackPack(v, w, c);
		pa.knapsack(pa.v, pa.w, pa.c, pa.m);
		System.out.println("装入背包中物品总价值最大为：");
		System.out.println(pa.m[1][c]);
		pa.traceback(pa.m, pa.w, pa.c, pa.x);
		System.out.println("装入的物品的序号为：");
		for (int i = 0; i <= v.length - 1; i++) {
			if (pa.x[i] == 1)
				System.out.print(i + " ");
		}
	}
}