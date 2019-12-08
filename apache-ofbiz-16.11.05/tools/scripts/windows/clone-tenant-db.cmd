set hostname=%1
set dbusername=%2
set dbpassword=%3
set dbport=%4

set tenantId=%5
set username=%6
set password=%7

echo 'Creating DB for tenant: %tenantId%'

mysql -h%hostname% -u%dbusername% -p%dbpassword% -e "CREATE USER '%username%'@'%%' IDENTIFIED BY '%password%';"
mysql -h%hostname% -u%dbusername% -p%dbpassword% -e "CREATE USER '%username%'@'localhost' IDENTIFIED BY '%password%';"

mysql -h%hostname% -u%dbusername% -p%dbpassword% -e "CREATE DATABASE ofbiz_%tenantId%"
mysql -h%hostname% -u%dbusername% -p%dbpassword% -Dofbiz_%tenantId% -e "GRANT ALL PRIVILEGES ON ofbiz_%tenantId%.* TO '%username%'@'%%' WITH GRANT OPTION;"
mysql -h%hostname% -u%dbusername% -p%dbpassword% -Dofbiz_%tenantId% -e "GRANT ALL PRIVILEGES ON ofbiz_%tenantId%.* TO '%username%'@'localhost' WITH GRANT OPTION;"

echo 'Copying schema and data from Template DB'
mysqldump  -h%hostname% -u%dbusername% -p%dbpassword% --quick ofbiz_template | mysql  -h%hostname% -u%dbusername% -p%dbpassword% ofbiz_%tenantId%


mysql -h%hostname% -u%dbusername% -p%dbpassword% -e "CREATE DATABASE ofbizolap_%tenantId%"
mysql -h%hostname% -u%dbusername% -p%dbpassword% -Dofbizolap_%tenantId% -e "GRANT ALL PRIVILEGES ON ofbizolap_%tenantId%.* TO '%username%'@'%%' WITH GRANT OPTION;"
mysql -h%hostname% -u%dbusername% -p%dbpassword% -Dofbizolap_%tenantId% -e "GRANT ALL PRIVILEGES ON ofbizolap_%tenantId%.* TO '%username%'@'localhost' WITH GRANT OPTION;"

mysqldump  -h%hostname% -u%dbusername% -p%dbpassword% --quick ofbizolap_template | mysql  -h%hostname% -u%dbusername% -p%dbpassword% ofbizolap_%tenantId%


REM USAGE: clone-tenant-db.cmd localhost root admin123 3306 t1 t1user t1pwd
