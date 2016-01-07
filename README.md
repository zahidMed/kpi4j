
#kpi4j
##What is it?

This is an open source library that allows generating performance indicators easily for java based application. This library is configurable through an xml file (similar to log4j) in order to define the performance object types, dimensions, counters, the output format and statistics generation periodicity. Those statistics can be saved as a file (xml/json) or injected directly in a database. Then they can be correlated and analyzed through any Business Intelligence solution in order to check the application performance and quality: number of transactions, ratio of successful and failure ...

##Some concepts

In order to retrieve the application statistics, developer should be aware of three main concepts:
 * Object type: is a group of counters that belong to the same feature or behavior.
 * Dimension: distinguish the counters of the same Object type. for example when providing a web service for many customers, developer can distinguish between the customers statistics by adding a dimension "customer" that have as a value the customer name or identifier.
 * Counter: define the statistic developer want to retrieve. For example number of opened sessions, number of successful and failed transactions ....

##Dependencies

When using kpi4j, add the following dependencies:

    <dependency>
    	<groupId>log4j</groupId>
    	<artifactId>log4j</artifactId>
    	<version>1.2.17</version>
    </dependency>
    <dependency>
    	<groupId>it.sauronsoftware.cron4j</groupId>
    	<artifactId>cron4j</artifactId>
    	<version>2.2.5</version>
    </dependency>
    <dependency>
    	<groupId>xml-apis</groupId>
    	<artifactId>xml-apis</artifactId>
    	<version>1.3.04</version>
    </dependency>

##How use it?

###Configuration
Using kpi4j is made by creating a configuration file /resources/kpi4j.xml like this one

    <config>
	    <Collector name="collector1"  schedulingPattern="* * * * *">
		    <ObjectType name="ot1">
			    <Dimensions>
				    <Dimension name="dimension1" type="String"/>
			    </Dimensions>
			    <Counters>
				    <Counter name="applicationId" type="Integer" value="1"/>
				    <Counter name="ctr1" type="Integer"/>
				    <Counter name="ctr2" type="Long"/>
			    </Counters>
		    </ObjectType>
		    <Appender class="com.kpi4j.appender.SimpleXMLFileAppender" >
			    <param name="file" value="C:/statistics/stsFile"/>
		    </Appender>
	    </Collector>
    </config>

 * Collector: is the process that will manage the statistics defined inside this tag and save them using the indicated Appender according to the crontab defined in the attribute schedulingPattern.
 * ObjectType: is the object type, the name should be unique in the Collector
 * Dimension: is the statistic dimension. The dimension is defined by a name and type (Integer, Long, String, Float or Boolean).
 * Counter: is defined by it's name which would be unique in the ObjectType tag, type (Integer, Long, String, Float or Boolean). if the attribute value is defined, the counter will be initialized with that value.
 Appender: defines the output format in which the statistics will be stored. The current version supports only xml format.

###Call in the application source code

Similar to log4j, developer can call the collector by its name, then use it to increment counters values.

code example based on the above configuration file

    public class HelloKpi4j{
    
    	public static Collector collector=Collector.getCollector("collector1");
    
    	public static void main(String[] args) {
    		//increment the counter "ctr1" of the dimension "client1" by 1
    		collector.incrementCounter("ObjectType","ot1","dimension1","client1","ctr1",1);    	
    		//increment the counter "ctr2" of the dimension "client2" by 3
			collector.incrementCounter("ObjectType","ot1","dimension1","client2","ctr2",3L);
    	}
    
    }
    
    }

Output file sample:

    <?xml version="1.0" encoding="UTF-8" standalone="no"?>
    <Statistics>
    	<ObjectType name="ot1">
    		<StartDate>201601071220</StartDate>
    		<EndDate>201601071221</EndDate>
    		<dimension1 value="client1">
    			<ctr1>1</ctr1>
    			<ctr2>0</ctr2>
    		</dimension1>
    		<dimension2 value="client2">
    			<ctr1>0</ctr1>
    			<ctr2>3</ctr2>
    		</dimension2>
    	</ObjectType>
    </Statistics>
    
For any further question, fell free to contact me at zahid.med@gmail.com