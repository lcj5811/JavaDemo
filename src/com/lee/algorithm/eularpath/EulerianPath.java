package com.lee.algorithm.eularpath;

class BTNode {
	String data;
	BTNode left;
	BTNode right;

	BTNode(String data, BTNode left, BTNode right) {
		this.data = data;
		this.left = left;
		this.right = right;
	}
}

class BTree {
	protected BTNode root;

	BTree(BTNode root) {
		this.root = root;
	}

	public BTNode getRoot() {
		return root;
	}
}

class init {
	BTNode a = new BTNode("1", null, null);
	BTNode b = new BTNode("2", null, null);
	BTNode d = new BTNode("6", null, null);
	BTNode e = new BTNode("9", null, null);
	BTNode f = new BTNode("2", null, null);
	BTNode g = new BTNode("+", a, b);
	BTNode h = new BTNode("5", null, null);
	BTNode m = new BTNode("*", e, f);
	BTNode n = new BTNode("-", g, h);
	BTNode p = new BTNode("+", d, m);
	BTNode q = new BTNode("*", n, p);// 根结点
}

class count {
	count(BTNode p) {

	}
}

/**
 * 采用欧拉路径遍历二叉树java
 * 
 * @author Lee
 *
 */
public class EulerianPath {
	public static void visit(BTNode p) {
		System.out.print(p.data + " ");
	}

	// 欧拉遍历二叉树
	protected static void eulerTour(BTree T, BTNode p) {
		visit(p);
		if (p.left != null && p.right != null) {
			if (p != null) {
				eulerTour(T, p.left);
			}
		}
		visit(p);
		if (p.left != null && p.right != null) {
			eulerTour(T, p.right);
		}
		visit(p);
	}

	public static void main(String args[]) {
		init T = new init();
		BTree tree = new BTree(T.q);
		System.out.print("欧拉遍历：");
		eulerTour(tree, tree.getRoot());
	}
}
