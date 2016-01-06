package com.kpi4j.appender;

import java.util.Collection;
import java.util.Map;

import com.kpi4j.records.PerformanceRecord;


/**
 * Abstract class that that will be implemented by different kind of appenders: File, Database ...
 * The appender role is to store the performance indicators according needed format.
 * @author Mohammed ZAHID <zahid.med@gmail.com>
 *
 */
abstract public class Appender {

	/**
	 * Called after the initialization of kpi4j, this method can be used to initialize the appender
	 * example: open a file or open database connection ....
	 */
	abstract public void initialize();
	
	/**
	 * Called before the application shutdown 
	 */
	abstract public void finalize();
	
	/**
	 * Save performance indicators according to the destination format
	 * @param records
	 */
	abstract public void save(Collection<PerformanceRecord> records);
}
