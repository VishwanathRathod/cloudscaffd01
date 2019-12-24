

## Multi-Tenant Setup Instructions

The steps in this document guides the developer/administrator to setup the infrastructure required to support multi-tenant operations.
> All the steps below needs to done only one per environment.

### Create Template Database

The template database is a special database instance which contains all the required tables, and seed data required for 
creating a new tenant.
When we are onboarding a new client (i.e creating a new tenant) we simply make a clone of template DB.
This is faster comparing to creating a database afresh, creating tables and loading data.

##### Windows
Go to folder: <OFBIZ_HOME>/tools/scripts/windows

Run create-template-db.cmd script

pass parameters: dbhost (localhost), db-username, db-password, and db-port
~~~
create-template-db.cmd localhost ofadmin oAdmin@123 3306
~~~
> Replace username, password values as per your configuration

##### Linux

Go to directory: <OFBIZ_HOME>/tools/scripts/linux

Run create-template-db shell script

~~~
sh create-template-db localhost ofadmin oAdmin@123 3306
~~~
> Replace host, username, password as per configuration

### Create Tenant entry for Template DB in Ofbiz

Windows
~~~
gradlew createTenant -PtenantId=template -PtenantReaders=seed,seed-initial -PdbPlatform=M -PdbUser=template_user -PdbPassword=template@321
~~~
Unix
~~~
./gradlew createTenant -PtenantId=template -PtenantReaders=seed,seed-initial -PdbPlatform=M -PdbIp=localhost -PdbUser=template_user -PdbPassword=template@321
~~~

The above command takes a while to create all the required tables, and load the needed seed-data.


