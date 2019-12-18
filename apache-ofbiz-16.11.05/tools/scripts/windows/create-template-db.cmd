set hostname=%1
set dbusername=%2
set dbpassword=%3
set dbport=%4

mysql -h%hostname% -u%dbusername% -p%dbpassword% -e "CREATE USER 'template_user'@'%%' IDENTIFIED BY 'template@321';"
mysql -h%hostname% -u%dbusername% -p%dbpassword% -e "CREATE USER 'template_user'@'localhost' IDENTIFIED BY 'template@321';"

mysql -h%hostname% -u%dbusername% -p%dbpassword% -e "CREATE DATABASE ofbiz_template"
mysql -h%hostname% -u%dbusername% -p%dbpassword% -Dofbiz_template -e "GRANT ALL PRIVILEGES ON ofbiz_template.* TO 'template_user'@'%%' WITH GRANT OPTION;"
mysql -h%hostname% -u%dbusername% -p%dbpassword% -Dofbiz_template -e "GRANT ALL PRIVILEGES ON ofbiz_template.* TO 'template_user'@'localhost' WITH GRANT OPTION;"

mysql -h%hostname% -u%dbusername% -p%dbpassword% -e "CREATE DATABASE ofbizolap_template"
mysql -h%hostname% -u%dbusername% -p%dbpassword% -Dofbizolap_template -e "GRANT ALL PRIVILEGES ON ofbizolap_template.* TO 'template_user'@'%%' WITH GRANT OPTION;"
mysql -h%hostname% -u%dbusername% -p%dbpassword% -Dofbizolap_template -e "GRANT ALL PRIVILEGES ON ofbizolap_template.* TO 'template_user'@'localhost' WITH GRANT OPTION;"


