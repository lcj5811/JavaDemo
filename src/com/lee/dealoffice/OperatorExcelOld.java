package com.lee.dealoffice;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;

public class OperatorExcelOld {
	private HSSFWorkbook book;
	private HSSFSheet sheet;
	private HSSFRow row;
	private HSSFCell cell;
	private FileOutputStream out;

	private HSSFCellStyle bigTitleStyle, smallTitleStyle, attributeStyle, normalStyle;

	private int bigTitle, smallTitle, attribute, normal;

	private float height;

	public OperatorExcelOld() {
	}

	public void newBook() {
		book = new HSSFWorkbook();
	}

	public void newSheet(String name) { // 新建一个sheet
		sheet = null;
		sheet = book.createSheet(name);
	}

	public void openBook(String path) {
		try {
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(path));
			book = new HSSFWorkbook(fs);
		} catch (Exception e) {
			e.printStackTrace();
			book = new HSSFWorkbook();
		}
	}

	public boolean openSheet(String name) {
		sheet = null;
		sheet = book.getSheet(name);
		return sheet != null;
	}

	public void merged(int r1, int c1, int r2, int c2) { // 合并单元格
		sheet.addMergedRegion(new CellRangeAddress(r1, r2, c1, c2));
	}

	public void autoSize(int cols) { // 单元格自动调整行宽
		for (int i = 0; i <= cols; ++i) {
			sheet.autoSizeColumn(i);
		}
	}

	public void save(String path) {
		try {
			out = new FileOutputStream(path);
			book.write(out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initCellStyle(int bigTitle, int smallTitle, int attribute, int normal, float height) {
		this.bigTitle = bigTitle;
		this.smallTitle = smallTitle;
		this.attribute = attribute;
		this.normal = normal;
		this.height = height;

		this.initBigTitleStyle();
		this.initSmallTitleStyle();
		this.initAttributeStyle();
		this.initNormalStyle();
	}

	private void initBigTitleStyle() {
		HSSFFont font = book.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) this.bigTitle);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);

		this.bigTitleStyle = book.createCellStyle();
		this.bigTitleStyle.setAlignment(CellStyle.ALIGN_CENTER);
		this.bigTitleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		this.bigTitleStyle.setWrapText(true);
		this.bigTitleStyle.setFont(font);
	}

	private void initSmallTitleStyle() {
		HSSFFont font = book.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) this.smallTitle);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);

		this.smallTitleStyle = book.createCellStyle();
		this.smallTitleStyle.setAlignment(CellStyle.ALIGN_CENTER);
		this.smallTitleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		this.smallTitleStyle.setWrapText(true);
		this.smallTitleStyle.setFont(font);
	}

	private void initAttributeStyle() {
		HSSFFont font = book.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) this.attribute);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);

		this.attributeStyle = book.createCellStyle();
		this.attributeStyle.setAlignment(CellStyle.ALIGN_CENTER);
		this.attributeStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		this.attributeStyle.setWrapText(true);
		this.attributeStyle.setFont(font);

		this.attributeStyle.setBorderBottom((short) 1);
		this.attributeStyle.setBorderLeft((short) 1);
		this.attributeStyle.setBorderRight((short) 1);
		this.attributeStyle.setBorderTop((short) 1);
	}

	private void initNormalStyle() {
		HSSFFont font = book.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) this.normal);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);

		this.normalStyle = book.createCellStyle();
		this.normalStyle.setAlignment(CellStyle.ALIGN_CENTER);
		this.normalStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		this.normalStyle.setWrapText(true);
		this.normalStyle.setFont(font);

		this.normalStyle.setBorderBottom((short) 1);
		this.normalStyle.setBorderLeft((short) 1);
		this.normalStyle.setBorderRight((short) 1);
		this.normalStyle.setBorderTop((short) 1);
	}

	public void write(String value) { // 向cell单元格写入一个值
		cell.setCellValue(value);
	}

	public void write(double value) { // 向cell单元格写入一个值
		this.write("" + value);
	}

	public void write(float value) {
		this.write("" + value);
	}

	public void write(int value) { // 向cell单元格写入一个值
		this.write("" + value);
	}

	public String read() {
		return cell.getStringCellValue();
	}

	public void getBigTitleCell(int i, int j) {
		row = sheet.getRow(i);
		if (row == null) {
			row = sheet.createRow(i);
			row.setHeightInPoints(30);
		}
		cell = row.getCell(j);
		if (cell == null) {
			cell = row.createCell(j);
			cell.setCellStyle(this.bigTitleStyle);
		}
	}

	public void getSmallTitleCell(int i, int j) {
		row = sheet.getRow(i);
		if (row == null) {
			row = sheet.createRow(i);
			row.setHeightInPoints(20);
		}
		cell = row.getCell(j);
		if (cell == null) {
			cell = row.createCell(j);
			cell.setCellStyle(this.smallTitleStyle);
		}
	}

	public void getAttributeCell(int i, int j) {
		row = sheet.getRow(i);
		if (row == null) {
			row = sheet.createRow(i);
			row.setHeightInPoints(this.height);
		}
		cell = row.getCell(j);
		if (cell == null) {
			cell = row.createCell(j);
			cell.setCellStyle(this.attributeStyle);
		}
	}

	public void getAttributeCell(int i, int j, int multiple) {
		row = sheet.getRow(i);
		if (row == null) {
			row = sheet.createRow(i);
			row.setHeightInPoints(this.height * multiple);
		}
		cell = row.getCell(j);
		if (cell == null) {
			cell = row.createCell(j);
			cell.setCellStyle(this.attributeStyle);
		}
	}

	public void getNormalCell(int i, int j) {
		row = sheet.getRow(i);
		if (row == null) {
			row = sheet.createRow(i);
			row.setHeightInPoints(this.height);
		}
		cell = row.getCell(j);
		if (cell == null) {
			cell = row.createCell(j);
			cell.setCellStyle(this.normalStyle);
		}
	}

	public void setLateralPrint() { // 设置水平打印
		HSSFPrintSetup ps = sheet.getPrintSetup();

		ps.setLandscape(true); // 打印方向，true：横向，false：纵向
		ps.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE); // 纸张
		// sheet.setMargin(HSSFSheet.BottomMargin,( double ) 0.5 );// 页边距（下）
		// sheet.setMargin(HSSFSheet.LeftMargin,( double ) 0.1 );// 页边距（左）
		// sheet.setMargin(HSSFSheet.RightMargin,( double ) 0.1 );// 页边距（右）
		// sheet.setMargin(HSSFSheet.TopMargin,( double ) 0.5 );// 页边距（上）
		// sheet.setHorizontallyCenter(true);//设置打印页面为水平居中
		// sheet.setVerticallyCenter(true);//设置打印页面为垂直居中

	}

	public void getBorderCell(int i, int j) {

		row = sheet.getRow(i);
		if (row == null) {
			row = sheet.createRow(i);
			row.setHeightInPoints(this.height);
		}
		cell = row.getCell(j);
		if (cell == null) {
			cell = row.createCell(j);
			cell.setCellStyle(this.normalStyle);
		}

		CellStyle cellStyle = book.createCellStyle();
		cellStyle.cloneStyleFrom(normalStyle);
		cellStyle.setBorderBottom((short) 0); // 下边框
		cellStyle.setBorderLeft((short) 0);// 左边框
		cellStyle.setBorderTop((short) 1);// 上边框
		cellStyle.setBorderRight((short) 0);// 右边框
		cell.setCellStyle(cellStyle);
	}
}
