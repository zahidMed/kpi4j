package com.kpi4j;

import com.kpi4j.config.Configurator;
import com.kpi4j.config.xml.DomComfigurator;

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
		configurator= new DomComfigurator();
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
