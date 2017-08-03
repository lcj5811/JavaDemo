package com.lee.gis.dealkml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * @ClassName com.lee.gis.dealkml.DealKml
 * @description 处理kml文件
 * @author 凌霄
 * @data 2017年4月14日 下午10:19:42
 */
public class DealKml {
	String mFilePath, mFileName, mFilePathName;

	public DealKml(String mFilePath, String mFileName) {
		this.mFileName = mFileName;
		this.mFilePath = mFilePath;
		this.mFilePathName = mFilePath + "\\" + mFileName;
	}

	public void writeDocument() throws Exception {
		// 创建SAXReader对象
		SAXReader reader = new SAXReader();
		// 读取文件 转换成Document
		Document document = reader.read(new File(mFilePathName));
		document = writePlacemark(document);
		// 把document写入新文件
		writeDocumentToFile(document);
	}

	public Document writePlacemark(Document d) {
		// 获取根节点元素对象
		Element kml = d.getRootElement();
		Element Document = kml.element("Document");
		Element Folder = Document.element("Folder");
		Element name = Folder.element("name");
		name.clearContent();
		name.setText("20170415");
		d = writePoint(d);
		d = writeLineString(d, false);
		d = writePolygon(d);
		return d;
	}

	public Document writePolygon(Document d) {
		Element kml = d.getRootElement();
		Element Document = kml.element("Document");
		Element Folder = Document.element("Folder");
		Element Placemark = Folder.addElement("Placemark");
		Element name = Placemark.addElement("name");
		name.setText("面");
		Element styleUrl = Placemark.addElement("styleUrl");
		styleUrl.setText("#m_ylw-pushpin");
		Element Polygon = Placemark.addElement("Polygon");
		Element tessellate = Polygon.addElement("tessellate");
		tessellate.setText("1");
		Element outerBoundaryIs = Polygon.addElement("outerBoundaryIs");
		Element LinearRing = outerBoundaryIs.addElement("LinearRing");
		Element coordinates = LinearRing.addElement("coordinates");
		coordinates.setText(
				"112.163864332415,28.6571841735422,0 112.163854226788,28.65718408883114,0 112.1638416631259,28.6571795368998,0 112.1638266082067,28.65717274142871,0 112.1638215901208,28.65717047641763,0 112.1638090976896,28.65716148040188,0 112.1638016581798,28.65715252772715,0 112.1637992713228,28.65714361842661,0 112.1637994100428,28.65713473131743,0 112.1637970234304,28.6571258240451,0 112.1637895863713,28.65711687548578,0 112.1637770999227,28.65710788565927,0 112.163759494333,28.65710329586725,0 112.1637494647878,28.65709876992671,0 112.1637418897468,28.65709870637608,0 112.1637292648106,28.65709860064077,0 112.1637166397527,28.65709849485506,0 112.1637014898341,28.6570983679627,0 112.1636888647297,28.65709826221539,0 112.1636787647452,28.6570981775528,0 112.1636686648462,28.65709809291789,0 112.1636637611552,28.65708916866419,0 112.1636638710728,28.65708250783505,0 112.1636641271754,28.65706696816703,0 112.1636643100145,28.65705587024253,0 112.1636720288739,28.65704705643674,0 112.1636746623742,28.65704042029708,0 112.1636823433865,28.65703382698201,0 112.1636900601317,28.65702501574287,0 112.1636977398796,28.65701842372392,0 112.1637054548957,28.65700961417797,0 112.1637106458119,28.65700078444042,0 112.1637209183056,28.6569897803581,0 112.1637311894614,28.65697877778849,0 112.1637337479991,28.65697658176186,0 112.1637440529096,28.65696336420026,0 112.1637567729066,28.6569568196083,0 112.1637694916516,28.65695027555924,0 112.1637821745436,28.65694594841905,0 112.1637897095255,28.65694160413051,0 112.163804671929,28.65693735728084,0 112.1638196244778,28.65693532176189,0 112.1638296040158,28.65693322596204,0 112.1638345858478,28.65693328507034,0 112.1638495559383,28.65693124717028,0 112.1638595469982,28.65692914935062,0 112.163869542243,28.65692705073398,0 112.1638820177003,28.65692719709608,0 112.1638969961655,28.65692737279709,0 112.1639094847129,28.65692751925784,0 112.1639144042971,28.6569364494497,0 112.1639218237201,28.65694541100512,0 112.1639217654239,28.65695206494953,0 112.1639266670844,28.65696321612865,0 112.1639290694273,28.65697433702442,0 112.1639289720732,28.65698542618349,0 112.1639338742544,28.65699657964795,0 112.1639337962592,28.6570054505256,0 112.1639336985907,28.65701653844573,0 112.1639361205275,28.65702544209207,0 112.1639360032871,28.65703874626957,0 112.1639334058869,28.65704979710402,0 112.1639283292103,28.65705859385604,0 112.1639232344614,28.65706960468189,0 112.1639181602818,28.65707839662808,0 112.1639155656311,28.65708943900908,0 112.1639079965993,28.65709818936854,0 112.1639054035239,28.65710922717018,0 112.1639003339624,28.65711801013039,0 112.1638952656548,28.65712679094987,0 112.1638951888278,28.65713564784576,0 112.1638926360899,28.65714225072246,0 112.1638900259638,28.65715549373483,0 112.1638874045025,28.65716213644198,0 112.1638872706866,28.65717102663607,0 112.163864332415,28.6571841735422,0 ");
		return d;
	}

	public Document writeLineString(Document d, boolean isTrack) {
		Element kml = d.getRootElement();
		Element Document = kml.element("Document");
		Element Folder = Document.element("Folder");
		Element Placemark = Folder.addElement("Placemark");
		Element name = Placemark.addElement("name");
		name.setText("线");
		Element styleUrl = Placemark.addElement("styleUrl");
		String style = isTrack ? "#msn_ylw-pushpin" : "#msn_ylw-pushpin0";
		styleUrl.setText(style);
		Element LineString = Placemark.addElement("LineString");
		Element tessellate = LineString.addElement("tessellate");
		tessellate.setText("1");
		Element coordinates = LineString.addElement("coordinates");
		coordinates.setText(
				"112.1633560839654,28.65715748315017,0 112.1633560799185,28.65717185079246,0 112.1633560758542,28.65718622130728,0 112.1633560724496,28.65719854115739,0 112.1633560666715,28.65721907886364,0 112.1633584009882,28.65722523789911,0 112.163367742904,28.65723755099727,0 112.1633724147891,28.65724165357591,0 112.1633817597688,28.65724574955921,0 112.1633957781516,28.65725189412334,0 112.1634051244671,28.65725599076054,0 112.1634168082149,28.65726213958368,0 112.1634261561655,28.65726829219532,0 112.1634331680333,28.65727444870758,0 112.1634425174339,28.65728060246875,0 112.1634518676297,28.65728675661806,0 112.1634658939822,28.65729496079495,0 112.1634775880075,28.65730933877409,0 112.1634846090088,28.65732372637898,0 112.1634939692732,28.65733811346166,0 112.1634986470303,28.65735459253976,0 112.1635033169957,28.65736697762382,0 112.1635079885652,28.65737936722456,0 112.1635103162769,28.65739591212428,0 112.163512650192,28.65740625296948,0 112.1635149769748,28.65742695622859,0 112.16351496589,28.65744146519121,0 112.1635149579837,28.65745183309723,0 ");
		return d;
	}

	public Document writePoint(Document d) {
		Element kml = d.getRootElement();
		Element Document = kml.element("Document");
		Element Folder = Document.element("Folder");
		Element Placemark = Folder.addElement("Placemark");
		Element name = Placemark.addElement("name");
		name.setText("点");
		Element styleUrl = Placemark.addElement("styleUrl");
		styleUrl.setText("#m_ylw-pushpin0");
		Element Point = Placemark.addElement("Point");
		Element gx = Point.addElement("gx:drawOrder");
		gx.setText("1");
		Element coordinates = Point.addElement("coordinates");
		coordinates.setText("112.1630245703132,28.65740066264356,0");
		return d;
	}

	/**
	 * 将document写入新的文件
	 * 
	 * @param document
	 * @throws Exception
	 */
	private void writeDocumentToFile(Document document) throws Exception {
		// 输出格式
		OutputFormat format = OutputFormat.createPrettyPrint();
		// 设置编码
		format.setEncoding("UTF-8");
		// XMLWriter 指定输出文件以及格式
		XMLWriter writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream(new File(mFilePathName)), "UTF-8"),
				format);
		// 写入新文件
		writer.write(document);
		writer.flush();
		writer.close();
	}

	/**
	 * 创建文件
	 * 
	 * @param mPath
	 *            文件路径
	 * @param mName
	 *            文件名.扩展名
	 */
	public void CopySrcKmlFile() {
		// path表示你所创建文件的路径
		// String path = "d:/tr/rt";
		File f = new File(mFilePath);
		if (!f.exists()) {
			f.mkdirs();
		}
		// fileName表示你创建的文件名；为txt类型；
		// String fileName = "test.txt";
		File file = new File(f, mFileName);
		if (file.exists()) {
			file.delete();
		} else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		String mFileName = "layer.kml";
		String mFilePath = "C:\\Users\\Lee\\Desktop";
		DealKml mDealKml = new DealKml(mFilePath, mFileName);
		mDealKml.writeDocument();
	}

}
