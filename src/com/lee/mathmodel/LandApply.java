package com.lee.mathmodel;

import javax.swing.JOptionPane;

public class LandApply {
	public static void main(String[] args) {

		// args[]--元数据、输出
		Window.inTable("===start===");
		System.out.println("===start===");
		switch (Window.whichDialog) {
		case 1:
			MinDistance0.main(args);
			break;

		}
		System.out.println("===over===");
		Window.inTable("===over===");
		JOptionPane.showMessageDialog(null, "任务完成!", "提示信息", JOptionPane.INFORMATION_MESSAGE);
		Window.startJButton.setEnabled(true);
	}
}
