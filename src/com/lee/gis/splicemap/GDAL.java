package com.lee.gis.splicemap;

import java.io.File;

import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;

public class GDAL {
	static void SaveBitmapBuffered(Dataset src, Dataset dst, int x, int y) {
		if (src.getRasterCount() < 3) {
			System.out.println(-1);
		}

		// Get the GDAL Band objects from the Dataset
		Band redBand = src.GetRasterBand(1);
		Band greenBand = src.GetRasterBand(2);
		Band blueBand = src.GetRasterBand(3);

		// Get the width and height of the raster
		int width = redBand.getXSize();
		int height = redBand.getYSize();

		byte[] r = new byte[width * height];
		byte[] g = new byte[width * height];
		byte[] b = new byte[width * height];

		redBand.ReadRaster(0, 0, width, height, 0, width, height, r, 0);
		greenBand.ReadRaster(0, 0, width, height, 0, width, height, g, 0);
		blueBand.ReadRaster(0, 0, width, height, 0, width, height, b, 0);

		Band wrb = dst.GetRasterBand(1);
		wrb.WriteRaster(x * width, y * height, width, height, 0, width, height,
				r, 0);
		Band wgb = dst.GetRasterBand(2);
		wgb.WriteRaster(x * width, y * height, width, height, 0, width, height,
				g, 0);
		Band wbb = dst.GetRasterBand(3);
		wbb.WriteRaster(x * width, y * height, width, height, 0, width, height,
				b, 0);
	}

	// / 拼接瓦片
	public static void CombineTiles(TilesBounds tilesBounds, String tilePath,
			String outPutFileName) {
		if (new File(outPutFileName).exists()) {
			new File(outPutFileName).delete();
		}
		int imageWidth = 256 * (tilesBounds.getMaxCol()
				- tilesBounds.getMinCol() + 1);
		int imageHeight = 256 * (tilesBounds.getMaxRow()
				- tilesBounds.getMinRow() + 1);

		// Register driver(s).
		gdal.AllRegister();
		Driver driver = gdal.GetDriverByName("GTiff");
		Dataset destDataset = driver.Create(outPutFileName, imageWidth,
				imageHeight, 3, gdalconstConstants.GDT_Byte);
		// DataType.GDT_Byte
		for (int col = tilesBounds.getMinCol(); col <= tilesBounds.getMaxCol(); col++) {
			for (int row = tilesBounds.getMinRow(); row <= tilesBounds
					.getMaxRow(); row++) {
				try {
					String sourceFileName = tilePath
							+ tilesBounds.getZoomLevel() + "\\" + col + "\\"
							+ row + ".jpg";
					if (new File(sourceFileName).exists()) {
						Dataset sourceDataset = gdal.Open(sourceFileName,
								gdalconstConstants.GA_ReadOnly);
						if (sourceDataset != null) {
							SaveBitmapBuffered(sourceDataset, destDataset, col
									- tilesBounds.getMinCol(), row
									- tilesBounds.getMinRow());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		// destDataset.Dispose();
	}

	public static void main(String[] args) {
		TilesBounds tilesBounds = new TilesBounds();
		tilesBounds.setMaxCol(856066);
		tilesBounds.setMinCol(856015);
		tilesBounds.setMaxRow(437254);
		tilesBounds.setMinRow(437208);
		tilesBounds.setZoomLevel(20);
		String outPutFileName = "F:/out" + tilesBounds.getZoomLevel() + ".tif";
		String tilePath = "F:/test/谷歌卫星图/";
		CombineTiles(tilesBounds, tilePath, outPutFileName);
	}
}
