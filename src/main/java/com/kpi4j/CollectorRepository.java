package com.kpi4j;

import java.util.Map;

/**
 * The collector repository is used to create and retrieve Collectors.
 * The implementation of this class should be singleton
 * @author Mohammed ZAHID <zahid.med@gmail.com>
 *
 */
public abstract class CollectorRepository {

	/**
	 * Collectors map
	 */
	Map<String,Collector> collectors;
	
	/**
	 * indicates if the the collector repository was initialized or no
	 */
	boolean initialized=false;

	/**
	 * Get collector by name, if not defined in the configuration file, the method instantiates
	 * and returns an empty Collector to not cause NullPointerException in the code execution.
	 * @param name: the collector name
	 * @return
	 */
	public abstract Collector getCollector(String name);
	
	/**
	 * Add collector to the Collector repository.
	 * @param coll
	 * @param name
	 */
	public abstract void addCollector(Collector coll,String name);
	public abstract void setInitialized(boolean initialized);

}
