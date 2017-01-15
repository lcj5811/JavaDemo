package com.lee.learn.extend_;

/**
 * @ClassName com.lee.learn.Initail
 * @description
 * @author 凌霄
 * @data 2016年10月4日 上午11:46:53
 */
public class Initail {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Telphone tel1 = new CellPhone();
		tel1.call();
		tel1.message();
		Telphone tel2 = new SmartPhone();
		tel2.call();
		tel2.message();

		IPlayGame ip1 = new SmartPhone();
		ip1.playGame();
		IPlayGame ip2 = new Psp();
		ip2.playGame();

		IPlayGame ip3 = new IPlayGame() {
			@Override
			public void playGame() {
				// TODO Auto-generated method stub
				System.out.println("使用匿名内部类的方式实现接口");
			}
		};
		ip3.playGame();

		new IPlayGame() {
			@Override
			public void playGame() {
				// TODO Auto-generated method stub
				System.out.println("使用匿名内部类的方式实现接口2");
			}
		}.playGame();;
	}

}
