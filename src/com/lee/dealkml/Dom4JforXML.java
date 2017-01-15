package com.lee.dealkml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class Dom4JforXML {

	public static void main(String[] args) {
		Dom4JforXML demo = new Dom4JforXML();
		try {
			demo.test();
			demo.test2();
			demo.test4();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void test() throws Exception {
		// 创建SAXReader对象
		SAXReader reader = new SAXReader();
		// 读取文件 转换成Document
		Document document = reader.read(new File("C:\\Users\\Lee\\Desktop\\dom4jForJavaXML\\dom4j\\s.xml"));
		// 获取根节点元素对象
		Element root = document.getRootElement();
		// 遍历
		listNodes(root);
	}

	// 遍历当前节点下的所有节点
	public void listNodes(Element node) {
		System.out.println("当前节点的名称：" + node.getName());
		// 首先获取当前节点的所有属性节点
		List<Attribute> list = node.attributes();
		// 遍历属性节点
		for (Attribute attribute : list) {
			System.out.println("属性" + attribute.getName() + ":" + attribute.getValue());
		}
		// 如果当前节点内容不为空，则输出
		if (!(node.getTextTrim().equals(""))) {
			System.out.println(node.getName() + "：" + node.getText());
		}
		// 同时迭代当前节点下面的所有子节点
		// 使用递归
		Iterator<Element> iterator = node.elementIterator();
		while (iterator.hasNext()) {
			Element e = iterator.next();
			listNodes(e);
		}
	}

	// 操作属性节点
	public void test2() throws Exception {
		// 创建SAXReader对象
		SAXReader reader = new SAXReader();
		// 读取文件 转换成Document
		Document document = reader.read(new File("C:\\Users\\Lee\\Desktop\\dom4jForJavaXML\\dom4j\\s.xml"));
		// 获取根节点元素对象
		Element root = document.getRootElement();

		System.out.println("-------添加属性前------");
		// 获取节点student1
		Element student1Element = root.element("student1");
		// 遍历
		listNodes(student1Element);
		// 获取其属性
		Attribute idAttribute = student1Element.attribute("id");
		// 删除其属性
		student1Element.remove(idAttribute);
		// 为其添加新属性
		student1Element.addAttribute("name", "这是student1节点的新属性");
		System.out.println("-------添加属性后------");
		listNodes(student1Element);
	}

	// 添加节点
	public void test3() throws Exception {
		// 创建SAXReader对象
		SAXReader reader = new SAXReader();
		// 读取文件 转换成Document
		Document document = reader.read(new File("C:\\Users\\Lee\\Desktop\\dom4jForJavaXML\\dom4j\\s.xml"));
		// 获取根节点元素对象
		Element root = document.getRootElement();
		System.out.println("-------添加节点前------");
		// 获取节点student1
		Element student1Element = root.element("student1");
		// 遍历
		listNodes(student1Element);
		// 添加phone节点
		Element phoneElement = student1Element.addElement("phone");
		// 为phone节点设置值
		phoneElement.setText("137xxxxxxxx");
		System.out.println("-------添加节点后------");
		listNodes(student1Element);
	}

	// 添加节点后，写入新的文件
	public void test4() throws Exception {
		// 创建SAXReader对象
		SAXReader reader = new SAXReader();
		// 读取文件 转换成Document
		Document document = reader.read(new File("C:\\Users\\Lee\\Desktop\\dom4jForJavaXML\\dom4j\\s.xml"));
		// 获取根节点元素对象
		Element root = document.getRootElement();
		System.out.println("-------添加节点前------");
		// 获取节点student1
		Element student1Element = root.element("student1");
		// 遍历
		listNodes(student1Element);
		// 添加phone节点
		Element phoneElement = student1Element.addElement("phone");
		// 为phone节点设置值
		phoneElement.setText("137xxxxxxxx");
		System.out.println("-------添加节点后------");
		listNodes(student1Element);
		// 把student1Element写入新文件
		writerDocumentToNewFile(document);
		System.out.println("---写入完毕----");
	}

	// document写入新的文件
	public void writerDocumentToNewFile(Document document) throws Exception {
		// 输出格式
		OutputFormat format = OutputFormat.createPrettyPrint();
		// 设置编码
		format.setEncoding("UTF-8");
		// XMLWriter 指定输出文件以及格式
		XMLWriter writer = new XMLWriter(new OutputStreamWriter(
				new FileOutputStream(new File("C:\\Users\\Lee\\Desktop\\dom4jForJavaXML\\dom4j\\s1.xml")), "UTF-8"),
				format);

		// 写入新文件
		writer.write(document);
		writer.flush();
		writer.close();
	}
}
