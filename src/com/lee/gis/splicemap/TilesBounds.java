package com.lee.gis.splicemap;

public class TilesBounds {

	int minCol, maxCol, minRow, maxRow, zoomlevel;

	

	public int getZoomLevel() {
		return zoomlevel;
	}

	public void setZoomLevel(int zoomlevel) {
		this.zoomlevel = zoomlevel;
	}

	public int getMinCol() {
		return minCol;
	}

	public void setMinCol(int minCol) {
		this.minCol = minCol;
	}

	public int getMaxCol() {
		return maxCol;
	}

	public void setMaxCol(int maxCol) {
		this.maxCol = maxCol;
	}

	public int getMinRow() {
		return minRow;
	}

	public void setMinRow(int minRow) {
		this.minRow = minRow;
	}

	public int getMaxRow() {
		return maxRow;
	}

	public void setMaxRow(int maxRow) {
		this.maxRow = maxRow;
	}

}
