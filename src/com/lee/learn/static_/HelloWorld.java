package com.lee.learn.static_;

/**
 * @ClassName com.lee.learn.Exception.HelloWorld
 * @description
 * @author 凌霄
 * @data 2016年10月5日 上午11:35:56
 */
public class HelloWorld {

	String name; // 声明变量name
	String sex; // 声明变量sex
	static int age;// 声明静态变量age

	// 构造方法
	public HelloWorld() {
		System.out.println("通过构造方法初始化name");
		name = "tom";
	}

	// 初始化块
	{
		System.out.println("通过初始化块初始化sex");
		sex = "男";
	}

	// 静态初始化块
	static {
		System.out.println("通过静态初始化块初始化age");
		age = 20;
	}

	public void show() {
		System.out.println("姓名：" + name + "，性别：" + sex + "，年龄：" + age);
	}

	public static void main(String[] args) {

		// 创建对象
		HelloWorld hello = new HelloWorld();
		// 调用对象的show方法
		hello.show();
	}
}
