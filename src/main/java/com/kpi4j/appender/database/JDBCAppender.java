/* 
 * Copyright (C) 2016 Mohammed ZAHID (zahid.med@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.kpi4j.appender.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.kpi4j.Counter;
import com.kpi4j.Dimension;
import com.kpi4j.ObjectType;
import com.kpi4j.appender.Appender;
import com.kpi4j.records.BooleanCounter;
import com.kpi4j.records.DimensionRecord;
import com.kpi4j.records.DoubleCounter;
import com.kpi4j.records.IntCounter;
import com.kpi4j.records.LeafRecord;
import com.kpi4j.records.LongCounter;
import com.kpi4j.records.OBjectTypeRecord;
import com.kpi4j.records.PerformanceRecord;
import com.kpi4j.records.StringCounter;

/**
 * Extends the Appender in order to save performance indicators in a JDBC based database.
 * @author Mohammed ZAHID <zahid.med@gmail.com>
 *
 */
public class JDBCAppender extends Appender{

	public static Logger logger=Logger.getLogger("kpi4j");
	
	String host;
	String port="3306";
	String login;
	String password;
	String driver;
	String type;
	String database;
	Connection  connection;
	boolean initialized=false;
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		try
		{
			Class.forName(driver);
			connection = DriverManager.getConnection("jdbc:"+type+"://"+host+":"+port+"/"+database, login,password);
			initialized=true;
		}
		catch(Exception e)
		{
			logger.error("Unexpected exception while initialising Connection for"+database, e);
		}
	}

	@Override
	public void finalize() {
		// TODO Auto-generated method stub
		if(connection!=null)
			try {
				connection.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				logger.error("Unexpected exception while closing connection for "+database, e);
			}
	}

	@Override
	public void save(Collection<PerformanceRecord> records) {
		// TODO Auto-generated method stub
		if(!initialized) return;
		for(PerformanceRecord record: records)
		{
			
			OBjectTypeRecord otRec=(OBjectTypeRecord) record;
			try {
				PreparedStatement ps=connection.prepareStatement(getPreparedStsQuery(otRec.getObjectType()));
				executeBatchInsert(ps, otRec, 0);
				ps.executeBatch();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("Exception while saving performance records", e);
			}
			
			
		}
	}

	/**
	 * Generate prepared statement query according to the Object type elements
	 * @param ot ObjectType from which the sql query is generated
	 * @return sql query
	 */
	String getPreparedStsQuery(ObjectType ot){
		StringBuffer buff= new StringBuffer("insert into ");
		buff.append(ot.getName()+" (start_date, end_date");
		int fieldCount=0;
		if(ot.getDimensions()!=null)
			for(Dimension dim: ot.getDimensions()){
				buff.append(", "+dim.getName());
				fieldCount++;
			}
		
		if(ot.getCounters()!=null)
			for(Counter ctr: ot.getCounters()){
				buff.append(", "+ctr.getName());
				fieldCount++;
			}
		buff.append(") values (?,?");
		for(int i=0;i<fieldCount;i++)
			buff.append(",?");
		
		buff.append(")");
		return buff.toString();
	}
	
	/**
	 * Recursive method that fill the record in the prepared statement by parsing 
	 * the Performance record tree
	 * @param ps
	 * @param rec
	 * @param depth
	 * @throws SQLException
	 */
	void executeBatchInsert(PreparedStatement ps, PerformanceRecord rec, int depth) throws SQLException{
		if(rec instanceof OBjectTypeRecord){
			OBjectTypeRecord record= (OBjectTypeRecord) rec;
			ps.setTimestamp(1, new Timestamp(record.getStartTime().getTime()));
			ps.setTimestamp(2, new Timestamp(record.getEndTime().getTime()));
			for(PerformanceRecord subRec: record.getChildren().values()){
				executeBatchInsert(ps, subRec, 3);
			}
		}
		else if(rec instanceof DimensionRecord){
			DimensionRecord record= (DimensionRecord) rec;
			ps.setObject(depth++, record.getValue());
			if(record.getChildren().values().iterator().next() instanceof LeafRecord)
			{
				for(PerformanceRecord subRec: record.getChildren().values()){
					LeafRecord ctr=(LeafRecord) subRec;
					if(ctr.getValue()!=null)
						ps.setObject(depth++, ctr.getValue());
					else
					{
						if(subRec instanceof StringCounter)
							ps.setNull(depth++, Types.VARCHAR);
						else if(subRec instanceof BooleanCounter)
							ps.setNull(depth++, Types.BOOLEAN);
						else if(subRec instanceof DoubleCounter)
							ps.setNull(depth++, Types.DOUBLE);
						else if(subRec instanceof IntCounter)
							ps.setNull(depth++, Types.INTEGER);
						else if(subRec instanceof LongCounter)
							ps.setNull(depth++, Types.BIGINT);
					}
				}
				ps.addBatch();
			}
			else
				for(PerformanceRecord subRec: record.getChildren().values()){
					executeBatchInsert(ps,subRec, depth);
				}
		}
	}
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
