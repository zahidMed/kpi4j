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

package com.kpi4j.thread;

import java.util.Collection;

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
