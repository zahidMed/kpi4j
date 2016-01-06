package com.kpi4j.thread;

import java.util.Collection;
import java.util.Map;

import com.kpi4j.Collector;
import com.kpi4j.appender.Appender;
import com.kpi4j.records.PerformanceRecord;

public class CollectorCron implements Runnable {

	Collector collector;
	
	public CollectorCron(Collector collector){
		this.collector=collector;
	}
	
	public Collector getCollector() {
		return collector;
	}
	public void setCollector(Collector collector) {
		this.collector = collector;
	}
	public void run() {
		// TODO Auto-generated method stub
		if(collector==null)
		{
			return;
		}
		Collection<PerformanceRecord> records=collector.initRecordValues();
		for(Appender app:collector.getAppenders())
		{
				app.save(records);
		}
	}
	
	public void finalize(){
		for(Appender app:collector.getAppenders())
		{
				app.finalize();
		}
	}

}
