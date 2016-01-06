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
