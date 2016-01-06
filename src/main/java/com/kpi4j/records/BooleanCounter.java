package com.kpi4j.records;

import org.apache.log4j.Logger;

public class BooleanCounter extends LeafRecord{

	boolean value=false;
	public static Logger logger=Logger.getLogger(StringCounter.class);
	
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
		// TODO Auto-generated method stub
		return value;
	}
	

	@Override
	public void setCounterValue(Object...input){
		this.value=(Boolean) input[0];
	}

	@Override
	public void incrementCounter(Object... input) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(Object obj) {
		// TODO Auto-generated method stub
		this.value=(Boolean) obj;
	}

	


}
