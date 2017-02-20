package com.lee.mathmodel.netAna;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName csuft.ygzx.path AllPath
 * @Description: 求从A点B点所有可行的路径（不能经过重复点） 若条件为不能经过重复的路径， 注释(index != -1)的判断
 *               此种方法为递归方法，若点的数据过多，需改写成双重循环的方法（递归层级不宜太深）
 * @author DZ.Yang
 * @date 2016年10月17日 上午2:42:34
 * @version V1.0
 */

public class AllPath_v0 {
	// 将路径构建成一个对称矩阵
	final int size = 11;
	int[][] maze = new int[size][size];

	// 存放搜索时经过路径的点
	List<Integer> path = new ArrayList();
	int id = 0;

	public AllPath_v0() {
		// 数据格式：两个点的编号，从1开始
		int[][] lines = { { 1, 2, 5 }, // 表示1号点与2号点相连(后面可加权重[即距离])
				{ 1, 7, 56 }, //
				{ 1, 9, 58 }, //
				{ 2, 3, 54 }, //
				{ 2, 10, 53 }, //
				{ 3, 9, 51 }, //
				{ 3, 10, 50 }, //
				{ 3, 11, 51 }, //
				{ 4, 9, 51 }, //
				{ 4, 11, 53 }, //
				{ 5, 6, 57 }, //
				{ 5, 8, 56 }, //
				{ 5, 11, 54 }, //
				{ 6, 7, 57 }, //
				{ 6, 8, 51 }, //
				{ 7, 9, 50 }, //
				{ 8, 9, 50 },//
		};
		// 构建对称矩阵
		int x, y;
		for (int i = 0, rows = lines.length; i < rows; ++i) {
			x = lines[i][0] - 1;
			y = lines[i][1] - 1;
			maze[x][y] = maze[y][x] = lines[i][3];
		}
		// 打印出对称矩阵
		System.out.println("源数据：");
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				System.out.print(maze[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("结果：");
	}

	/**
	 * 求得所有可行的路径
	 * 
	 * @param start
	 *            开始点编号
	 * @param end
	 *            void 结束点编号
	 */
	void pass(int start, final int end) {
		path.add(start);
		if (start == end) {// 找到一条路径
			System.out.print("路径" + (++id) + "：");
			for (int i = 0, size = path.size(); i < size; ++i) {
				System.out.print((path.get(i) + 1) + " ");
			}
			System.out.println();
		} else {
			int index = -1, temp;
			for (int i = 0; i < size; ++i) {
				if (maze[start][i] == 1) {
					index = path.indexOf(i);
					if (index != -1) {
						continue;
					}
					temp = maze[start][i];
					// 改写数据，表示此点已遍历过
					maze[start][i] = maze[i][start] = -1;
					pass(i, end);
					// 还原数据
					maze[start][i] = maze[i][start] = temp;
				}
			}
		}
		path.remove(path.size() - 1);
	}

	public static void main(String[] args) {
		AllPath_v0 instance = new AllPath_v0();
		int start = 1, end = 11;
		instance.pass(start - 1, end - 1);
		System.out.println("************over************");
	}

}
