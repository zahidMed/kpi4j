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

import com.kpi4j.config.Configurator;
import com.kpi4j.config.xml.DomConfigurator;

/**
 *  Allows the configuration of kpi4j from an external file.
 *  Some times it's usefull to use an external file for configuring kpi4j, so this class
 *  Allow it, for example when developing a web application, this class can be used in a 
 *  Servlet that run at the server startup.
 *  @author Mohammed ZAHID <zahid.med@gmail.com>
 *
 */
public class PropertyConfigurator {

	Configurator configurator;
	CollectorRepository repo;
	
	/**
	 * private constructor
	 */
	private PropertyConfigurator(){
		configurator= new DomConfigurator();
		repo=BasicCollectorRepository.getCollectorRepository();
	}
	
	/**
	 * This method is used to configure kpi4j from a file
	 * @param path: path of the configuration file
	 */
	public static void configure(String path){
		PropertyConfigurator prop=new PropertyConfigurator();
		prop.configurator.configure(path, prop.repo);
		prop.repo.setInitialized(true);
	}
	
}
