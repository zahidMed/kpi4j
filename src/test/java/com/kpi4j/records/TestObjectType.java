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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.kpi4j.Counter;
import com.kpi4j.Dimension;
import com.kpi4j.ObjectType;

public class TestObjectType {

	
	@Test
	public void testCheckInputWithoutDimention() throws ClassNotFoundException{
		ObjectType ot= new ObjectType();
		ot.setName("ot1");
		
		Counter c1= new Counter();
		c1.setName("ctr1");
		c1.setType("Integer");
		ot.addCounter(c1);
		
		Counter c2= new Counter();
		c2.setName("ctr2");
		c2.setType("Long");
		ot.addCounter(c2);
		
		Counter c3= new Counter();
		c3.setName("ctr3");
		c3.setType("String");
		ot.addCounter(c3);
		
		assertEquals(true, ot.checkInput("ObjectType","ot1","ctr1",1));
		assertEquals(true, ot.checkInput("ObjectType","ot1","ctr2",1L));
		assertEquals(true, ot.checkInput("ObjectType","ot1","ctr3","kdjkjsk"));
		
		assertEquals(false, ot.checkInput("ObjectType","ot1","ctr1",12L));
		assertEquals(false, ot.checkInput("ObjectType","ot1","ctr2","mksmkfms"));
		assertEquals(false, ot.checkInput("ObjectType","ot1","ctr3",1));
	}
	
	
	@Test
	public void testCheckInputWithDimention() throws ClassNotFoundException{
		ObjectType ot= new ObjectType();
		ot.setName("ot1");
		
		Dimension dim1= new Dimension();
		dim1.setName("dim1");
		dim1.setType(Integer.class);
		ot.addDimension(dim1);
		
		Dimension dim2= new Dimension();
		dim2.setName("dim2");
		dim2.setType(String.class);
		ot.addDimension(dim2);
		
		Counter c1= new Counter();
		c1.setName("ctr1");
		c1.setType("Integer");
		ot.addCounter(c1);
		
		Counter c2= new Counter();
		c2.setName("ctr2");
		c2.setType("Long");
		ot.addCounter(c2);
		
		Counter c3= new Counter();
		c3.setName("ctr3");
		c3.setType("String");
		ot.addCounter(c3);
		
		assertEquals(true, ot.checkInput("ObjectType","ot1","dim1",1,"dim2","Cell=997","ctr1",1));
		assertEquals(true, ot.checkInput("ObjectType","ot1","dim1",45,"dim2","Cell=998","ctr2",1L));
		assertEquals(true, ot.checkInput("ObjectType","ot1","dim1",32,"dim2","Cell=999","ctr3","kdjkjsk"));
		
		assertEquals(false, ot.checkInput("ObjectType","ot1","ctr1",12L));
		assertEquals(false, ot.checkInput("ObjectType","ot1","dim1",1,"ctr2","mksmkfms"));
		assertEquals(false, ot.checkInput("ObjectType","ot1","dim1",1L,"dim2","Cell=997","ctr3",1));
		assertEquals(false, ot.checkInput("ObjectType","ot1","dim1","lsjdl","dim2","Cell=997","ctr3",1));
	}
}
