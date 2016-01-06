package com.kpi4j.records;

import org.apache.log4j.Logger;


public class StringCounter extends LeafRecord{

	public static Logger logger=Logger.getLogger(StringCounter.class);
	
	String value;
	
	public StringCounter(String name){
		
		this.name=name;
	}
	
	public StringCounter(String name,PerformanceRecord root){
		this.name=name;
		this.root=root;
	}
	
	public StringCounter(PerformanceRecord root){
		this.root=root;
	}
	
	public Object getValue() {
		// TODO Auto-generated method stub
		return value;
	}

	
	@Override
	public void setCounterValue(Object...input){
		this.value=(String) input[0];
	}

	@Override
	public void incrementCounter(Object... input) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(Object obj) {
		// TODO Auto-generated method stub
		this.value=(String) obj;
	}



}
