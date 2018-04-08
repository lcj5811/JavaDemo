package com.lee.algorithm.eularpath;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

/**
 * 欧拉回路
 * 
 * @author:gaoshangshang
 * @version:0.9 date:2008-10-22
 * @description: 随机生成欧拉图，稀疏图 邻接表表示 复杂度O（|E|+|V|）
 *               算法描述：先对欧拉图执行一次深度优先搜索，找到第一个回路，此时，
 *               仍有部分边未被访问，则找出有尚未访问的边的路径上的第一个顶点，
 *               执行另外一次DFS，得到另外一个回路，然后把第二个回路拼接在第一 个回路上，继续该过程，直到所有的边都被遍历。
 * 
 */
public class EularCircuit {
	public EularCircuit() {
	}

	public static void main(String[] args) {
		System.out.println("please input n:");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int n = 4;
		try {
			n = Integer.parseInt(br.readLine());
		} catch (Exception ex) {
			return;
		}
		try {
			Graph g = new Graph(n);
			g.printg();
			g.circuit();
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return;
		}
	}
}

class Node {
	// 该变量纯粹为了便于给顶点默认命名，在此程序中无特殊用途
	private static int count = 0;
	private String name;
	// 该顶点的邻接表，里面存储的是SNode，内含该顶点邻接的顶点在图的顶点列表中的index和该边是否被访问
	private ArrayList<SNode> adjacencyList;

	public Node() {
		name = "node " + count;
		adjacencyList = new ArrayList<SNode>();
	}

	public Node(String name) {
		this.name = name;
		adjacencyList = new ArrayList<SNode>();
	}

	// 该函数用于测试本顶点的所有边是否都已经被访问，本程序未用
	public boolean isAllVisited() {
		for (int i = 0; i < adjacencyList.size(); i++) {
			SNode sn = (SNode) adjacencyList.get(i);
			if (sn.visited == false) {
				return false;
			}
		}
		return true;
	}

	// 返回该结点的邻接点的个数
	public int getAdjacencyCount() {
		return adjacencyList.size();
	}

	// 用于测试图中的index为i的顶点是否在本顶点的邻接表中
	public boolean contains(int i) {
		return this.adjacencyList.contains(new SNode(i));
	}

	// 删除本顶点的一个邻接点，该点的index为i
	public void removeAdjacencyNode(int i) {
		this.adjacencyList.remove(new SNode(i));
	}

	public void addAdjacencyNode(int i) { // i为邻接顶点在图顶点列表中的index。
		this.adjacencyList.add(new SNode(i));
	}

	// 返回邻接表中的第i个邻接点
	public SNode getAdjacencyNode(int i) {
		return (SNode) this.adjacencyList.get(i);
	}

	// 返回邻接表中一个SNode，该SNode的属性“index”为i_ref
	public SNode getAdjacencyNodeEX(int i_ref) {
		for (int i = 0; i < this.getAdjacencyCount(); i++) {
			if (getAdjacencyNode(i).index == i_ref) {
				return getAdjacencyNode(i);
			}
		}
		return null;
	}

	public String toString() {
		return this.name;
	}
}

// simple node,主要用于邻接表中，包含index（该点在图的nodelist中index）和visited（该边是否访问过）
class SNode {
	public boolean visited = false;
	public int index = 0;

	public SNode(int index) {
		this.index = index;
	}

	// 必须重载
	public boolean equals(Object o) {
		if (((SNode) o).index == this.index) {
			return true;
		}
		return false;
	}

	public String toString() {
		return " adjacency " + index;
	}
}

// 欧拉图，随机生成，如果生成失败，则抛出异常。
class Graph {
	private ArrayList<Node> nodeList; // 顶点列表
	private ArrayList<Integer> path; // 欧拉回路
	private int count; // 顶点数

	public Graph(int n) throws Exception {
		this.count = n;
		nodeList = new ArrayList<Node>(count);
		if (!ginit(count)) {

			throw new Exception("sorry,a failure,retry please");
		}

	}

	// 搜索欧拉回路，算法描述见文件顶部注释
	public void circuit() {
		path = new ArrayList<Integer>();
		int top = 0; // 当前回游的起点
		int k = 0; // 当前回游的起点在path中的index
		path.add(new Integer(0));
		while (true) {
			int i, j;
			// int init=0;
			i = top; // i为一次回游的当前点
			ArrayList<Integer> path1 = new ArrayList<Integer>();
			path1.add(new Integer(top));
			while (true) { // 执行一遍回路遍历，但不一定遍历所有的边
				Node node = (Node) nodeList.get(i);
				for (j = 0; j < this.count; j++) {
					if (node.contains(j) && node.getAdjacencyNodeEX(j).visited == false) {
						path1.add(new Integer(j));
						node.getAdjacencyNodeEX(j).visited = true;
						((Node) nodeList.get(j)).getAdjacencyNodeEX(i).visited = true;
						i = j;
						break;
					}
				}
				if (i == top) {
					break;
				}
			}
			path.remove(k);
			path.addAll(k, path1);
			k++;
			if (k >= path.size()) {
				break;
			}
			top = ((Integer) path.get(k)).intValue();
		}
		for (int z = 0; z < path.size(); z++) {
			System.out.print(path.get(z).toString() + "");
		}
	}

	// 随机生成欧拉图
	private boolean ginit(int n) {
		int i;
		for (i = 0; i < n; i++) {
			nodeList.add(new Node("node " + i));
		}
		ArrayList<Integer> linked = new ArrayList<Integer>();
		linked.add(new Integer(0));
		Random rand = new Random();
		// 生成随机连通图
		for (i = 1; i < n; i++) {
			int size = linked.size(); // 取得连通集合里面的最后进入的顶点index
			int top = ((Integer) (linked.get(size - 1))).intValue();
			Node node = (Node) (nodeList.get(top)); // 根据top取得该node
			while (true) {
				int randint = rand.nextInt(n);
				if (randint == top) {
					continue;
				}
				if (node.contains(randint)) {
					continue;
				} else {
					if (!linked.contains(new Integer(randint))) {
						linked.add(new Integer(randint));
					} else if (node.getAdjacencyCount() >= (linked.size() - 1 > 6 ? 6 : linked.size() - 1)) {
						continue;
					} else {
						i--;
					}
					node.addAdjacencyNode(randint);
					Node randnode = (Node) nodeList.get(randint);
					randnode.addAdjacencyNode(top);
					break;
				} // end if
			} // end while
		} // end for
			// printg();//for test
			// 使所有的顶点度数为偶数
		/*
		 * 如果该顶点v1的度数为奇数，则从后面找一个度数也为奇数的点v2， 1、如果v1和v2已经有边,则如果两个点的度数不为1，则去掉它们的连边，
		 * 否则继续往后查找度数为奇数的点。 2、如果v1和v2没有边，则在它们之间连边
		 * 3、如果没有找到合适的点，则再找一度数为奇的点v3，和一个度数为偶的点v4，如果v4
		 * 和v1，v3没有相连，则在v4和v1，v4和v3之间加边。 4、如果仍然没有找到，4、1
		 * 则将该点与任意一个不相连的点之间加边，重新执行该“变偶”操作; 4、2
		 * 如果找不到与该点不相连的点，则找一度数大于2的点，去除与该点的边，重新执行
		 */
		for (i = 0; i < this.count - 1; i++)

		{
			Node node = (Node) nodeList.get(i);
			if (node.getAdjacencyCount() % 2 != 0) {
				int j = 0;
				for (j = i + 1; j < this.count; j++) {
					Node nn = (Node) nodeList.get(j);
					if (nn.getAdjacencyCount() % 2 != 0) {
						if (node.contains(j)) { // 1、见上注释
							if (nn.getAdjacencyCount() != 1 && node.getAdjacencyCount() != 1) {
								node.removeAdjacencyNode(j);
								nn.removeAdjacencyNode(i);
								break;
							} else {
								continue;
							}
						} else { // 2、见上注释
							node.addAdjacencyNode(j);
							nn.addAdjacencyNode(i);
							// i = j;
							break;
						} // end if
					} // end if
				} // end for
				if (j == this.count) {
					int k;
					Node nk = null;
					for (k = i + 1; k < this.count; k++) {// 3、见上注释
						nk = (Node) nodeList.get(k);
						if (nk.getAdjacencyCount() % 2 != 0) {
							break;
						}
					}
					int kk = k;
					for (k = 0; k < i; k++) {
						Node n1 = (Node) nodeList.get(k);
						if (!n1.contains(kk) && !n1.contains(i)) {
							n1.addAdjacencyNode(kk);
							nk.addAdjacencyNode(k);
							n1.addAdjacencyNode(i);
							node.addAdjacencyNode(k);
							break;
						}
					}
					boolean retry = false;
					if (k == i) {// 4、1 见上注释
						int vv;
						for (vv = 0; vv < this.count; vv++) {
							Node vn = (Node) nodeList.get(vv);
							if (!vn.contains(i) && i != vv) {
								vn.addAdjacencyNode(i);
								node.addAdjacencyNode(vv);
								retry = true;
								break;
							}
						}
						if (vv == count) {// 4、2 见上注释
							for (vv = 0; vv < count; vv++) {
								Node vnn = (Node) nodeList.get(vv);
								if (vnn.getAdjacencyCount() > 1) {
									vnn.removeAdjacencyNode(i);
									node.removeAdjacencyNode(vv);
									retry = true;
									break;
								}
							}
						}
					}
					if (retry) {
						i = -1;
					}
				}
			} // end if
		}
		return this.isEularG();
	}

	// end function

	// 判断生成的图是否为欧拉图
	public boolean isEularG() {
		boolean isEular = true;
		for (int i = 0; i < this.count; i++) {
			Node n = (Node) nodeList.get(i);
			if (n.getAdjacencyCount() % 2 != 0) {
				isEular = false;
				break;
			}
		}
		return isEular;
	}

	public void printg() {
		for (int i = 0; i < this.count; i++) {
			Node n = (Node) nodeList.get(i);
			System.out.print(n.toString() + "");
			for (int j = 0; j < n.getAdjacencyCount(); j++) {
				System.out.print(n.getAdjacencyNode(j).toString() + "");
			}
			System.out.println();
		}
	}
}