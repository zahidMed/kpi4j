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

package com.kpi4j.config.xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.security.GeneralSecurityException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.kpi4j.Collector;
import com.kpi4j.CollectorRepository;
import com.kpi4j.Counter;
import com.kpi4j.Dimension;
import com.kpi4j.ObjectType;
import com.kpi4j.appender.Appender;
import com.kpi4j.config.Configurator;
import com.kpi4j.records.BasicRecordFactory;

public class DomComfigurator implements Configurator{

	public static Logger logger=Logger.getLogger("kpi4j");
	
	static final String CONFIG_TAG="config";
	static final String COLLECTOR_TAG="Collector";
	static final String OBJECT_TYPE_TAG="ObjectType";
	static final String COUNTER_TAG="Counter";
	static final String APPENDERS_TAG="Appenders";
	static final String APPENDER_TAG="Appender";
	static final String PARAM_TAG="param";
	static final String DIMENSIONS_TAG="Dimensions";
	static final String DIMENSION_TAG="Dimension";
	static final String VALUE_TAG="Value";
	
	static final String NAME_ATTR="name";
	static final String TYPE_ATTR="type";
	static final String VALUE_ATTR="value";
	static final String CLASS_ATTR="class";
	static final String SCHEDULING_PATTERN="schedulingPattern";
	
	CollectorRepository repository;
	
	
	
	
	public boolean configure(URI uri, CollectorRepository repository) {
		// TODO Auto-generated method stub
		InputStream in=null;
		boolean res=false;
		try {
			in=new FileInputStream(uri.toString());
			res=configure(in, repository);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			logger.error("",e1);
		}
		finally {
			if(in!=null)
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return res;
		
	}
	
	public boolean configure(String uri, CollectorRepository repository) {
		// TODO Auto-generated method stub
		boolean res=false;
		InputStream in=null;
		try {
			in=new FileInputStream(uri);
			res=configure(in, repository);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		finally {
			if(in!=null)
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return res;

		
	}
	
	public boolean configure(InputStream in, CollectorRepository repository) {
		logger.debug("configure kpi4j");
		// TODO Auto-generated method stub
		if(in==null) 
		{
			System.err.println("kpi4j: WARN No config file found: InputStream is null");
			logger.error("kpi4j: WARN No config file found: InputStream is null");
			return false;
		}
		boolean res=false;
		this.repository=repository;
		try {
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
	        Document document = documentBuilder.parse(in);
	        parse(document);
	        res=true;
	        logger.debug("kpi4j configured");
	    } catch (Exception e) {
	    	e.printStackTrace();
	        logger.error("",e);
	    }

		return res;
	}

	protected void parse(Document doc) throws ClassNotFoundException{
		logger.debug("parse kpi4j config file");
		Element config=doc.getDocumentElement();
		NodeList collectors=config.getElementsByTagName(COLLECTOR_TAG);
		if(collectors!=null)
			for(int i=0;i<collectors.getLength();i++){
				parseCollector((Element)collectors.item(i));
			}
	}
	
	
	protected void parseCollector(Element collector) throws ClassNotFoundException{
		logger.debug("parse collector: "+collector.getAttribute(NAME_ATTR));
		NodeList ots=collector.getElementsByTagName(OBJECT_TYPE_TAG);
		String name=collector.getAttribute(NAME_ATTR);
		String schedulingPattern=collector.getAttribute(SCHEDULING_PATTERN);
		Collector coll=new Collector(name);
		coll.setSchedulingPattern(schedulingPattern);
		if(ots!=null)
		{
			for(int i=0;i<ots.getLength();i++)
			{
				ObjectType ot=parseObjectType((Element)ots.item(i));
				coll.addObjectType(ot);
				//coll.addRecord(BasicRecordFactory.createRecord(ot), ot.getName());
			}
		}
		NodeList appenders=collector.getElementsByTagName(APPENDER_TAG);
		if(appenders!=null)
		{
			for(int i=0;i<appenders.getLength();i++)
			{
				Appender ap=parseAppender((Element)appenders.item(i));
				coll.addAppender(ap);
			}
		}
		coll.init();
		repository.addCollector(coll, name);
	}
	
	protected ObjectType parseObjectType(Element app) throws ClassNotFoundException{
		logger.debug("parse ObjectType: "+app.getAttribute(NAME_ATTR));
		ObjectType ot= new ObjectType();
		ot.setName(app.getAttribute(NAME_ATTR));
		NodeList ctrs=app.getElementsByTagName(COUNTER_TAG);
		if(ctrs!=null)
			for(int i=0;i<ctrs.getLength();i++)
			{
				Element ctr=(Element) ctrs.item(i);
				logger.debug("parse Counter: "+ctr.getAttribute(NAME_ATTR));
				Counter counter= new Counter();
				counter.setName(ctr.getAttribute(NAME_ATTR));
				counter.setType(ctr.getAttribute(TYPE_ATTR));
				if(ctr.hasAttribute(VALUE_ATTR))
				{
					if(String.class.equals(counter.getType()))
						counter.setDefaultValue(ctr.getAttribute(VALUE_ATTR));
					else if(Integer.class.equals(counter.getType()))
						counter.setDefaultValue(Integer.parseInt(ctr.getAttribute(VALUE_ATTR)));
					else if(Float.class.equals(counter.getType()))
						counter.setDefaultValue(Float.parseFloat(ctr.getAttribute(VALUE_ATTR)));
					else if(Boolean.class.equals(counter.getType()))
						counter.setDefaultValue(Boolean.parseBoolean(ctr.getAttribute(VALUE_ATTR)));
				}
				ot.addCounter(counter);
			}
		NodeList dms=app.getElementsByTagName(DIMENSION_TAG);
		if(dms!=null)
			for(int i=0;i<dms.getLength();i++){
				Dimension dm=parseDimension((Element) dms.item(i));
				ot.addDimension(dm);
			}
		return ot;
	}
	
	protected Dimension parseDimension(Element app) throws ClassNotFoundException{
		Dimension dim=new Dimension();
		String dimNAme=app.getAttribute(NAME_ATTR);
		String dimType=app.getAttribute(TYPE_ATTR);
		dim.setName(dimNAme);
		dim.setType(dimType);
		NodeList vals=app.getElementsByTagName(VALUE_TAG);
		if(vals!=null)
			for(int i=0;i<vals.getLength();i++)
			{
				String val=vals.item(i).getNodeValue();
				if("integer".equalsIgnoreCase(dimType))
					dim.addValue(Integer.parseInt(val));
				else if("long".equals(dimType))
					dim.addValue(Long.parseLong(val));
				else
					dim.addValue(val);
					
			}
		return dim;
	}

	protected Appender parseAppender(Element app){
		logger.debug("parse Appender: "+app.getAttribute(CLASS_ATTR));
		String className=app.getAttribute(CLASS_ATTR);
		try {
			Appender appendre= (Appender) Class.forName(className).newInstance();
			NodeList params=app.getElementsByTagName(PARAM_TAG);
			if(params!=null)
				for(int i=0;i<params.getLength();i++){
					Element param=(Element) params.item(i);
					Method method=appendre.getClass().getMethod(getAttributeSetter(param.getAttribute(NAME_ATTR)),String.class);
					method.invoke(appendre,param.getAttribute(VALUE_ATTR));
				}
			appendre.initialize();
			return appendre;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("",e);
		} 
		return null;
	}
	
	public String getAttributeSetter(String name){
		if(name==null || name.length()==0)
			throw new IllegalArgumentException("Invalid attribute name");
		StringBuffer buff= new StringBuffer("set");
		for(int i=0;i<name.length();i++)
		{
			if(i==0)
				buff.append(String.valueOf(name.charAt(i)).toUpperCase());
			else
				buff.append(String.valueOf(name.charAt(i)));
			
		}
		return buff.toString();
	}
}
