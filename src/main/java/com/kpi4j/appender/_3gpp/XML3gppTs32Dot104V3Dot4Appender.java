/* 
 * Copyright (C) 2016 Mohammed ZAHID (zahid.med@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.kpi4j.Counter;
import com.kpi4j.appender.Appender;
import com.kpi4j.records.DimensionRecord;
import com.kpi4j.records.LeafRecord;
import com.kpi4j.records.OBjectTypeRecord;
import com.kpi4j.records.PerformanceRecord;

/**
 * Extends the Appender in order to save performance indicators in the Telecom 3GPP format 3GPP_TS_32.104_V3.4.
 * @author Mohammed ZAHID <zahid.med@gmail.com>
 *
 */
public class XML3gppTs32Dot104V3Dot4Appender extends Appender {

	DateFormat fdf = new SimpleDateFormat("yyyyMMddHHmm");
	/**
	 * The sender name
	 */
	String sn="";
	
	/**
	 * file format version 
	 */
	String ffv="1";
	
	/**
	 * Sender type
	 */
	String st="";
	
	/**
	 * Vendor name
	 */
	String vn="";
	
	/**
	 * NE user name 
	 */
	String neun="";
	
	/**
	 *  NE distinguished name 
	 */
	String nedn="";

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finalize() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Save performance indicators in the xml format described above
	 */
	@Override
	public void save(Collection<PerformanceRecord> records) {

		for(PerformanceRecord record: records)
		{
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder;
				try {
					
					OBjectTypeRecord otRec = (OBjectTypeRecord) record;
					docBuilder = docFactory.newDocumentBuilder();
					// root elements
					Document doc = docBuilder.newDocument();
					
					Node pi = doc.createProcessingInstruction
					         ("xml-stylesheet", "type=\"text/xsl\" href=\"MeasDataCollection.xsl\"");
					
					Element mdcElement = doc.createElement("mdc");
					mdcElement.setAttribute("xmlns:HTML", "http://www.w3.org/TR/REC-xml");
					doc.insertBefore(pi, mdcElement);
					doc.appendChild(mdcElement);
					
					Element mfhElement= doc.createElement("mfh");
					mfhElement.appendChild(doc.createElement("ffv").appendChild(doc.createTextNode(ffv)));
					mfhElement.appendChild(doc.createElement("sn").appendChild(doc.createTextNode(sn)));
					mfhElement.appendChild(doc.createElement("vn").appendChild(doc.createTextNode(vn)));
					mfhElement.appendChild(doc.createElement("cbt").appendChild(doc.createTextNode(String.valueOf(otRec.getStartTime().getTime()))));
					mdcElement.appendChild(mfhElement);
					
					Element mdElement = doc.createElement("md");
					Element neidElement = doc.createElement("neid");
					neidElement.appendChild(doc.createElement("neun").appendChild(doc.createTextNode(neun)));
					neidElement.appendChild(doc.createElement("nedn").appendChild(doc.createTextNode(nedn)));
					mdElement.appendChild(neidElement);
					Element mi=doc.createElement("mi");
					
					Element mts = doc.createElement("mts");
					mts.appendChild(doc.createTextNode(String.valueOf(otRec.getEndTime().getTime())));
					mi.appendChild(mts);

					Element gp = doc.createElement("gp");
					int duration = (int) ((otRec.getEndTime().getTime() - otRec.getStartTime().getTime()) / 1000);
					gp.appendChild(doc.createTextNode(String.valueOf(duration)));
					mi.appendChild(gp);
					
					int i = 1;
					for (Counter counter : otRec.getObjectType().getCounters()) {
						Element measType = doc.createElement("mt");
						measType.appendChild(doc.createTextNode(counter.getName()));
						mi.appendChild(measType);
					}
					parseRecord(otRec, mi, doc, null);

//						if(sd==null)
//						{
//							sd=((OBjectTypeRecord) record).getStartTime();
//							ed=((OBjectTypeRecord) record).getEndTime();
//						}
						//parseRecord((OBjectTypeRecord) record, mdcElement, doc);
						
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					Transformer transformer = transformerFactory.newTransformer();
					transformer.setOutputProperty(OutputKeys.INDENT, "yes");
					transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
					DOMSource source = new DOMSource(doc);
					String fileName=otRec.getName()+"_"+fdf.format(otRec.getStartTime())+"_"+fdf.format(otRec.getEndTime())+".xml";
					StreamResult result = new StreamResult(new File(fileName));
					transformer.transform(source, result);
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TransformerConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	
	void parseRecord(PerformanceRecord rec, Element parent, Document doc, String moid) {
		if (rec instanceof OBjectTypeRecord) {
			OBjectTypeRecord record = (OBjectTypeRecord) rec;
			for (PerformanceRecord subRec : record.getChildren().values()) {
				parseRecord(subRec, parent, doc, moid);
			}
			
		} else if (rec instanceof DimensionRecord) {
			DimensionRecord record = (DimensionRecord) rec;
			if (moid!=null)
				moid+=","+record.getName() +"="+ record.getValue();
			else
				moid=record.getName() +"="+ record.getValue();
			
			Element measValue=null;
			boolean isLastDim=false;
			for (PerformanceRecord subRec : record.getChildren().values()) {
				isLastDim=(subRec instanceof LeafRecord);
				if(isLastDim && measValue==null)
				{ 
					measValue = doc.createElement("measValue");
					measValue.setAttribute("measObjLdn", moid);
					parent.appendChild(measValue);
				}
				parseRecord(subRec,(isLastDim)?measValue:parent, doc, moid);
			}
		} else if (rec instanceof LeafRecord) {
			Element valElement = doc.createElement("r");
			valElement.appendChild(doc.createTextNode(String.valueOf(rec.getValue())));
			parent.appendChild(valElement);
		}
	}
	
	
	
}
