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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.kpi4j.Collector;
import com.kpi4j.config.xml.DomComfigurator;

import it.sauronsoftware.cron4j.Scheduler;

public class Cron4jTaskManager {

	public static Logger logger=Logger.getLogger("kpi4j");
	private static Cron4jTaskManager instance=null;
	List<CollectorCron> list;
	Scheduler scheduler;
	private Cron4jTaskManager(){
		scheduler = new Scheduler();
		list= new ArrayList<CollectorCron>();
	}
	
	public static Cron4jTaskManager getInstance(){
		if(instance==null) instance= new Cron4jTaskManager();
		return instance;
	}
	
	
	public void registerCollector(Collector coll){
		logger.debug("schedule new collector "+coll.getName()+", scheduling pattern "+coll.getSchedulingPattern());
		CollectorCron cron= new CollectorCron(coll);
		list.add(cron);
		scheduler.schedule(coll.getSchedulingPattern(), cron);
	}
	
	public void start(){
		scheduler.start();
	}
	
	public void registerShutdownHandler(){
		
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run(){
				System.out.println("Stopping");
				scheduler.stop();
				for(CollectorCron coll:list){
					coll.run();
					coll.finalize();
				}
			}
		});
	}
	
}
