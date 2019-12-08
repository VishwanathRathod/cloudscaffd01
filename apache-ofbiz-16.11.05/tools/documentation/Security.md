## Security - Groups and Permissions

#### Definition:
* SecurityGroup -- Define groups 
* SecurityPermission -- define list of permissions
* SecurityGroupPermission -- associate permissions with groups
* UserLoginSecurityGroup -- associate userLogin account with Security group

#### Security Groups 

1. AP_PLANNER - Planner
2. AP_APPROVER - Approver
3. AP_DEPLOYER - Deployer
4. AP_FULLADMIN - Administrator

##### Services / Java or Groovy

Ways to check if currently logged in person has given permission
~~~
Security security = dctx.getSecurity();
if (!security.hasPermission("PORTAL_VIEW_USERS", userLogin)) {
        return ServiceUtil.returnError("No permissions error");
}
~~~
##### FTL
~~~
<#if security.hasEntityPermission("PORTAL", "_ADD_USER", session)>
    ...
</#if>
~~~

##### Screens
~~~
<condition>
    <if-has-permission permission="PORTAL" action="_VIEW_USERS"/>
</condition>
<widgets>
    <platform-specific>
        <html>
            <html-template
                    location="component://portal/webapp/portal/pages/users/manage_users.ftl" />
        </html>
    </platform-specific>
</widgets>
<fail-widgets>
    <platform-specific>
        <html>
            <html-template
                    location="component://portal/webapp/portal/error/no_permission.ftl" />
        </html>
    </platform-specific>
</fail-widgets>

~~~