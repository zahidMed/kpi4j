package com.kpi4j.records;

import com.kpi4j.ObjectType;

public class BasicRecordFactory {

	public static OBjectTypeRecord createRecord(ObjectType ot) {
		// TODO Auto-generated method stub
		return new OBjectTypeRecord(ot).init();
	}
	

}
