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

package com.kpi4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.kpi4j.appender.Appender;
import com.kpi4j.records.BasicRecordFactory;
import com.kpi4j.records.OBjectTypeRecord;
import com.kpi4j.records.PerformanceRecord;

/**
 * This is the central class in the kpi4j package. Most performance monitoring operations, except configuration,
 *  are done through this class
 * @author Mohammed ZAHID <zahid.med@gmail.com>
 *
 */
public class Collector {

	public static Logger logger=Logger.getLogger("kpi4j");
	
	static CollectorRepository collectorRepository;
	
	Map<String,ObjectType> objectTypes;
	
	List<Appender> appenders;
	
	String schedulingPattern;
	
	Map<String,PerformanceRecord> records;// = new LinkedHashMap<String, PerformanceRecord>();
	
	boolean initRecords=false;
	
	Object lock= new Object();
	
	String name;
	
	

	
	
	public Collector(String name)
	{
		this.name=name;
	}
	
	/**
	 * Initialize the records defined in the configuration file at the application startup
	 */
	public void init()
	{
		logger.debug("init record objects");
		for(ObjectType ot: objectTypes.values())
			addRecord(BasicRecordFactory.createRecord(ot));

		
	}
	
	/**
	 * Retrieve the performance records and initialize them
	 * @return PerformanceRecord records
	 */
	public Collection<PerformanceRecord> initRecordValues()
	{
		logger.debug("create new record objects");
		initRecords=true;
		Map<String,PerformanceRecord> old=records;
		synchronized(lock)
		{
			
				records= Collections.synchronizedMap(new LinkedHashMap<String, PerformanceRecord>());
			for(ObjectType ot: objectTypes.values())
				addRecord(BasicRecordFactory.createRecord(ot));
			initRecords=false;
			lock.notifyAll();
		}
		for(PerformanceRecord rec: old.values()){
			((OBjectTypeRecord)rec).setEndTime(new Date());
		}
		return old.values();
	}

	/**
	 * addd an OBjectTypeRecord to the records MAP
	 * @param pr
	 */
    void addRecord(OBjectTypeRecord pr){
    	if(records==null) records = new LinkedHashMap<String, PerformanceRecord>();
    	records.put(pr.getName(), pr);
    }
	
    /**
     * Get a Collector by name
     * @param name: the collector name
     * @return the collector
     * @see {@link CollectorRepository#getCollector(String)}
     */
	public static synchronized Collector getCollector(String name){
		if(collectorRepository==null) collectorRepository=BasicCollectorRepository.getCollectorRepository();
		return collectorRepository.getCollector(name);
	}
	
	
	/**
	 * Add an object type
	 * @param ot
	 */
	public void addObjectType(ObjectType ot){
		if(objectTypes==null) objectTypes= new LinkedHashMap<String, ObjectType>();
		objectTypes.put(ot.getName(),ot);
	}
	
	
	/**
	 * Add Appender to the current Collector
	 * @param ot
	 */
	public void addAppender(Appender ot){
		if(appenders==null) appenders= new ArrayList<Appender>();
		appenders.add(ot);
	}
	

	/**
	 * Increase counter value, this method is called for numeric counters
	 * @param input: the counter path and value. example
	 * incrementCounter("ObjectType",OT_NAME,"Dim1",DIMENSION_NAME,...,"COUNTER_NAME",COUNTER_VALUE);
	 */
	public void incrementCounter(Object... input){
		if(records==null) records = new LinkedHashMap<String, PerformanceRecord>();
		if(checkInput(input))
		{
			synchronized(lock)
			{
				while(initRecords)
					try {
						lock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			OBjectTypeRecord otRecord=(OBjectTypeRecord)records.get(input[1]);
			otRecord.incrementCounter(Arrays.copyOfRange(input, 2, input.length));
		}
		else
			logger.error("Invalid input data: "+Arrays.toString(input));
	}
	

	/**
	 * Set the counter value, this method is called for all the counters
	 * @param input: the counter path and value. example
	 * incrementCounter("ObjectType",OT_NAME,"Dim1",DIMENSION_NAME,...,"COUNTER_NAME",COUNTER_VALUE);
	 */
	public void setCounterValue(Object... input){
		if(records==null) records = new LinkedHashMap<String, PerformanceRecord>();
		if(checkInput(input)){
			synchronized(lock)
			{
				while(initRecords)
					try {
						lock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			OBjectTypeRecord otRecord=(OBjectTypeRecord) records.get(input[1]);
			otRecord.setCounterValue(Arrays.copyOfRange(input, 2, input.length));
			
		}
		else
			logger.error("Invalid input data: "+Arrays.toString(input));
	}
	
	/**
	 * Check the input array of the method increaseCounter and setCounterValue according to the
	 * ObjectType definition in the configuration file. 
	 * @param input
	 * @return
	 */
	boolean checkInput(Object... input){
		if(input==null || input.length%2==1) return false;
		ObjectType ot=objectTypes.get(input[1]);
		if(ot==null) return false;
		if(ot.getDepth()*2!=input.length) return false;
		return ot.checkInput(input);
	}




	public void setSchedulingPattern(String schedulingPattern) {
		this.schedulingPattern = schedulingPattern;
	}

	public List<Appender> getAppenders() {
		return appenders;
	}

	public void setAppenders(List<Appender> appenders) {
		this.appenders = appenders;
	}

	public String getSchedulingPattern() {
		return schedulingPattern;
	}
	
	public String getName() {
		return name;
	}
	
	public Map<String, PerformanceRecord> getRecords() {
		return records;
	}

	public void setRecords(Map<String, PerformanceRecord> realRecords) {
		this.records = realRecords;
	}

	
	
	public PerformanceRecord getOBjectTypeRecord(String name){
		return records.get(name);
	}
}
