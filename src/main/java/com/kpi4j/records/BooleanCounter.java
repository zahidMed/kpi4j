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

public class BooleanCounter extends LeafRecord{

	boolean value=false;
	private static final Logger logger=Logger.getLogger("kpi4j");
	
	public BooleanCounter(String name){
		this.name=name;
	}
	
	public BooleanCounter(String name,PerformanceRecord root){
		this.name=name;
		this.root=root;
	}
	
	public BooleanCounter(PerformanceRecord root){
		this.root=root;
	}
	
	public Object getValue() {
		return value;
	}
	

	@Override
	public void setCounterValue(Object...input){
		this.value=(Boolean) input[0];
	}

	@Override
	public void incrementCounter(Object... input) {
		logger.info("Boolean counter cannot be incremented");
	}

	@Override
	public void setValue(Object obj) {
		this.value=(Boolean) obj;
	}

	


}
