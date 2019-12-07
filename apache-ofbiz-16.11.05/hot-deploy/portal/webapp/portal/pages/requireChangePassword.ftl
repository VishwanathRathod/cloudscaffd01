
<style>
    .login-sidenav {
        height: 100%;
        background-color: #763965    ;
        overflow-x: hidden;
        padding-top: 20px;
    }
    .login-main {
        padding: 0px 10px;
    }
    @media screen and (max-width: 450px) {
        .login-form{
            margin-top: 10%;
        }
    }
    @media screen and (min-width: 768px){
        .login-main{
            margin-left: 40%;
        }
        .login-sidenav{
            width: 40%;
            position: fixed;
            z-index: 1;
            top: 0;
            left: 0;
        }
        .login-form{
            margin-top: 40%;
        }
    }
    .login-main-text{
        margin-top: 20%;
        padding: 60px;
        color: #fff;
    }
    .login-main-text h2{
        font-weight: 300;
    }
</style>


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
        <div class="login-main-text">
            <h2>AutoPatt Console<br> Login </h2>
            <p>Change Your Password</p>
        </div>
    </div>
    <div class="login-main">
        <div class="col-md-6 col-sm-12">
            <div>

                <div class="login-form">
                    <div>
                        <#list errorMessageList as error>
                            <div class="alert alert-danger" role="alert">
                                ${error}
                            </div>
                        </#list>
                    </div>
                    <form id="login" action="<@ofbizUrl>requireChangePassword</@ofbizUrl>" method="post">
                        <input type="hidden" name="requirePasswordChange" value="Y"/>
                        <input type="hidden" name="USERNAME" value="${requestAttributes.USERNAME}"/>
                        <div class="form-group">
                            <label>Current Password</label>
                            <input type="password" class="form-control" placeholder="Password" name="PASSWORD">
                        </div>
                        <div class="form-group">
                            <label>New Password</label>
                            <input type="password" class="form-control" placeholder="New Password" name="newPassword">
                        </div>
                        <div class="form-group">
                            <label>Verify Password</label>
                            <input type="password" class="form-control" placeholder="Verify Password" name="newPasswordVerify">
                        </div>
                        <button type="submit" class="btn btn-primary">Change</button>
                    </form>
                </div>
            </div>
        </div>
    </div>


