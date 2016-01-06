package com.kpi4j.records;

import org.junit.Test;

import com.kpi4j.Counter;
import com.kpi4j.Dimension;
import com.kpi4j.ObjectType;

public class TestRecord {

	@Test
	public void testRecord1() throws ClassNotFoundException{
		ObjectType ot= new ObjectType();
		ot.setName("ot1");
		Counter ct= new Counter();
		ct.setName("ctr1");
		ct.setType("Integer");
		ot.addCounter(ct);
		ct= new Counter();
		ct.setName("ctr2");
		ct.setType("Long");
		ot.addCounter(ct);
	}
}
