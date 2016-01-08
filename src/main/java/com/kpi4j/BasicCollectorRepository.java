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


package com.kpi4j;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.kpi4j.config.Configurator;
import com.kpi4j.config.xml.DomComfigurator;
import com.kpi4j.thread.Cron4jTaskManager;

/**
 * BAsic implementation of CollectorRepository
 * @author Mohammed ZAHID <zahid.med@gmail.com>
 * @see {@link CollectorRepository}
 */
public class BasicCollectorRepository extends CollectorRepository{

	private static BasicCollectorRepository repo=null;
	Configurator conf= new DomComfigurator();
	Cron4jTaskManager cron4jTaskManager=Cron4jTaskManager.getInstance();
	
	public static Logger logger=Logger.getLogger("kpi4j");
	
	/**
	 * Private constructor to implements Singleton design pattern. 
	 */
	private BasicCollectorRepository(){
		 collectors=new HashMap<String,Collector>();
	}
	
	/**
	 * Initialize the Collectors from the configuration file.
	 */
	synchronized void initialize()
	{
		if(initialized) return;
		initialized=conf.configure(getClass().getClassLoader().getResourceAsStream("resources/kpi4j.xml"), this);
		if(initialized)
		{
			cron4jTaskManager.start();
			cron4jTaskManager.registerShutdownHandler();
		}
	}
	
	
	
	@Override
	public Collector getCollector(String name) {
		// TODO Auto-generated method stub
		
		initialize();
		Collector coll=collectors.get(name);
		if(coll==null){
			logger.error("Collector not found");
			coll=	new Collector(name);
			collectors.put(name, coll);
		}
		return coll;
	}




	public static CollectorRepository getCollectorRepository() {
		// TODO Auto-generated method stub
		if(repo==null) repo= new  BasicCollectorRepository();
		return repo;
	}




	@Override
	public void addCollector(Collector coll, String name) {
		// TODO Auto-generated method stub
		collectors.put(name, coll);
		cron4jTaskManager.registerCollector(coll);
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	

}
