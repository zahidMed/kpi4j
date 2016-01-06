package com.kpi4j;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.kpi4j.Collector;
import com.kpi4j.Counter;
import com.kpi4j.Dimension;
import com.kpi4j.ObjectType;
import com.kpi4j.PropertyConfigurator;
import com.kpi4j.records.BranchRecord;
import com.kpi4j.records.LeafRecord;
import com.kpi4j.records.OBjectTypeRecord;

public class TestCollector {

	 private final static String KPI4J_PROPS = "src/test/resources/kpi4j.xml";
	 
	
	public TestCollector() 
	{
		PropertyConfigurator.configure(KPI4J_PROPS);
	}
	
	@Test
	public void testCheckInputWithoutDimention() throws ClassNotFoundException{
		Collector collector=Collector.getCollector("TestCollector");
		assertEquals(true, collector.checkInput("ObjectType","ot1","ctr1",1));
		assertEquals(true, collector.checkInput("ObjectType","ot1","ctr2",1L));
		assertEquals(true, collector.checkInput("ObjectType","ot1","ctr3","kdjkjsk"));
		
		assertEquals(false, collector.checkInput("ObjectType","ot1","ctr1",12L));
		assertEquals(false, collector.checkInput("ObjectType","ot1","ctr2","mksmkfms"));
		assertEquals(false, collector.checkInput("ObjectType","ot1","ctr3",1));
	}
	
	
	@Test
	public void testCheckInputWithDimention() throws ClassNotFoundException{
		Collector collector=Collector.getCollector("TestCollector");
		assertEquals(true, collector.checkInput("ObjectType","ot2","dim1",1,"dim2","Cell=997","ctr1",1));
		assertEquals(true, collector.checkInput("ObjectType","ot2","dim1",45,"dim2","Cell=998","ctr2",1L));
		assertEquals(true, collector.checkInput("ObjectType","ot2","dim1",32,"dim2","Cell=999","ctr3","kdjkjsk"));
		
		assertEquals(false, collector.checkInput("ObjectType","ot2","ctr1",12L));
		assertEquals(false, collector.checkInput("ObjectType","ot2","dim1",1,"ctr2","mksmkfms"));
		assertEquals(false, collector.checkInput("ObjectType","ot2","dim1",1L,"dim2","Cell=997","ctr3",1));
		assertEquals(false, collector.checkInput("ObjectType","ot2","dim1","lsjdl","dim2","Cell=997","ctr3",1));
	}
	
	
	@Test
	public void testSetCounterValueWithoutDimension() throws ClassNotFoundException{
		Collector collector=Collector.getCollector("TestCollector");
		
		collector.setCounterValue("ObjectType","ot1","ctr1",10);
		collector.setCounterValue("ObjectType","ot1","ctr2",11L);
		collector.setCounterValue("ObjectType","ot1","ctr3","kdjkjsk");
		
		assertEquals(10,((BranchRecord)collector.getOBjectTypeRecord("ot1")).getChildren().get("ctr1").getValue());
		assertEquals(11L,((BranchRecord)collector.getOBjectTypeRecord("ot1")).getChildren().get("ctr2").getValue());
		assertEquals("kdjkjsk",((BranchRecord)collector.getOBjectTypeRecord("ot1")).getChildren().get("ctr3").getValue());
	}
	
	
	@Test
	public void testSetCounterValuetWithDimention() throws ClassNotFoundException{
		Collector collector=Collector.getCollector("TestCollector");
		
		collector.setCounterValue("ObjectType","ot2","dim1",1,"dim2","Cell=997","ctr1",1);
		collector.setCounterValue("ObjectType","ot2","dim1",45,"dim2","Cell=998","ctr2",12L);
		collector.setCounterValue("ObjectType","ot2","dim1",32,"dim2","Cell=999","ctr3","kdjkjsk");
		BranchRecord br1=(BranchRecord)((OBjectTypeRecord)collector.getOBjectTypeRecord("ot2")).getChildren().get(1);
		
		BranchRecord br2=(BranchRecord)br1.getChildren().get("Cell=997");
		System.out.println(br2);
		LeafRecord lf=(LeafRecord) br2.getChildren().get("ctr1");
		assertEquals(1, lf.getValue());
		assertEquals(12L, ((BranchRecord)((BranchRecord)((OBjectTypeRecord)collector.getOBjectTypeRecord("ot2")).getChildren().get(45)).getChildren().get("Cell=998")).getChildren().get("ctr2").getValue());
		assertEquals("kdjkjsk", ((BranchRecord)((BranchRecord)((OBjectTypeRecord)collector.getOBjectTypeRecord("ot2")).getChildren().get(32)).getChildren().get("Cell=999")).getChildren().get("ctr3").getValue());
		
	}
	
	
	@Test
	public void testIncrementCounterWithoutDimension() throws ClassNotFoundException{
		Collector collector=Collector.getCollector("TestCollector");
		
		collector.incrementCounter("ObjectType","ot1","ctr1",10);
		collector.incrementCounter("ObjectType","ot1","ctr2",11L);
		
		collector.incrementCounter("ObjectType","ot1","ctr1",10);
		collector.incrementCounter("ObjectType","ot1","ctr2",11L);
		collector.setCounterValue("ObjectType","ot1","ctr3","kdjkjsk");
		
		assertEquals(20,((BranchRecord)collector.getOBjectTypeRecord("ot1")).getChildren().get("ctr1").getValue());
		assertEquals(22L,((BranchRecord)collector.getOBjectTypeRecord("ot1")).getChildren().get("ctr2").getValue());
		assertEquals("kdjkjsk",((BranchRecord)collector.getOBjectTypeRecord("ot1")).getChildren().get("ctr3").getValue());
	}
	
	
	@Test
	public void testIncrementCounterWithDimention() throws ClassNotFoundException{
		Collector collector=Collector.getCollector("TestCollector");
		
		collector.incrementCounter("ObjectType","ot2","dim1",1,"dim2","Cell=997","ctr1",1);
		collector.incrementCounter("ObjectType","ot2","dim1",45,"dim2","Cell=998","ctr2",12L);
		
		collector.incrementCounter("ObjectType","ot2","dim1",1,"dim2","Cell=997","ctr1",1);
		collector.incrementCounter("ObjectType","ot2","dim1",45,"dim2","Cell=998","ctr2",12L);
		
		collector.setCounterValue("ObjectType","ot2","dim1",32,"dim2","Cell=999","ctr3","kdjkjsk");
		BranchRecord br1=(BranchRecord)((OBjectTypeRecord)collector.getOBjectTypeRecord("ot2")).getChildren().get(1);
		
		BranchRecord br2=(BranchRecord)br1.getChildren().get("Cell=997");
		System.out.println(br2);
		LeafRecord lf=(LeafRecord) br2.getChildren().get("ctr1");
		assertEquals(2, lf.getValue());
		assertEquals(24L, ((BranchRecord)((BranchRecord)((OBjectTypeRecord)collector.getOBjectTypeRecord("ot2")).getChildren().get(45)).getChildren().get("Cell=998")).getChildren().get("ctr2").getValue());
		assertEquals("kdjkjsk", ((BranchRecord)((BranchRecord)((OBjectTypeRecord)collector.getOBjectTypeRecord("ot2")).getChildren().get(32)).getChildren().get("Cell=999")).getChildren().get("ctr3").getValue());
		
	}
}
