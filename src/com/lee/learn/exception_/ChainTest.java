package com.lee.learn.exception_;

public class ChainTest {

	/**
	 * test1():抛出“喝大了”异常
	 * test2():调用test1(),捕获“喝大了”异常，并且包装成运行时异常，继续抛出
	 * main方法中，调用test2(),尝试捕获test2()方法抛出的异常
	 */
	public static void main(String[] args) {
		ChainTest ct = new ChainTest();
		try{
			ct.test2();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void test1() throws DrunkException{
		throw new DrunkException("喝车别开酒！");
	}
	
	public void test2(){
		try {
			test1();
		} catch (DrunkException e) {
			// TODO Auto-generated catch block
			RuntimeException newExc = 
				new RuntimeException(e);
//			newExc.initCause(e);
			throw newExc;
		}
	}
}
