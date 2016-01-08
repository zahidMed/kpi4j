package com.kpi4j.appender;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import org.junit.Test;

import com.kpi4j.Counter;
import com.kpi4j.Dimension;
import com.kpi4j.ObjectType;
import com.kpi4j.records.OBjectTypeRecord;

public class TestJDBCAppender {

	String host="localhost";
	String port="3306";
	String login="root";
	String password="root";
	String driver="com.mysql.jdbc.Driver";
	String type="mysql";
	String database="kpi4j";
	Connection  connection;
	
	public TestJDBCAppender() throws ClassNotFoundException, SQLException{
		Class.forName(driver);
		connection = DriverManager.getConnection("jdbc:"+type+"://"+host+":"+port+"/"+database, login,password);
		
	}
	@Test
	public void testGetPreparedStsQueryWithoutDimension() throws ClassNotFoundException{
		
		JDBCAppender appender= new JDBCAppender();
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
		
		String query=appender.getPreparedStsQuery(ot);
		assertEquals("insert into ot1 (start_date, end_date, ctr1, ctr2, ctr3) values (?,?,?,?,?)",query);
	}
	
	
	@Test
	public void testGetPreparedStsQueryWitDimension() throws ClassNotFoundException{
		
		JDBCAppender appender= new JDBCAppender();
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
		
		String query=appender.getPreparedStsQuery(ot);
		assertEquals("insert into ot1 (start_date, end_date, dim1, dim2, ctr1, ctr2, ctr3) values (?,?,?,?,?,?,?)",query);
	}
	
	
	@Test
	public void testExecuteBatchInsert() throws SQLException, ClassNotFoundException{
		
		connection.createStatement().executeUpdate("drop table if exists ot1;");
		connection.createStatement().executeUpdate("create table ot1 ("
				+ "start_date datetime not null,"
				+ "end_date datetime,"
				+ "dim1 int not null,"
				+ "dim2 varchar(45) not null,"
				+ "ctr1 int,"
				+ "ctr2 bigint,"
				+ "ctr3 varchar(45),"
				+ "primary key(start_date,dim1,dim2)"
				+ ")");
		
		JDBCAppender appender= new JDBCAppender();
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
		rec.incrementCounter("dim1",1,"dim2",1,"ctr1",11);
		rec.incrementCounter("dim1",1,"dim2",1,"ctr2",12L);
		rec.setCounterValue("dim1",1,"dim2",1,"ctr3","Hello");
		
		rec.incrementCounter("dim1",1,"dim2",2,"ctr1",11);
		rec.incrementCounter("dim1",1,"dim2",2,"ctr2",12L);
		rec.setCounterValue("dim1",1,"dim2",2,"ctr3","Hello");
		
		rec.incrementCounter("dim1",2,"dim2",1,"ctr1",11);
		rec.incrementCounter("dim1",2,"dim2",1,"ctr2",12L);
		rec.setCounterValue("dim1",2,"dim2",1,"ctr3","Hello");
		
		rec.incrementCounter("dim1",2,"dim2",2,"ctr1",11);
		rec.incrementCounter("dim1",2,"dim2",2,"ctr2",12L);
		rec.setCounterValue("dim1",2,"dim2",2,"ctr3","Hello");
		
		rec.setEndTime(new Date());
		
		PreparedStatement ps=connection.prepareStatement("insert into ot1 (start_date, end_date, dim1, dim2, ctr1, ctr2, ctr3) values (?,?,?,?,?,?,?)");
		appender.executeBatchInsert(ps, rec, 0);
		ps.executeBatch();
	}
}
