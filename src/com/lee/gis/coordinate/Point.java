package com.lee.gis.coordinate;

public class Point {
	/** 经度 单位度 */
	private double longitude;
	/** 纬度 单位度 */
	private double latitude;

	/**
	 * 单位度
	 * 
	 * @param longitude
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * 单位度
	 * 
	 * @param latitude
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * 单位度
	 * 
	 * @return
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * 单位度
	 * 
	 * @return
	 */
	public double getLatitude() {
		return latitude;
	}
}
