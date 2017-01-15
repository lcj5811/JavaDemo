package com.lee.learn.extend_;

/**
 * @ClassName com.lee.learn.SmartPhone
 * @description
 * @author 凌霄
 * @data 2016年10月4日 上午11:42:24
 */
public class SmartPhone extends Telphone implements IPlayGame {

	@Override
	public void call() {
		System.out.println("通过语音来打电话");
	}

	@Override
	public void message() {
		System.out.println("通过语音来发短信");
	}

	@Override
	public void playGame() {
		System.out.println("具有玩游戏的功能");

	}

}
