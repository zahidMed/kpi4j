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
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that represents an ObjectType definition, it reflects 
 * the ObjectType tag in the configuration file
 * @author Mohammed ZAHID <zahid.med@gmail.com>
 *
 */
public class ObjectType
{

	/**
	 * the Object type name
	 */
	String name;
	
	/**
	 * The object type dimensions
	 */
	List<Dimension> dimensions;
	
	/**
	 * The object type counters
	 */
	Map<String,Counter> map;
	
	/**
	 * Retrieve a counter by it's name
	 * @param name: The counter name
	 * @return
	 */
	public Counter getCounter(String name){
		return map.get(name);
	}
	
	/**
	 * Add counter to the object type
	 * @param c
	 */
	public void addCounter(Counter c){
		if(map==null)
			map= new LinkedHashMap<String, Counter>();
		map.put(c.getName(), c);
	}
	
	/**
	 * Add dimension to the object type
	 * @param d
	 */
	public void addDimension(Dimension d){
		if(dimensions==null) 
			dimensions= new ArrayList<Dimension>();
		dimensions.add(d);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<Counter> getCounters() {
		return map.values();
	}

	public List<Dimension> getDimensions() {
		return dimensions;
	}

	public void setDimensions(List<Dimension> dimensions) {
		this.dimensions = dimensions;
	}
	
	
	public int getDepth(){
		return 2+((dimensions!=null)?dimensions.size():0);
	}
	
	
	public boolean checkInput(Object...input)
	{
		if(dimensions==null)
		{
			Counter counter=map.get(input[2]);
			if(counter==null) 
				return false;
			return counter.getType().isInstance(input[3]);
		}
		else
		{
			for(int i=1;i<input.length/2-1;i++){
				Dimension dim=dimensions.get(i-1);
				if(!dim.getName().equals(input[i*2])) 
					return false;
				if(!dim.getType().isInstance(input[i*2+1])) 
					return false;
			}
			Counter counter=map.get(input[input.length-2]);
			if(counter==null) 
				return false;
			return counter.getType().isInstance(input[input.length-1]);
		}
	}

}
