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
import java.util.List;

/**
 * Class that represents a Dimension definition, it reflects 
 * the Dimension tag in the configuration file
 * @author Mohammed ZAHID <zahid.med@gmail.com>
 * @see {@link #ObjectType}
 */
public class Dimension {

	/**
	 * Dimension name
	 */
	String name;
	
	/**
	 * Dimension type: Integer, Long, Double, String ....
	 */
	Class type;
	
	/**
	 * Dimension values
	 */
	List values;
	
	public void addValue(Object val)
	{
		if(values==null) values= new ArrayList();
			values.add(val);
	}
	
	public void setType(String type) throws ClassNotFoundException {
		if("string".equalsIgnoreCase(type))
			this.type = String.class;
		else if("integer".equalsIgnoreCase(type))
			this.type=Integer.class;
		else if("long".equalsIgnoreCase(type))
			this.type=Long.class;
		else if("float".equalsIgnoreCase(type))
			this.type=Float.class;
		else if("boolean".equalsIgnoreCase(type))
			this.type=Boolean.class;
		else
			this.type=Class.forName(type);
	}
	
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
	public List getValues() {
		return values;
	}
	public void setValues(List values) {
		this.values = values;
	}
}
