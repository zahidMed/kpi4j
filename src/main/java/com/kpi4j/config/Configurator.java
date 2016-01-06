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
