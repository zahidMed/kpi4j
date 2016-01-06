package com.kpi4j.records;

import org.apache.log4j.Logger;

public class IntCounter extends NumericCounter{

	int value=0;
	public static Logger logger=Logger.getLogger(StringCounter.class);
	
	
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
		// TODO Auto-generated method stub
		return value;
	}

	@Override
	public void setCounterValue(Object...input){
		
		this.value=(Integer) input[0];
	}
	@Override
	public synchronized void incrementCounter(Object... input) {
		// TODO Auto-generated method stub
		this.value+=(Integer) input[0];
	}

	@Override
	public void setValue(Object obj) {
		// TODO Auto-generated method stub
		this.value=(Integer) obj;
	}

	

}
