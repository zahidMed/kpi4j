package com.kpi4j.config;

import java.io.InputStream;
import java.net.URI;

import com.kpi4j.CollectorRepository;

/**
 * 
 * @author Mohammed ZAHID <zahid.med@gmail.com>
 *
 */
public interface Configurator {

	public boolean configure(URI configFile, CollectorRepository repository);
	public boolean configure(String configFile, CollectorRepository repository);
	public boolean configure(InputStream in, CollectorRepository repository);
}
