
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

###SimpleXMLFileAppender
the output file sample bellow is generated when using the appender **com.kpi4j.appender.SimpleXMLFileAppender** :

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

###JDBCAppender

If you want to save performance records directly in JDBC database such use Mysql, Postgresql, MariaDB, Oracle ... you can use the appender **com.kpi4j.appender.JDBCAppender** for this task.

####JDBCAppender dependencies

Developer should add the JDBC driver that corresponds to the database

**Mysql dependency**

		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<version>5.1.30</version>
		</dependency>

**MariaDB dependency**

		<dependency>
			<groupId>org.mariadb.jdbc</groupId>
			<artifactId>mariadb-java-client</artifactId>
			<version>1.2.0</version>
		</dependency>

**Postgresql dependency**

    <dependency>
    	<groupId>postgresql</groupId>
    	<artifactId>postgresql</artifactId>
    	<version>VERSION</version>
    </dependency>

####JDBCAppender configuration

The configuration of **Appender** tag should look like:

    <Appender class="com.kpi4j.appender.database.JDBCAppender" >
			<param name="host" value="localhost"/>
			<param name="port" value="3306"/>
			<param name="login" value="root"/>
			<param name="password" value="root"/>
			<param name="driver" value="com.mysql.jdbc.Driver"/>
			<param name="type" value="mysql"/>
			<param name="database" value="kpi4j"/>
			<param name="createTable" value="true"/>
    </Appender>

Where:
 * host: the database host name
 * port: the database port
 * login: the database login
 * password: the database password
 * driver: the JDBC driver com.mysql.jdbc.Driver for mysql, org.mariadb.jdbc.Driver for mariaDB ...
 * type: the database type: mysql, mariadb or postgresql ...
 * database: the database schema name in which the performance records will be stored.
 * createTable: indicate if the library should create the tables related to the Object types in the database if not exist (default value is false)

####Database pre-requisites (If the tag createTable is not used )

The database should be prepared before executing the program. The object types should correspond to tables with the same names and the dimension should be declared as a primary keys. Developer should also add two datetime fields which are **start_date (primary key)** and **end_date**.
Following the configuration file above, the corresponding table should be:


	create table ot1 (
	start_date datetime not null,
	end_date datetime,
	dimension1 varchar(45) not null,
	applicationId int,
	ctr1 int,
	ctr2 bigint,
	primary key(start_date,dimension1)
	);

###3GPP XML file appenders
The 3GPP appenders are used to generate statistic files for telecommunication systems according to the 3GPP specifications. Each Object type is stored in separated file. The following table link the appender class to the vesrion

Version|Class
-------|-------------------
3GPP TS 32.435 version 7.2.0 Release 7|com.kpi4j.appender.XML3GppTs32Dot435V7Dot2Appender

####Appender configuration: 3GPP TS 32.435 version 7.2.0 Release 7

The configuration of this 3GPP version can be done by defining the parameters vendorName, dnPrefix, localDn, elementType, swVersion and directory where the statistic files will be saved.

The statistics file names corresponds to the Object type names with start time and end time.

Bellow a configuration example for the object type RncFunction.

    <?xml version="1.0" encoding="UTF-8"?>
    <config>
    	<Collector name="node1"  schedulingPattern="* * * * *">
    		<ObjectType name="RncFunction">
    			<Dimensions>
    				<Dimension name="RncFunction" type="String"/>
    				<Dimension name="UtranCell" type="String"/>
    			</Dimensions>
    			<Counters>
    				<Counter name="attTCHSeizures" type="Integer"/>
    				<Counter name="succTCHSeizures" type="Integer"/>
    				<Counter name="attImmediateAssignProcs" type="Integer"/>
    				<Counter name="succImmediateAssignProcs" type="Integer"/>
    				<Counter name="ethernetStatsBroadcastTx" type="Integer"/>
    			</Counters>
    		</ObjectType>
    		<Appender class="com.kpi4j.appender._3gpp.XML3GppTs32Dot435V7Dot2Appender" >
    			<param name="vendorName" value="CompanyNN"/>
    			<param name="dnPrefix" value="DC=a1.companyNN.com,SubNetwork=1,IRPAgent=1"/>
    			<param name="localDn" value="SubNetwork=CountryNN,MeContext=MEC-Gbg-1,ManagedElement=RNC-Gbg-1"/>
    			<param name="elementType" value="RNC"/>
    			<param name="userLabel" value="RNC Telecomville"/>
    			<param name="swVersion" value="R30.1.5"/>
    			<param name="directory" value="C:/statistics/"/>
    		</Appender>
    	</Collector>
    </config>


the output file of the configuration above looks like:

	<?xml version="1.0" encoding="UTF-8" standalone="no"?>
	<?xml-stylesheet type="text/xsl" href="MeasDataCollection.xsl"?>
	<measCollecFile xmlns="http://www.3gpp.org/ftp/specs/archive/32_series/32.435#measCollec">
	  <fileHeader CompanyNN="CompanyNN" dnPrefix="DC=a1.companyNN.com,SubNetwork=1,IRPAgent=1" fileFormatVersion="32.435 V7.2.0">
	    <fileSender elementType="RNC" localDn="SubNetwork=CountryNN,MeContext=MEC-Gbg-1,ManagedElement=RNC-Gbg-1"/>
	    <measCollec beginTime="2016-01-15T16:04:00+0000"/>
	  </fileHeader>
	  <measData>
	    <managedElement localDn="SubNetwork=CountryNN,MeContext=MEC-Gbg-1,ManagedElement=RNC-Gbg-1" swVersion="R30.1.5" userLabel="RNC Telecomville"/>
	  </measData>
	  <measInfo>
	    <granPeriod duration="PT60S" endTime="2016-01-15T16:05:00+0000"/>
	    <repPeriod duration="PT60S"/>
	    <measType p="1">attTCHSeizures</measType>
	    <measType p="2">succTCHSeizures</measType>
	    <measType p="3">attImmediateAssignProcs</measType>
	    <measType p="4">succImmediateAssignProcs</measType>
	    <measType p="5">ethernetStatsBroadcastTx</measType>
	    <measValue measObjLdn="RncFunction=RF-1,UtranCell=Gbg-2">
	      <r p="1">2990</r>
	      <r p="2">2988</r>
	      <r p="3">2927</r>
	      <r p="4">2962</r>
	      <r p="5">3006</r>
	    </measValue>
	    <measValue measObjLdn="RncFunction=RF-1,UtranCell=Gbg-1">
	      <r p="1">3008</r>
	      <r p="2">2978</r>
	      <r p="3">2969</r>
	      <r p="4">2982</r>
	      <r p="5">2969</r>
	    </measValue>
	    <measValue measObjLdn="RncFunction=RF-2,UtranCell=Gbg-1">
	      <r p="1">2983</r>
	      <r p="2">2963</r>
	      <r p="3">3019</r>
	      <r p="4">2892</r>
	      <r p="5">3018</r>
	    </measValue>
	    <measValue measObjLdn="RncFunction=RF-2,UtranCell=Gbg-2">
	      <r p="1">2997</r>
	      <r p="2">2934</r>
	      <r p="3">2860</r>
	      <r p="4">3024</r>
	      <r p="5">2904</r>
	    </measValue>
	  </measInfo>
	</measCollecFile>


##Library performance

###Test environment

 * Dual core i5 2.60 GHz
 * RAM: 4 Go
 * OS: Windows 7 pro 64 bit
 * JRE version 1.7.0_75

###Result

The library test was performed by creating **100** concurrent threads that increment randomly counters with 1 dimension depth.

This test shows that the average duration of incrementing a counter is **180 ns** (nanosecond).



For any further question, fell free to contact me at zahid.med@gmail.com