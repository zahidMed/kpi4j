package com.kpi4j;

/**
 * Class that represents a Counter definition, it reflects 
 * the Counter tag in the configuration file
 * @author Mohammed ZAHID <zahid.med@gmail.com>
 * @see {@link #ObjectType}
 */
public class Counter {

	/**
	 * Counter name
	 */
	String name;
	
	/**
	 *  Counter type: Integer, Long, Double, String ....
	 */
	Class type;
	
	/**
	 * Counter default value
	 */
	Object defaultValue;
	
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
	public Object getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}
}
