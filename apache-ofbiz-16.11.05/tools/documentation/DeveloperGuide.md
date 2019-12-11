

### Database Setup
Install Mysql DB (8.0)

Run below sql commands 
* CREATE DATABASE ofbiz;
* CREATE DATABASE ofbizolap;
* CREATE DATABASE ofbiztenant;
* CREATE USER ofbiz@localhost;

* SET PASSWORD FOR 'ofbiz'@'localhost' = 'ofbiz123';

* GRANT ALL PRIVILEGES ON \*.* to 'ofbiz'@'localhost';


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
