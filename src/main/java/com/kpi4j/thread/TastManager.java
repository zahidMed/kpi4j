package com.kpi4j.thread;

import com.kpi4j.CollectorRepository;

public interface TastManager {

	public void shceduleCollectors(CollectorRepository repository);
	
	public String frequencyToCron(String frequency);
}
