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
