package com.lee.mathmodel.eularpath;

import javax.swing.*;
import java.util.regex.Pattern;

/**
 * 建立欧拉图，判断有无欧拉回路
 * @author lxrmido
 * @Website buzhuanggeek.com
 * @Edit GVIM_073
 * @Date 2011/09/11
 */
public class Eular {
	private static boolean points[][];
	private static int nop;
	private static int stack[];
	private static int cur_sp = 0;
	private static StringBuilder route = new StringBuilder();
	private static StringBuilder routem = new StringBuilder();

	private static void push(int elem) {
		stack[cur_sp++] = elem;
	}

	private static int pop() {
		return stack[--cur_sp];
	}

	private static boolean exist(int elem) {
		for (int i = 0; i < cur_sp; i++) {
			if (stack[i] == elem)
				return true;
		}
		return false;
	}

	private static void trace(int cp) {
		push(cp);
		boolean zombie = true;
		boolean mesh = false;
		for (int i = 0; i < nop; i++) {
			if (points[cp][i] == true) {
				if (!exist(i)) {
					zombie = false;
					trace(i);
				} else if (i == stack[0]) {
					mesh = true;
				}
			}
		}
		if (zombie && cur_sp == nop) {
			if (mesh) {
				push(stack[0]);
				addRoute(routem);
				pop();
			} else {
				addRoute(route);
			}
		}
		pop();
	}

	private static void addRoute(StringBuilder Route) {
		StringBuilder tmprt = new StringBuilder();
		for (int i = cur_sp - 1; i >= 0; i--) {
			tmprt.append(stack[i]);
			if (i != 0)
				tmprt.append("-");
		}
		tmprt.append("\n");
		if (Route.indexOf(tmprt.toString()) < 0) {
			for (int i = 0; i < cur_sp; i++) {
				Route.append(stack[i]);
				if (i != cur_sp - 1)
					Route.append("-");
			}
			Route.append("\n");
		}
	}

	public static void main(String[] args) {
		String tmp = JOptionPane.showInputDialog(null, "请输入无向图的结点数：");
		while (!(tmp.matches("\\d+"))) {
			tmp = JOptionPane.showInputDialog(null, "输入有误，请重新输入有向图的结点数：");
		}
		nop = Integer.parseInt(tmp);
		if (nop == 1) {
			JOptionPane.showMessageDialog(null, "结点数为1，故此图为欧拉图，欧拉通路、回路均为 0 。");
			System.exit(0);
		}
		points = new boolean[nop][nop];
		stack = new int[nop * 2];
		String tmpary[];
		while ((tmp = JOptionPane.showInputDialog(null, "请输入无向路径，结点以数字表示,以任意符号或空格分隔（如 0-1-2-3,不再输入点取消：")) != null) {
			boolean eftv = true;
			if (!(tmp.matches("\\d.*"))) {
				JOptionPane.showMessageDialog(null, "含有不存在的结点，本路径无效！");
				continue;
			}
			Pattern p = Pattern.compile("\\D+");
			tmpary = p.split(tmp);
			for (int i = 0; i < tmpary.length; i++) {
				if (Integer.parseInt(tmpary[i]) >= nop) {
					JOptionPane.showMessageDialog(null, "含有不存在的结点，本路径无效！");
					eftv = false;
					break;
				}
			}
			if (!eftv) {
				eftv = true;
				continue;
			}
			for (int i = 0; i < tmpary.length - 1; i++) {
				points[Integer.parseInt(tmpary[i])][Integer.parseInt(tmpary[i + 1])] = true;
			}
		}
		stack = new int[nop * 2];
		for (int i = 0; i < nop; i++) {
			push(i);
			for (int j = 0; j < nop; j++) {
				if (points[i][j] == true) {
					trace(j);
				}
			}
			pop();
		}
		if (route.length() > 1) {
			JOptionPane.showMessageDialog(null, "所有欧拉通路（不含回路）为：\n" + route);
			if (routem.length() > 1) {
				JOptionPane.showMessageDialog(null, "该图是欧拉图，所有欧拉回路为：\n" + routem);
			} else {
				JOptionPane.showMessageDialog(null, "该图不是欧拉图");
			}
		} else {
			JOptionPane.showMessageDialog(null, "不含欧拉通路。");
		}
		System.exit(0);
	}
}