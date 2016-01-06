package com.kpi4j.thread;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.kpi4j.Collector;
import com.kpi4j.config.xml.DomComfigurator;

import it.sauronsoftware.cron4j.Scheduler;

public class Cron4jTaskManager {

	public static Logger logger=Logger.getLogger(DomComfigurator.class);
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
