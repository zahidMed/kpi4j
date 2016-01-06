package com.kpi4j.records;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.kpi4j.Counter;
import com.kpi4j.Dimension;
import com.kpi4j.ObjectType;
import com.kpi4j.PropertyConfigurator;

public class TestOBjectTypeRecord {

private final static String KPI4J_PROPS = "src/test/resources/kpi4j.xml";
	 
	
	public TestOBjectTypeRecord() 
	{
		//PropertyConfigurator.configure(KPI4J_PROPS);
	}
	
	@Test
	public void testSetCounterValueWithoutDimension() throws ClassNotFoundException{
		ObjectType ot= new ObjectType();
		ot.setName("ot1");
		
		Counter c1= new Counter();
		c1.setName("ctr1");
		c1.setType("Integer");
		ot.addCounter(c1);
		
		Counter c2= new Counter();
		c2.setName("ctr2");
		c2.setType("Long");
		ot.addCounter(c2);
		
		Counter c3= new Counter();
		c3.setName("ctr3");
		c3.setType("String");
		ot.addCounter(c3);
		OBjectTypeRecord rec= new OBjectTypeRecord(ot);
		rec.init();
		rec.setCounterValue("ctr1",10);
		rec.setCounterValue("ctr2",11L);
		rec.setCounterValue("ctr3","kdjkjsk");
		
		assertEquals(10,rec.children.get("ctr1").getValue());
		assertEquals(11L,rec.children.get("ctr2").getValue());
		assertEquals("kdjkjsk",rec.children.get("ctr3").getValue());
	}
	
	
	
	@Test
	public void testSetCounterValuetWithDimention() throws ClassNotFoundException{
		ObjectType ot= new ObjectType();
		ot.setName("ot1");
		
		Dimension dim1= new Dimension();
		dim1.setName("dim1");
		dim1.setType(Integer.class);
		dim1.addValue(1);
		dim1.addValue(2);
		dim1.addValue(3);
		ot.addDimension(dim1);
		
		Dimension dim2= new Dimension();
		dim2.setName("dim2");
		dim2.setType(String.class);
		ot.addDimension(dim2);
		
		Counter c1= new Counter();
		c1.setName("ctr1");
		c1.setType("Integer");
		ot.addCounter(c1);
		
		Counter c2= new Counter();
		c2.setName("ctr2");
		c2.setType("Long");
		ot.addCounter(c2);
		
		Counter c3= new Counter();
		c3.setName("ctr3");
		c3.setType("String");
		ot.addCounter(c3);
		
		OBjectTypeRecord rec= new OBjectTypeRecord(ot);
		System.out.println("set counter "+"dim1"+1+", dim2"+"Cell=997, "+"ctr1"+1);
		rec.setCounterValue("dim1",1,"dim2","Cell=997","ctr1",1);
		rec.setCounterValue("dim1",2,"dim2","Cell=998","ctr2",12L);
		rec.setCounterValue("dim1",3,"dim2","Cell=999","ctr3","kdjkjsk");
		BranchRecord br1=(BranchRecord)rec.children.get(1);
		
		BranchRecord br2=(BranchRecord)br1.children.get("Cell=997");
		System.out.println(br2);
		LeafRecord lf=(LeafRecord) br2.children.get("ctr1");
		assertEquals(1, lf.getValue());
		assertEquals(12L, ((BranchRecord)((BranchRecord)rec.children.get(2)).children.get("Cell=998")).children.get("ctr2").getValue());
		assertEquals("kdjkjsk", ((BranchRecord)((BranchRecord)rec.children.get(3)).children.get("Cell=999")).children.get("ctr3").getValue());
		
	}
	

	@Test
	public void testincrementCounterWithoutDimension() throws ClassNotFoundException{
		ObjectType ot= new ObjectType();
		ot.setName("ot1");
		
		Counter c1= new Counter();
		c1.setName("ctr1");
		c1.setType("Integer");
		ot.addCounter(c1);
		
		Counter c2= new Counter();
		c2.setName("ctr2");
		c2.setType("Long");
		ot.addCounter(c2);
		
		Counter c3= new Counter();
		c3.setName("ctr3");
		c3.setType("String");
		ot.addCounter(c3);
		OBjectTypeRecord rec= new OBjectTypeRecord(ot);
		
		rec.incrementCounter("ctr1",10);
		rec.incrementCounter("ctr2",11L);
		
		rec.incrementCounter("ctr1",5);
		rec.incrementCounter("ctr2",9L);
		
		rec.setCounterValue("ctr3","kdjkjsk");
		
		assertEquals(15,rec.children.get("ctr1").getValue());
		assertEquals(20L,rec.children.get("ctr2").getValue());
		assertEquals("kdjkjsk",rec.children.get("ctr3").getValue());
	}
	

	
	@Test
	public void testincrementCountertWithDimention() throws ClassNotFoundException{
		ObjectType ot= new ObjectType();
		ot.setName("ot1");
		
		Dimension dim1= new Dimension();
		dim1.setName("dim1");
		dim1.setType(Integer.class);
		ot.addDimension(dim1);
		
		Dimension dim2= new Dimension();
		dim2.setName("dim2");
		dim2.setType(String.class);
		ot.addDimension(dim2);
		
		Counter c1= new Counter();
		c1.setName("ctr1");
		c1.setType("Integer");
		ot.addCounter(c1);
		
		Counter c2= new Counter();
		c2.setName("ctr2");
		c2.setType("Long");
		ot.addCounter(c2);
		
		Counter c3= new Counter();
		c3.setName("ctr3");
		c3.setType("String");
		ot.addCounter(c3);
		
		OBjectTypeRecord rec= new OBjectTypeRecord(ot);
		rec.incrementCounter("dim1",1,"dim2","Cell=997","ctr1",1);
		rec.incrementCounter("dim1",45,"dim2","Cell=998","ctr2",12L);
		
		rec.incrementCounter("dim1",1,"dim2","Cell=997","ctr1",3);
		rec.incrementCounter("dim1",45,"dim2","Cell=998","ctr2",8L);
		
		rec.setCounterValue("dim1",32,"dim2","Cell=999","ctr3","kdjkjsk");
		BranchRecord br1=(BranchRecord)rec.children.get(1);
		
		BranchRecord br2=(BranchRecord)br1.children.get("Cell=997");
		
		LeafRecord lf=(LeafRecord) br2.children.get("ctr1");
		assertEquals(4, lf.getValue());
		assertEquals(20L, ((BranchRecord)((BranchRecord)rec.children.get(45)).children.get("Cell=998")).children.get("ctr2").getValue());
		assertEquals("kdjkjsk", ((BranchRecord)((BranchRecord)rec.children.get(32)).children.get("Cell=999")).children.get("ctr3").getValue());
		
	}
	
}
