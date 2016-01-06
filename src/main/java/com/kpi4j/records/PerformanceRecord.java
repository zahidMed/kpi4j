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

public abstract class PerformanceRecord {

	
	protected String name;
	protected Class type;
	
	PerformanceRecord root;
	
	
	public abstract Object getValue();
	public abstract void setValue(Object obj);
	public abstract void setCounterValue(Object...input);
	public abstract void incrementCounter(Object...input);
	//public abstract void incrementCounter(String name, Number value);
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Class getType() {
		return type;
	}
	public void setType(Class type) {
		this.type = type;
	}
	
	public void setRoot(PerformanceRecord root) {
		this.root = root;
	}
	public PerformanceRecord getRoot() {
		return root;
	}
}
