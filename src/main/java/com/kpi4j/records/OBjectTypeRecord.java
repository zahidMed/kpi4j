package com.kpi4j.records;

import java.util.Arrays;
import java.util.Date;

import com.kpi4j.Counter;
import com.kpi4j.ObjectType;

public class OBjectTypeRecord extends BranchRecord{

	ObjectType objectType;
	Date startTime;
	Date endTime;
	
	public OBjectTypeRecord(ObjectType objectType){
		this.objectType=objectType;
		this.root=this;
		this.name=objectType.getName();
		startTime= new Date();
	}
	
	public OBjectTypeRecord init()
	{
		
		if(objectType.getDimensions()==null){
			for(Counter counter: objectType.getCounters()){
				LeafRecord lf=null;
				if(String.class.equals(counter.getType()))
					lf= new StringCounter(root);
				else if(Integer.class.equals(counter.getType()))
					lf= new IntCounter(root);
				else if(Long.class.equals(counter.getType()))
					lf= new LongCounter(root);
				else if(Double.class.equals(counter.getType()))
					lf= new DoubleCounter(root);
				else if(Boolean.class.equals(counter.getType()))
					lf= new BooleanCounter(root);
				children.put(counter.getName(), lf);
			}
		}
		else{
			
		}
		return this;
	}
	
	

	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return objectType.getName();
	}

	public ObjectType getObjectType() {
		return objectType;
	}

	public void setObjectType(ObjectType objectType) {
		this.objectType = objectType;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Override
	public void setValue(Object obj) {
		// TODO Auto-generated method stub
		
	}

}
