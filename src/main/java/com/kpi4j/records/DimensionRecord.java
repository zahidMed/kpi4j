package com.kpi4j.records;

import java.util.Arrays;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;

import com.kpi4j.Collector;

public class DimensionRecord extends BranchRecord{

	Object value;
	public static Logger logger=Logger.getLogger(Collector.class);
	
	
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
