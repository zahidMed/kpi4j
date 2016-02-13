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

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.kpi4j.Collector;
import com.kpi4j.Counter;

public abstract class BranchRecord extends PerformanceRecord{

	private static final Logger logger=Logger.getLogger("kpi4j");
	
	Map<Object,PerformanceRecord> children= Collections.synchronizedMap(new LinkedHashMap<Object, PerformanceRecord>());
	
	String childName;
	
	@Override
	public void incrementCounter(Object...input) {

		if(input.length==2)
		{
			PerformanceRecord record=children.get(input[0]);
			if(record==null)
			{
				for(Counter counter :((OBjectTypeRecord)root).getObjectType().getCounters())
				{
					LeafRecord lfr=null;
					if(String.class.equals(counter.getType()))
						lfr= new StringCounter(counter.getName(),root);
					else if(Integer.class.equals(counter.getType()))
						lfr= new IntCounter(counter.getName(),root);
					else if(Long.class.equals(counter.getType()))
						lfr= new LongCounter(counter.getName(),root);
					else if(Double.class.equals(counter.getType()))
						lfr= new DoubleCounter(counter.getName(),root);
					else if(Boolean.class.equals(counter.getType()))
						lfr= new BooleanCounter(counter.getName(),root);
					
					if(counter.getDefaultValue()!=null)
						lfr.setValue(counter.getDefaultValue());
					children.put(counter.getName(), lfr);
					
				}
				record=children.get(input[0]);
			}
			
			record.incrementCounter(input[1]);
		}
		else{
			PerformanceRecord record=children.get(input[1]);
			if(record==null)
			{
				record= new DimensionRecord((String)input[0], input[1].getClass(), input[1],root);
				children.put(input[1], record);
			}
			record.incrementCounter(Arrays.copyOfRange(input, 2, input.length));
		}
	}
	
	
	@Override
	public void setCounterValue(Object...input) {
			if(input.length==2)
			{
				PerformanceRecord record=children.get(input[0]);
				if(record==null)
				{
					for(Counter counter :((OBjectTypeRecord)root).getObjectType().getCounters())
					{
						LeafRecord lfr=null;
						if(String.class.equals(counter.getType()))
							lfr= new StringCounter(counter.getName(),root);
						else if(Integer.class.equals(counter.getType()))
							lfr= new IntCounter(counter.getName(),root);
						else if(Long.class.equals(counter.getType()))
							lfr= new LongCounter(counter.getName(),root);
						else if(Double.class.equals(counter.getType()))
							lfr= new DoubleCounter(counter.getName(),root);
						else if(Boolean.class.equals(counter.getType()))
							lfr= new BooleanCounter(counter.getName(),root);
						
						if(counter.getDefaultValue()!=null)
							lfr.setValue(counter.getDefaultValue());
						children.put(counter.getName(), lfr);
					}
					record=children.get(input[0]);
				}
				
				record.setCounterValue(input[1]);
			}
			else{
				PerformanceRecord record=children.get(input[1]);
				if(record==null)
				{
					record= new DimensionRecord((String)input[0], input[1].getClass(), input[1],root);
					children.put(input[1], record);
				}
				record.setCounterValue(Arrays.copyOfRange(input, 2, input.length));
			}
	}


	public Map<Object, PerformanceRecord> getChildren() {
		return children;
	}
	
	
	
}
