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

package com.kpi4j.records;

import org.apache.log4j.Logger;

public class IntCounter extends NumericCounter{

	int value=0;
	public static Logger logger=Logger.getLogger("kpi4j");
	
	
	public IntCounter(String name){
		
		this.name=name;
	}
	
	public IntCounter(String name,PerformanceRecord root){
		this.name=name;
		this.root=root;
	}
	
	public IntCounter(PerformanceRecord root){
		this.root=root;
	}
	
	public Object getValue() {
		return value;
	}

	@Override
	public void setCounterValue(Object...input){
		
		this.value=(Integer) input[0];
	}
	@Override
	public synchronized void incrementCounter(Object... input) {
		this.value+=(Integer) input[0];
	}

	@Override
	public void setValue(Object obj) {
		this.value=(Integer) obj;
	}

	

}
