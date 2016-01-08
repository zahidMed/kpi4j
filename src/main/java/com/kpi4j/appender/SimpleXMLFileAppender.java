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

package com.kpi4j.appender;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

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

import com.kpi4j.records.DimensionRecord;
import com.kpi4j.records.LeafRecord;
import com.kpi4j.records.OBjectTypeRecord;
import com.kpi4j.records.PerformanceRecord;

/**
 * Extends the Appender in order to save performance indicators in a simple XML format.
 * <Statistics>
 *	<ObjectType name="">
 *		<StartDate></StartDate>
 *		<EndDate></EndDate>
 *		<Dimention value="">
 *			<Counter></Counter>
 *		</Dimention>
 *	</ObjectType>
 * </Statistics>
 * @author Mohammed ZAHID <zahid.med@gmail.com>
 *
 */
public class SimpleXMLFileAppender extends Appender{

	/**
	 * file name
	 */
	String file;
	
	/**
	 * Date time format
	 */
	DateFormat df= new SimpleDateFormat("yyyyMMddHHmm");

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
		// TODO Auto-generated method stub
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("Statistics");
			doc.appendChild(rootElement);
			Date sd = null;
			Date ed=null;
			for(PerformanceRecord record: records)
			{
//				if(sd==null)
//				{
//					sd=((OBjectTypeRecord) record).getStartTime();
//					ed=((OBjectTypeRecord) record).getEndTime();
//				}
				parseRecord((OBjectTypeRecord) record, rootElement, doc);
				
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(doc);
			String fileName=file+"_"+df.format(sd)+"_"+df.format(ed)+".xml";
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
	
	/**
	 * Recursive method that parses performance records and save them in xml document. 
	 * @param rec: performance record
	 * @param parent: the parent element to which the new elements belong 
	 * @param doc: XML document where the tag will be added
	 * @param st: performance counters start date and time
	 * @param ed: performance counters end date and time
	 */
	private void parseRecord(PerformanceRecord rec,Element parent,Document doc){
		if(rec instanceof OBjectTypeRecord){
			OBjectTypeRecord record= (OBjectTypeRecord) rec;
			Element otElement = doc.createElement("ObjectType");
			otElement.setAttribute("name", record.getName());
			Element sdElement = doc.createElement("StartDate");
			sdElement.appendChild(doc.createTextNode(df.format(record.getStartTime())));
			otElement.appendChild(sdElement);
			Element edElement = doc.createElement("EndDate");
			edElement.appendChild(doc.createTextNode(df.format(record.getEndTime())));
			otElement.appendChild(edElement);
			for(PerformanceRecord subRec: record.getChildren().values()){
				parseRecord(subRec, otElement, doc);
			}
			parent.appendChild(otElement);
		}
		else if(rec instanceof DimensionRecord){
			DimensionRecord record= (DimensionRecord) rec;
			Element dimElement = doc.createElement(record.getName());
			dimElement.setAttribute("value", String.valueOf(record.getValue()));
			parent.appendChild(dimElement);
			for(PerformanceRecord subRec: record.getChildren().values()){
				parseRecord(subRec, dimElement, doc);
			}
		}
		else if(rec instanceof LeafRecord){
			Element valElement = doc.createElement(rec.getName());
			valElement.appendChild(doc.createTextNode(String.valueOf(rec.getValue())));
			parent.appendChild(valElement);
		}
	}
	
	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
	
}
