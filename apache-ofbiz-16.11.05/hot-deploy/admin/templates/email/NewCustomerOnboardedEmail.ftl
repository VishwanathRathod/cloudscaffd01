<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</head>
<body>

<img src="../static/logo/AutoPatt_mini.png" width="36px" align="left"/>

<p>
    Hello ${customerContactPartyName!},
</p>
<p>Your Organization Account has been created for <b>${customerOrganizationName!}</b></p>

<p>
    You have been identified as a administrator to manage this account, please use below details to login to the console.
</p>
<p>
    Username: ${customerContactEmail!}<br/>
    Password: ${customerContactInitialPassword!}<br/><br/>
    <i>Note: You will be asked to change the password when you login for first time.</i>
</p>

<p>
    Thank You<br/>
    AutoPatt Team
</p>

</body>
</html>