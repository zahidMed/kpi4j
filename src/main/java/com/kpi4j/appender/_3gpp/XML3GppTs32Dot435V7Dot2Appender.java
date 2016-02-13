package com.kpi4j.appender._3gpp;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.kpi4j.Counter;
import com.kpi4j.ObjectType;
import com.kpi4j.appender.Appender;
import com.kpi4j.records.DimensionRecord;
import com.kpi4j.records.LeafRecord;
import com.kpi4j.records.OBjectTypeRecord;
import com.kpi4j.records.PerformanceRecord;

/**
 * Extends the Appender in order to save performance indicators in the Telecom 3GPP format 3GPP_TS_32.435 V7.2.0
 * This appender creates a statistic file for every Object type
 * @see https://www.etsi.org/deliver/etsi_ts/132400_132499/132435/07.02.00_60/ts_132435v070200p.pdf
 * @author Mohammed ZAHID <zahid.med@gmail.com>
 *
 */
public class XML3GppTs32Dot435V7Dot2Appender extends Appender {

	private static final Logger logger=Logger.getLogger("kpi4j");
	
	String fileFormatVersion = "32.435 V7.2.0";

	String vendorName;

	String dnPrefix;

	String localDn;

	String elementType;

	String userLabel;

	String swVersion;

	String directory;

	DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	DateFormat fdf = new SimpleDateFormat("yyyyMMddHHmm");

	@Override
	public void initialize(Collection<ObjectType> objectTypes) {

	}

	@Override
	public void finalize() {

	}

	@Override
	public void save(Collection<PerformanceRecord> records) {

		for (PerformanceRecord record : records) {
			try {
				OBjectTypeRecord otRec = (OBjectTypeRecord) record;
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder;
				docBuilder = docFactory.newDocumentBuilder();
				// root elements
				Document doc = docBuilder.newDocument();

				Node pi = doc.createProcessingInstruction("xml-stylesheet",
						"type=\"text/xsl\" href=\"MeasDataCollection.xsl\"");

				Element measCollecFile = doc.createElement("measCollecFile");
				measCollecFile.setAttribute("xmlns",
						"http://www.3gpp.org/ftp/specs/archive/32_series/32.435#measCollec");
				doc.appendChild(measCollecFile);
				doc.insertBefore(pi, measCollecFile);
				

				Element fileHeader = doc.createElement("fileHeader");
				fileHeader.setAttribute("fileFormatVersion", fileFormatVersion);
				fileHeader.setAttribute(vendorName, vendorName);
				fileHeader.setAttribute("dnPrefix", dnPrefix);
				measCollecFile.appendChild(fileHeader);

				Element fileSender = doc.createElement("fileSender");
				fileSender.setAttribute("localDn", localDn);
				fileSender.setAttribute("elementType", elementType);
				fileHeader.appendChild(fileSender);

				Element measCollec = doc.createElement("measCollec");
				measCollec.setAttribute("beginTime", df.format(otRec.getStartTime()));
				fileHeader.appendChild(measCollec);

				Element measData = doc.createElement("measData");
				measCollecFile.appendChild(measData);

				Element managedElement = doc.createElement("managedElement");
				managedElement.setAttribute("localDn", localDn);
				managedElement.setAttribute("userLabel", userLabel);
				managedElement.setAttribute("swVersion", swVersion);
				measData.appendChild(managedElement);

				Element measInfo = doc.createElement("measInfo");
				measCollecFile.appendChild(measInfo);

				Element granPeriod = doc.createElement("granPeriod");
				int duration = (int) ((otRec.getEndTime().getTime() - otRec.getStartTime().getTime()) / 1000);
				granPeriod.setAttribute("duration", "PT" + duration + "S");
				granPeriod.setAttribute("endTime",  df.format(otRec.getEndTime()));
				measInfo.appendChild(granPeriod);

				Element repPeriod = doc.createElement("repPeriod");
				repPeriod.setAttribute("duration", "PT" + duration + "S");
				measInfo.appendChild(repPeriod);

				int i = 1;
				for (Counter counter : otRec.getObjectType().getCounters()) {
					Element measType = doc.createElement("measType");
					measType.setAttribute("p", String.valueOf(i++));
					measType.appendChild(doc.createTextNode(counter.getName()));
					measInfo.appendChild(measType);
				}
				parseRecord(otRec, measInfo, doc, null, null);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
				DOMSource source = new DOMSource(doc);
				String fileName = directory + "/statistics_" + otRec.getObjectType().getName() + "_"
						+ fdf.format(otRec.getStartTime()) + "_" + fdf.format(otRec.getEndTime()) + ".xml";
				StreamResult result = new StreamResult(new File(fileName));
				transformer.transform(source, result);
			} catch (ParserConfigurationException e) {
				logger.error("Error while saving statistics", e);
			} catch (TransformerConfigurationException e) {
				logger.error("Error while saving statistics", e);
			} catch (TransformerException e) {
				logger.error("Error while saving statistics", e);
			}
		}

	}

	/**
	 * Recursive method that creates the measValue with the counters values,
	 * It creates also the measObjLdn value from the different dimension.
	 * @param rec
	 * @param parent
	 * @param doc
	 * @param moid
	 * @param index
	 */
	void parseRecord(PerformanceRecord rec, Element parent, Document doc, String moid, Integer index) {
		if (rec instanceof OBjectTypeRecord) {
			OBjectTypeRecord record = (OBjectTypeRecord) rec;
			for (PerformanceRecord subRec : record.getChildren().values()) {
				parseRecord(subRec, parent, doc, moid, null);
			}
			
		} else if (rec instanceof DimensionRecord) {
			DimensionRecord record = (DimensionRecord) rec;
			if (moid!=null)
				moid+=","+record.getName() +"="+ record.getValue();
			else
				moid=record.getName() +"="+ record.getValue();
			
			int i = 1;
			Element measValue=null;
			boolean isLastDim=false;
			for (PerformanceRecord subRec : record.getChildren().values()) {
				isLastDim=subRec instanceof LeafRecord;
				if(isLastDim && measValue==null)
				{ 
					measValue = doc.createElement("measValue");
					measValue.setAttribute("measObjLdn", moid);
					parent.appendChild(measValue);
				}
				parseRecord(subRec,isLastDim?measValue:parent, doc, moid, i++);
			}
		} else if (rec instanceof LeafRecord) {
			Element valElement = doc.createElement("r");
			valElement.setAttribute("p", String.valueOf(index));
			valElement.appendChild(doc.createTextNode(String.valueOf(rec.getValue())));
			parent.appendChild(valElement);
		}
	}
	
	
	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getDnPrefix() {
		return dnPrefix;
	}

	public void setDnPrefix(String dnPrefix) {
		this.dnPrefix = dnPrefix;
	}

	public String getLocalDn() {
		return localDn;
	}

	public void setLocalDn(String localDn) {
		this.localDn = localDn;
	}

	public String getElementType() {
		return elementType;
	}

	public void setElementType(String elementType) {
		this.elementType = elementType;
	}

	public String getUserLabel() {
		return userLabel;
	}

	public void setUserLabel(String userLabel) {
		this.userLabel = userLabel;
	}

	public String getSwVersion() {
		return swVersion;
	}

	public void setSwVersion(String swVersion) {
		this.swVersion = swVersion;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

}
