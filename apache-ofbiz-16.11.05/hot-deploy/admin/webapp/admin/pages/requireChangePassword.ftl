
<#if requestAttributes.errorMessageList?has_content><#assign errorMessageList=requestAttributes.errorMessageList></#if>
<#if requestAttributes.eventMessageList?has_content><#assign eventMessageList=requestAttributes.eventMessageList></#if>
<#if requestAttributes.serviceValidationException??><#assign serviceValidationException = requestAttributes.serviceValidationException></#if>
<#if requestAttributes.uiLabelMap?has_content><#assign uiLabelMap = requestAttributes.uiLabelMap></#if>

<#if !errorMessage?has_content>
    <#assign errorMessage = requestAttributes._ERROR_MESSAGE_!>
</#if>
<#if !errorMessageList?has_content>
    <#assign errorMessageList = requestAttributes._ERROR_MESSAGE_LIST_!>
</#if>
<#if !eventMessage?has_content>
    <#assign eventMessage = requestAttributes._EVENT_MESSAGE_!>
</#if>
<#if !eventMessageList?has_content>
    <#assign eventMessageList = requestAttributes._EVENT_MESSAGE_LIST_!>
</#if>

<div class="container-fluid">

    <div class="login-sidenav">
        <#include "./common/preauth_logo.ftl"/>
        <div class="login-main-text">
            <h2>AutoPatt Admin <br> Login </h2>
            <p>In order to <b>protect your account</b>, we require you to change your password as you have just logged in using a temporary password.</p>
        </div>
    </div>
    <div class="login-main">
        <div class="col-md-6 col-sm-12">
            <div class="login-form">
                <h3>Change Your Password</h3>
                <div><hr/></div>
                <br/>
                <div>
                    <#list errorMessageList as error>
                        <div class="alert alert-danger" role="alert">
                            ${error}
                        </div>
                    </#list>
                </div>

                <form id="login" action="<@ofbizUrl>requireChangePassword</@ofbizUrl>" method="post">
                    <input type="hidden" name="requirePasswordChange" value="Y"/>
                    <input type="hidden" name="USERNAME" value="${requestAttributes.USERNAME!}"/>

                    <div class="form-group">
                        <label>Current Password</label>
                        <input type="password" class="form-control" placeholder="" name="PASSWORD" required>
                    </div>
                    <div class="form-group">
                        <label>New Password</label>
                        <input type="password" class="form-control" placeholder="" name="newPassword" required>
                    </div>
                    <div class="form-group">
                        <label>Confirm Password</label>
                        <input type="password" class="form-control" placeholder="" name="newPasswordVerify" required>
                    </div>
                    <button type="submit" class="btn btn-primary">Change</button>
                </form>
            </div>
        </div>
    </div>


