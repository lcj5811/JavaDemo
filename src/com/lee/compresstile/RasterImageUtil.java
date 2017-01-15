package com.lee.compresstile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import javax.imageio.ImageIO;

import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;

public class RasterImageUtil {
	private static final RasterImageUtil instance = new RasterImageUtil();

	static {
		System.out.println("GDAL Init...");
		gdal.AllRegister();
	}

	private RasterImageUtil() {
	}

	public static RasterImageUtil getInstance() {
		return instance;
	}

	// 以只读方式读取栅格文件
	private final int eAccess = gdalconstConstants.GA_ReadOnly;
	// 缓存的数据类型为32位的整型数据
	private final int blockType = gdalconstConstants.GDT_Int32;

	/**
	 * GDAL读取影像时的数据集
	 */
	private Dataset dataset = null;// 栅格数据集

	private int bandCount; // band波段数
	private int[] bandList; // 波段值列表

	private int[] blockColor; // 颜色值缓存数组
	private int[] tileColor; // 图片的颜色数组

	// 栅格的左、上、右、底的地理坐标
	public double leftGeog, topGeog, rightGeog, bottomGeog;
	// 栅格文件的宽度和高度
	private int rasterWidth, rasterHeight;
	// 栅格文件在X和Y方向的偏移量、每个像素的宽度和高度
	private double xOffset, yOffset, pixelWidth, pixelHeight;

	public String geoRefer = null;

	// 瓦片图像的后缀
	private final String tileSuffix = "png";
	// 瓦片集文件流和瓦片索引文件流
	private OutputStream tileArrayOutputStream = null;
	private OutputStream tileIndexOutputStream = null;
	// 瓦片的宽和高
	private int tileNormalWidth = SystemConst.tileNormalWidth;
	private int tileNormalHeight = SystemConst.tileNormalHeight;
	// 瓦片的比例
	// private int[] multipleArray = { 1 };
	// 瓦片集文件
	private File tileArrayFile = null;
	private int lastTileSize = 0;

	private int maxMultiple = 0;

	/**
	 * 设置瓦片的宽和高
	 * 
	 * @param tileWidth
	 * @param tileHeight
	 *            void
	 */
	public void setTileWidthAndHeight(int tileWidth, int tileHeight) {
		this.tileNormalWidth = tileWidth;
		this.tileNormalHeight = tileHeight;
	}

	/**
	 * 设置瓦片的比例
	 * 
	 * @param multipleArray
	 *            void
	 */
	public void setMultipleArray(int[] multipleArray) {
		// this.multipleArray = multipleArray;
	}

	/**
	 * 初始化栅格文件
	 * 
	 * @param rasterPath
	 * @return boolean
	 */
	public boolean initRasterImage(String imagePath) {
		if (this.dataset != null) {
			this.destroy();
		}
		this.dataset = gdal.Open(imagePath, eAccess);
		if (this.dataset == null) {
			System.out.println("打开栅格图失败" + gdal.GetLastErrorMsg());
			return false;
		}

		this.bandCount = this.dataset.getRasterCount();
		System.out.println("波段数:" + this.bandCount);

		this.geoRefer = this.dataset.GetProjectionRef();
		if (geoRefer != null) {
			System.out.println("影像的坐标系=" + geoRefer);
		}

		/**
		 * 左上角点坐标(padfGeoTransform[0],padfGeoTransform[3])；
		 * padfGeoTransform[1]是像元宽度(影像在宽度上的分辨率)；
		 * padfGeoTransform[5]是像元高度(影像在高度上的分辨率)；
		 * 如果影像是指北的,padfGeoTransform[2]和padfGeoTransform[4]这两个参数的值为0
		 * 栅格文件上点(P,L)对应的地理坐标(Xp,Yp) Xp=gtf[0]+P*gtf[1]+L*gtf[2]
		 * Yp=gtf[3]+P*gtf[4]+L*gtf[5]
		 */
		double[] gtf = this.dataset.GetGeoTransform();
		leftGeog = gtf[0]; // 栅格的左坐标
		topGeog = gtf[3]; // 栅格的顶部坐标
		pixelWidth = gtf[1]; // 栅格的右坐标
		pixelHeight = gtf[5]; // 栅格的底部坐标
		xOffset = gtf[2]; // 栅格在X轴上的偏移量
		yOffset = gtf[4]; // 栅格在Y轴上的偏移量
		rasterWidth = this.dataset.GetRasterXSize(); // 栅格的宽度
		rasterHeight = this.dataset.getRasterYSize(); // 栅格的高度
		rightGeog = gtf[0] + rasterWidth * pixelWidth + rasterHeight * xOffset;
		bottomGeog = gtf[3] + rasterWidth * yOffset + rasterHeight * pixelHeight;
		System.out.println("rasterLeft=" + leftGeog + " rasterTop=" + topGeog);
		System.out.println("pixelWidth=" + pixelWidth + " pixelHeight=" + pixelHeight);
		System.out.println("xOffset=" + xOffset + " yOffset=" + yOffset);
		System.out.println("rasterWidth=" + rasterWidth + " rasterHeight=" + rasterHeight);

		this.bandList = new int[this.bandCount];
		for (int idBand = 0; idBand < this.bandCount; ++idBand) {
			this.bandList[idBand] = idBand + 1;
		}

		/**
		 * 计算出最大比例的瓦片
		 */
		maxMultiple = Math.min(rasterWidth / tileNormalWidth, rasterHeight / tileNormalHeight);
		if (maxMultiple > 64) {
			maxMultiple = 64;
		}
		return true;
	}

	/**
	 * 将栅格属性写入到瓦片文件流的文件头
	 * 
	 * @param tileWidth
	 * @param tileHeight
	 * @param multiple
	 * @throws IOException
	 *             void
	 */
	public void writeImagePropertyToTileArrayFileHeader() throws IOException {
		System.out.println("向瓦片流集文件头写影像的属性");
		// 写入栅格影像的图像宽度和高度
		FileOperator.writeIntToFile(tileArrayOutputStream, rasterWidth);
		FileOperator.writeIntToFile(tileArrayOutputStream, rasterHeight);
		// 写入栅格影像左上角的坐标
		FileOperator.writeDoubleToFile(tileArrayOutputStream, leftGeog);
		FileOperator.writeDoubleToFile(tileArrayOutputStream, topGeog);
		// 写入栅格影像每个像素的宽度和高度
		FileOperator.writeDoubleToFile(tileArrayOutputStream, pixelWidth);
		FileOperator.writeDoubleToFile(tileArrayOutputStream, pixelHeight);
		// 写入栅格影像在X、Y方向的偏移量
		FileOperator.writeDoubleToFile(tileArrayOutputStream, xOffset);
		FileOperator.writeDoubleToFile(tileArrayOutputStream, yOffset);
		// 写入瓦片文件的宽度和高度、压缩倍数
		FileOperator.writeIntToFile(tileArrayOutputStream, tileNormalWidth);
		FileOperator.writeIntToFile(tileArrayOutputStream, tileNormalHeight);
		// 写入栅格影像的坐标系信息
		if (geoRefer != null) {
			FileOperator.writeByteArraytoFile(tileArrayOutputStream, geoRefer.getBytes());
		}
	}

	/**
	 * 写影像的瓦片流集
	 * 
	 * @param tileNormalWidth
	 * @param tileNormalHeight
	 * @param multiple
	 *            void
	 * @throws Exception
	 */
	public void writeImageTileArray(final int tileNormalWidth, final int tileNormalHeight, final int multiple)
			throws Exception {
		System.out.println("======开始" + multiple + "倍数影像转瓦片程序======");
		// 柵格块的标准大小
		final int blockNormalWidth = (int) (tileNormalWidth * multiple);
		final int blockNormalHeight = (int) (tileNormalHeight * multiple);

		int rowIndex, columnIndex;
		// 行数、列数、总瓦片数
		int rowCount = (rasterHeight - 1) / blockNormalHeight;
		int columnCount = (rasterWidth - 1) / blockNormalWidth;

		System.out.println("rowCount=" + rowCount + " columnCount=" + columnCount);
		// 瓦片对应影像块的左上角坐标、宽度、高度
		int blockXOff, blockYOff, blockWidth, blockHeight;
		// 瓦片的宽度和高度
		int tileWidth, tileHeight;

		int tileIndex = 0;
		int tileCount = (rowCount + 1) * (columnCount + 1);

		long startTime = System.currentTimeMillis();
		for (rowIndex = 0; rowIndex <= rowCount; ++rowIndex) {
			blockYOff = rowIndex * blockNormalHeight;
			if (rowIndex == rowCount) {// 最后一行
				blockHeight = rasterHeight - blockYOff;
			} else {
				blockHeight = blockNormalHeight;
			}
			tileHeight = (int) (blockHeight / multiple);

			for (columnIndex = 0; columnIndex <= columnCount; ++columnIndex) {
				System.out.println("multiple=" + multiple + " rowIndex=" + rowIndex + " columnIndex=" + columnIndex
						+ " rowCount=" + rowCount + " columnCount=" + columnCount);
				blockXOff = columnIndex * blockNormalWidth;
				if (columnIndex == columnCount) { // 最后一列
					blockWidth = rasterWidth - blockXOff;
				} else {
					blockWidth = blockNormalWidth;
				}
				tileWidth = (int) (blockWidth / multiple);
				try {
					writeImageTile(blockXOff, blockYOff, blockWidth, blockHeight, tileWidth, tileHeight);
				} catch (Exception e) {
					throw new Exception(
							e.getMessage() + "->生成" + multiple + "比例" + rowIndex + "行" + columnIndex + "列瓦片出错");
				}
				// RasterImageComponent
				// .setPromptMessage("第" + multiple + "级瓦片已完成" + (++tileIndex) +
				// "张，共" + tileCount + "张瓦片");
				System.out.println("第" + multiple + "级瓦片已完成" + (++tileIndex) + "张，共" + tileCount + "张瓦片");
			}
		}
		System.out.println("共耗时=" + (System.currentTimeMillis() - startTime));
	}

	/**
	 * 写影像一个瓦片流至瓦片流集文件中
	 * 
	 * @param blockXOff
	 * @param blockYOff
	 * @param blockWidth
	 * @param blockHeight
	 * @param tileWidth
	 * @param tileHeight
	 *            void
	 * @throws Exception
	 */
	public void writeImageTile(int blockXOff, int blockYOff, int blockWidth, int blockHeight, int tileWidth,
			int tileHeight) throws Exception {
		System.out.println(blockXOff + " " + blockYOff + " " + blockWidth + " " + blockHeight + " " + tileWidth + " "
				+ tileHeight);
		int tileSize = tileWidth * tileHeight;
		if (tileSize != lastTileSize) {
			this.blockColor = new int[this.bandCount * tileSize];
			lastTileSize = tileSize;
		}

		int ce = this.dataset.ReadRaster(blockXOff, blockYOff, blockWidth, blockHeight, tileWidth, tileHeight,
				blockType, blockColor, bandList);
		if (ce == gdalconstConstants.CE_Failure) {
			System.out.println("加载栅格图数据失败" + gdal.GetLastErrorMsg());
			throw new Exception(gdal.GetLastErrorMsg());
		}

		// 处理灰度图和RGB彩图
		switch (this.bandCount) {
		case 1:
			tileColor = ImageColorConvertor.dealGrayImage(blockColor, tileSize);
			break;
		case 3:
			tileColor = ImageColorConvertor.dealRGBImage(blockColor, tileSize);
			break;
		default:
			break;
		}

		// 保存图片
		BufferedImage image = null;
		try {
			image = new BufferedImage(tileWidth, tileHeight, BufferedImage.TYPE_INT_ARGB_PRE);
			image.setRGB(0, 0, tileWidth, tileHeight, tileColor, 0, tileWidth);
			ImageIO.write(image, "jpg", new File("D:\\"+blockXOff));
			// 将瓦片流保存至瓦片流集文件中
			ImageIO.write(image, tileSuffix, tileArrayOutputStream);
			image.flush();
			// 存储瓦片流后，记录瓦片流在瓦片流集文件中的结束位置
			FileOperator.writeLongToFile(tileIndexOutputStream, tileArrayFile.length());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			image = null;
		}
	}

	/**
	 * 创建影像的缩略图
	 * 
	 * @param bitmapWidth
	 * @param bitmapHeight
	 *            void
	 * @throws Exception
	 */
	public void writeImageScaleBitmap(final int bitmapWidth, final float bitmapHeight) throws Exception {
		System.out.println("writeImageScaleBitmap==写影像的缩略图");
		float inSampleSize = Math.max(rasterWidth / bitmapWidth, rasterHeight / bitmapHeight);
		float blockWidth = bitmapWidth * inSampleSize;
		float blockHeight = bitmapHeight * inSampleSize;
		float tileWidth = bitmapWidth;
		float tileHeight = bitmapHeight;
		if (blockWidth > rasterWidth) {
			tileWidth -= (blockWidth - rasterWidth) / inSampleSize;
			blockWidth = rasterWidth;
		}
		if (blockHeight > rasterHeight) {
			tileHeight -= (blockHeight - rasterHeight) / inSampleSize;
			blockHeight = rasterHeight;
		}
		System.out.println("blockWidth=" + blockWidth + " blockHeight=" + blockHeight);
		System.out.println("tileWidth=" + tileWidth + " tileHeight=" + tileHeight);

		System.out.println("缩略图的位数位置=" + tileArrayFile.length());
		FileOperator.writeLongToFile(tileIndexOutputStream, tileArrayFile.length());
		// 写入一个缩略图的标志
		FileOperator.writeIntToFile(tileArrayOutputStream, 0);

		System.out.println("缩略图的开始位置=" + tileArrayFile.length());
		FileOperator.writeLongToFile(tileIndexOutputStream, tileArrayFile.length());

		writeImageTile(0, 0, (int) blockWidth, (int) blockHeight, (int) tileWidth, (int) tileHeight);
		System.out.println("缩略图的结束位置=" + tileArrayFile.length());
	}

	/**
	 * 将栅格文件转换成瓦片的压缩文件
	 * 
	 * @param tileArrayPath
	 * @param tileWidth
	 * @param tileHeight
	 * @param multiple
	 * @param scaleBitmapWidth
	 * @param scaleBitmapHeight
	 *            void
	 */
	public void imageToTileArray(final String tileArrayPath, final int scaleBitmapWidth, final int scaleBitmapHeight) {
		try {
			// 初始化瓦片流集文件
			FileOperator.createFile(tileArrayPath, true);
			tileArrayFile = new File(tileArrayPath);
			tileArrayOutputStream = new FileOutputStream(tileArrayPath, true);
			// 初始化瓦片索引文件
			int index = tileArrayPath.lastIndexOf(".");
			String tileIndexPath = tileArrayPath.substring(0, index);
			tileIndexPath += SystemConst.tileIndexSuffix;
			FileOperator.createFile(tileIndexPath, true);
			tileIndexOutputStream = new FileOutputStream(tileIndexPath, true);

			// 写入栅格图像的属性数据
			this.writeImagePropertyToTileArrayFileHeader();
			// 将不同比例的瓦片集写入瓦片流集文件中
			// for (int i = 0, size = multipleArray.length; i < size; ++i) {
			for (int multiple = 1; multiple <= maxMultiple; multiple *= 2) {
				// 若瓦片宽和高的倍数都大于影像的宽高，可结束
				if ((tileNormalWidth * multiple > rasterWidth) && (tileNormalHeight * multiple > rasterHeight)) {
					System.out.println("单个瓦片的宽高都已超过影像的宽高");
					continue;
				}

				FileOperator.writeLongToFile(tileIndexOutputStream, tileArrayFile.length());
				// 写入当前的倍数
				FileOperator.writeIntToFile(tileArrayOutputStream, multiple);
				FileOperator.writeLongToFile(tileIndexOutputStream, tileArrayFile.length());
				// 写瓦片集
				this.writeImageTileArray(tileNormalWidth, tileNormalHeight, multiple);
			}
			// 写入缩略图
			this.writeImageScaleBitmap(scaleBitmapWidth, scaleBitmapHeight);
			// 写入文件结束符
			FileOperator.writeEOFToFile(tileArrayOutputStream);
			tileArrayOutputStream.flush();
			FileOperator.writeEOFToFile(tileIndexOutputStream);
			tileIndexOutputStream.flush();

			check(tileArrayPath, tileIndexPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// RasterImageComponent.setPromptMessage(e.getMessage());
		} finally {
			try {
				if (tileIndexOutputStream != null) {
					tileIndexOutputStream.close();
					tileIndexOutputStream = null;
				}
				if (tileArrayOutputStream != null) {
					tileArrayOutputStream.close();
					tileArrayOutputStream = null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// RasterImageComponent.setFinishImage();
		System.out.println("Finish");
	}

	public void check(String tileArrayPath, String tileIndexPath) {
		try {
			RandomAccessFile randomAccessFile = new RandomAccessFile(tileArrayFile, "r");
			long len = randomAccessFile.length();
			randomAccessFile.seek(len - 4);
			byte[] byteArray = new byte[DataTypeConvertor.intByteNum];
			randomAccessFile.read(byteArray);
			System.out.println("tileArrayFile=" + DataTypeConvertor.byteArrayToInt(byteArray));
			if (randomAccessFile != null) {
				randomAccessFile.close();
			}

			randomAccessFile = new RandomAccessFile(tileIndexPath, "r");
			len = randomAccessFile.length();
			randomAccessFile.seek(len - 4);
			byteArray = new byte[DataTypeConvertor.intByteNum];
			randomAccessFile.read(byteArray);
			System.out.println("tileIndexPath=" + DataTypeConvertor.byteArrayToInt(byteArray));
			if (randomAccessFile != null) {
				randomAccessFile.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 当项目结束时，需注销服务
	 */
	public void destroy() {
		this.dataset.delete();
		this.dataset = null;
	}
}
