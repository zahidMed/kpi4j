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

import com.kpi4j.Collector;

public class DimensionRecord extends BranchRecord{

	Object value;
	private static final Logger logger=Logger.getLogger("kpi4j");
	
	
	public DimensionRecord(String name,Class type,Object value){
		this.name=name;
		this.type=type;
		this.value=value;
	}
	
	public DimensionRecord(String name,Class type,Object value,PerformanceRecord root)
	{
		this.name=name;
		this.type=type;
		this.value=value;
		this.root=root;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}


	

	
	
	
}
