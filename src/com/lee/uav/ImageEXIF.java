package com.lee.uav;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collection;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.GpsDirectory;
import com.lee.gis.coordinate.UTM;

/**
 * @ClassName com.lee.uav.ImageEXIF
 * @description
 * @author 凌霄
 * @data 2017年8月15日 下午6:21:49
 */
public class ImageEXIF {
	String mFolderPath;

	String originalLoc = "";

	DecimalFormat df = new DecimalFormat("0.000000");

	public ImageEXIF(String mFolderPath) {
		this.mFolderPath = mFolderPath;
	}

	public static void main(String[] args) throws IOException, JpegProcessingException {
		ImageEXIF mImageEXIF = new ImageEXIF("E:\\数据\\GIS_Data\\无人机照片\\测试\\Goettingen_images");
		mImageEXIF.ReadFiles();
		// mImageEXIF.ReadMsgDemo();
	}

	public void ReadFiles() {
		File mFolder = new File(mFolderPath);
		for (File f : mFolder.listFiles()) {
			if (f.isFile()) {
//				f.renameTo(new File(f.getPath().replace(".JPG", "")));
				System.out.print(f.getName().replace(".jpg", "") + " ");
				ReadGPSMsg(f);
			}
		}
	}

	public String ReadGPSMsg(File img) {
		try {
			Metadata metadata = JpegMetadataReader.readMetadata(img);
			Collection<GpsDirectory> gps = metadata.getDirectoriesOfType(GpsDirectory.class);
			for (GpsDirectory gpsDirectory : gps) {
				String XY = UTM.getInstance().LatLon2XYString(gpsDirectory.getGeoLocation().toString());
				if (originalLoc.equals("")) {
					originalLoc = XY;
				}
				System.out.println(calcXY(XY) + " " + gpsDirectory.getDescription(6).replace(" metres", ".000000 ")
						+ "1.000000 0.100000 -0.010000 0.020000");
			}
		} catch (JpegProcessingException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String calcXY(String XY) {
		String xy[] = XY.split(",");
		String orgXY[] = originalLoc.split(",");
		double x = Double.valueOf(xy[0]);
		double y = Double.valueOf(xy[1]);
		double ox = Double.valueOf(orgXY[0]);
		double oy = Double.valueOf(orgXY[1]);
		return df.format((x - ox)) + " " + df.format((y - oy));
	}

	public void ReadMsgDemo() throws JpegProcessingException, IOException {
		File img = new File("E:\\数据\\GIS_Data\\无人机照片\\测试\\Goettingen_images\\DJI_0794.JPG");
		Metadata meta = JpegMetadataReader.readMetadata(img);
		for (Directory dir : meta.getDirectories()) {
			System.out.println(dir);
			System.out.println("— — — — — — — — — — — — —");
			for (Tag tag : dir.getTags()) {
				String tagName = tag.getTagName();
				String description = tag.getDescription();
				System.out.printf("%-30s\t%-100s\n", tagName, description);
			}
			System.out.println("————————————————————————————");
		}
	}

}
