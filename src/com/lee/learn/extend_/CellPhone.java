package com.lee.learn.extend_;

/**
 * @ClassName com.lee.learn.CellPhone
 * @description
 * @author 凌霄
 * @data 2016年10月4日 上午11:45:27
 */
public class CellPhone extends Telphone {

	@Override
	public void call() {
		System.out.println("通过键盘打电话");
	}

	@Override
	public void message() {
		System.out.println("通过键盘发短信");
	}

}
