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

import java.util.Collection;

import com.kpi4j.ObjectType;
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
	public abstract void initialize(Collection<ObjectType> objectTypes);
	
	/**
	 * Called before the application shutdown 
	 */
	public abstract void finalize();
	
	/**
	 * Save performance indicators according to the destination format
	 * @param records
	 */
	public abstract void save(Collection<PerformanceRecord> records);
}
