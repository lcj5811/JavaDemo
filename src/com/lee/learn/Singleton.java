package com.lee.learn;

/**
 * @Description 单例模式
 * @Author 凌霄
 * @Date 2016-12-5 下午4:05:38
 */
public class Singleton {

	private Singleton() {
	}

	public static Singleton getInstance() {
		return SingletonHolder.sInstance;
	}

	private static class SingletonHolder {
		private static final Singleton sInstance = new Singleton();
	}

}