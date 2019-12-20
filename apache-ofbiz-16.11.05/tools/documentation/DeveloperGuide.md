

### Database Setup
Install Mysql DB (8.0)

Run below sql commands 

###### Create Database and Users for Ofbiz 
* CREATE DATABASE ofbiz;
* CREATE DATABASE ofbizolap;
* CREATE DATABASE ofbiztenant;

* CREATE USER ofbiz@localhost IDENTIFIED BY 'OFbiz@123';
* CREATE USER ofbiz@'%' IDENTIFIED BY 'OFbiz@123';
* GRANT ALL PRIVILEGES ON ofbiz.* to 'ofbiz'@'localhost';
* GRANT ALL PRIVILEGES ON ofbiz.* to 'ofbiz'@'%';

* GRANT ALL PRIVILEGES ON ofbizolap.* to 'ofbiz'@'localhost';
* GRANT ALL PRIVILEGES ON ofbizolap.* to 'ofbiz'@'%';

* GRANT ALL PRIVILEGES ON ofbiztenant.* to 'ofbiz'@'localhost';
* GRANT ALL PRIVILEGES ON ofbiztenant.* to 'ofbiz'@'%';

##### Create Admin user to perform tenant creation operations

* CREATE USER ofadmin@'%' IDENTIFIED BY 'oAdmin@123';
* CREATE USER ofadmin@localhost IDENTIFIED BY 'oAdmin@123';
* GRANT ALL PRIVILEGES ON \*.* to 'ofadmin'@'%' WITH GRANT OPTION;
* GRANT ALL PRIVILEGES ON \*.* to 'ofadmin'@'localhost' WITH GRANT OPTION;

---

> OFBIZ_HOME - we refer to directory /apache-ofbiz-16.11.05 as OFBIZ_HOME

### entityengine.xml

make a copy from entityengine-dev.xml and call it entityengine.xml
Under <OFBIZ_HOME>/framework/entity/config



### Build & Run Project

Go to OFBIZ_HOME directory 

##### Build
~~~
gradlew clean build 
OR
./gradlew clean build
~~~

##### Load Demo data
~~~
gradlew loadDefault
OR 
./gradlew loadDefault
~~~

##### Run server
~~~
gradlew ofbiz
OR
./gradle ofbiz
~~~

Ctrl+C to stop server
 

---

#### AutoPatt Console

http://localhost:8080/portal

#### AutoPatt Admin Page

http://localhost:8080/admin
