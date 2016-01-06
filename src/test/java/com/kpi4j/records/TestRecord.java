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

package com.kpi4j.records;

import org.junit.Test;

import com.kpi4j.Counter;
import com.kpi4j.Dimension;
import com.kpi4j.ObjectType;

public class TestRecord {

	@Test
	public void testRecord1() throws ClassNotFoundException{
		ObjectType ot= new ObjectType();
		ot.setName("ot1");
		Counter ct= new Counter();
		ct.setName("ctr1");
		ct.setType("Integer");
		ot.addCounter(ct);
		ct= new Counter();
		ct.setName("ctr2");
		ct.setType("Long");
		ot.addCounter(ct);
	}
}
