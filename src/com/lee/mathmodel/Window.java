package com.lee.mathmodel;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;

public class Window extends JFrame {
	private static final long serialVersionUID = 8024877786837672000L;

	// private JLabel formworkJLabel; // 模板路径标签
	private JLabel outputJLabel; // 输出路径标签
	private JLabel dbJLabel; // 数据库路径标签

	// private JTextField formworkJTextField; // 模板路径显示区
	private JTextField outpuJTextField; // 输出路径显示区
	private JTextField dbJTextField; // 数据库路径显示区

	// private JButton formworkJButton; // 模板选择按钮
	private JButton outputJButton; // 输出选择按钮
	private JButton dbJButton; // 数据库选择按钮

	private JFileChooser fchPath; // 文件夹选择
	private static JTable tabFile; // 执行日志
	private static JScrollBar sBar;
	private JPanel pnlFile; // 文件列表承载面板（列表为tabFile，它会被放置到下面的JScrollPane）
	private JScrollPane jspFile; // 文件列表承载面板，用来承载tabFile，而被pnlFile承载
	private String mTitle;

	public static JButton startJButton; // 开始处理按钮
	public static int whichDialog;// 确定执行何种任务

	/**
	 * 布局和属性设置
	 */
	private void initLayoutAndProperties() {
		whichDialog = 1;
		initializeWindow(whichDialog);

		// 1
		// mTitle = "导出发包方调查表";

		// 2
		// mTitle = "导出承包方调查表";

		// 3
		// mTitle = "导出承包经营权公示表";

		// 4 6 7
		// mTitle = "导出承包归户表,登记薄,经营权合同书";

		// 8
		// mTitle = "导出承包经营权申请书";

		// 11
		// mTitle = "导出承包地块总表";

		// 12
		// mTitle = "导出承包摸底表";

		// 13
		// mTitle = "导出承包台帐";

		setTitle(mTitle);
		setSize(800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);

		// 设置窗体居中
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		int windowWidth = this.getWidth();
		int windowHeight = this.getHeight();
		setLocation(screenWidth / 2 - windowWidth / 2, screenHeight / 2 - windowHeight / 2);
	}

	private void initializeWindow(int w) {
		switch (w) {
		case 1:
			mTitle = "网络分析";
			break;
		}
	}

	/**
	 * 组件设置
	 */
	private void initComponents() {
		// // 模板标签
		// formworkJLabel = new JLabel();
		// formworkJLabel.setSize(60, 12);
		// formworkJLabel.setLocation(40, 20);
		// formworkJLabel.setText("模板路径:");

		// 数据库标签
		dbJLabel = new JLabel();
		dbJLabel.setSize(60, 12);
		dbJLabel.setLocation(40, 25);
		dbJLabel.setText("数据路径:");

		// 输出路径标签
		outputJLabel = new JLabel();
		outputJLabel.setSize(60, 12);
		outputJLabel.setLocation(40, 70);
		outputJLabel.setText("输出路径:");

		// // 模板路径显示区
		// formworkJTextField = new JTextField();
		// formworkJTextField.setSize(400, 25);
		// formworkJTextField.setLocation(110, 15);

		// 数据库路径显示区
		dbJTextField = new JTextField();
		dbJTextField.setSize(400, 25);
		dbJTextField.setLocation(110, 20);

		// 输出路径显示区
		outpuJTextField = new JTextField();
		outpuJTextField.setSize(400, 25);
		outpuJTextField.setLocation(110, 65);

		// // 模板选择按钮
		// formworkJButton = new JButton();
		// formworkJButton.setText("选 择");
		// formworkJButton.setSize(55, 25);
		// formworkJButton.setLocation(520, 15);
		// formworkJButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// btnPath_Click(0);
		// }
		// });

		// 输出选择按钮
		outputJButton = new JButton();
		outputJButton.setText("选 择");
		outputJButton.setSize(55, 25);
		outputJButton.setLocation(520, 65);
		outputJButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnPath_Click(1);
			}
		});

		// 数据库选择按钮
		dbJButton = new JButton();
		dbJButton.setText("选 择");
		dbJButton.setSize(55, 25);
		dbJButton.setLocation(520, 20);
		dbJButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnPath_Click(2);
			}
		});

		// 开始处理按钮
		startJButton = new JButton();
		startJButton.setText("开始");
		startJButton.setSize(85, 85);
		startJButton.setLocation(620, 15);
		startJButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				start();
				startJButton.setEnabled(false);
			}
		});

		// 文件选择
		fchPath = new JFileChooser();
		fchPath.setApproveButtonText("确定");
		fchPath.setCurrentDirectory(FileSystemView.getFileSystemView().getHomeDirectory()); // 文件浏览窗体默认打开桌面

		// 执行日志
		tabFile = new JTable(new DefaultTableModel(null, new String[] { "执行日志" }) {

			private static final long serialVersionUID = -3219008734044564195L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});

		// pnlFile
		pnlFile = new JPanel();
		pnlFile.setLocation(30, 100);
		pnlFile.setSize(700, 400);

		// jspFile
		jspFile = new JScrollPane();
		jspFile.setViewportView(tabFile);
		jspFile.setPreferredSize(pnlFile.getSize());

		sBar = jspFile.getVerticalScrollBar();

		pnlFile.add(jspFile);
		add(pnlFile);

		// add(formworkJLabel);
		add(outputJLabel);
		add(dbJLabel);

		// add(formworkJTextField);
		add(outpuJTextField);
		add(dbJTextField);

		// add(formworkJButton);
		add(outputJButton);
		add(dbJButton);
		add(startJButton);
	}

	/**
	 * 路径选择按钮点击事件
	 * 
	 * @param which
	 */
	private void btnPath_Click(int which) {
		int result = 0;
		result = fchPath.showOpenDialog(this);
		if (JFileChooser.APPROVE_OPTION == result) {
			switch (which) {
			// case 0:
			// formworkJTextField.setText(fchPath.getSelectedFile().getPath());
			// break;

			case 1:
				fchPath.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				outpuJTextField.setText(fchPath.getSelectedFile().getPath());
				break;

			case 2:
				fchPath.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				dbJTextField.setText(fchPath.getSelectedFile().getPath());
				break;
			}
		}
	}

	/**
	 * 开始按钮点击事件
	 */
	private void start() {
		ExecutorService threadPool = Executors.newFixedThreadPool(2);
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					LandApply.main(new String[] { dbJTextField.getText(), outpuJTextField.getText(), });
				} catch (Exception e) {
					inTable(e.toString());
				}
			}
		};
		threadPool.execute(runnable);
	}

	/**
	 * 执行过程中加载日志
	 * 
	 * @param log
	 */
	public static void inTable(String log) {
		// 开始对表格数据进行操作，先得到表模型
		DefaultTableModel dtm = (DefaultTableModel) tabFile.getModel();
		dtm.insertRow(dtm.getRowCount(), new String[] { log });
		sBar.setValue(sBar.getMaximum()); // 设置一个具体位置
	}

	public Window() {
		initLayoutAndProperties();
		initComponents();
	}

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
		// 设置程序UI模式，其实Java也就只自定义了几种，看源代码就能够完全了解
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		new Window().setVisible(true);
	}
}