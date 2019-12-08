<#if requestAttributes.errorMessageList?has_content><#assign errorMessageList=requestAttributes.errorMessageList></#if>
<#if requestAttributes.eventMessageList?has_content><#assign eventMessageList=requestAttributes.eventMessageList></#if>

<#if !errorMessage?has_content>
    <#assign errorMessage = requestAttributes._ERROR_MESSAGE_!>
</#if>
<#if !errorMessageList?has_content>
    <#assign errorMessageList = requestAttributes._ERROR_MESSAGE_LIST_!>
</#if>
<div class="container-fluid">
    <div>&nbsp;</div>
    <div class="table-title">
        <div class="row">
            <div class="col-sm-5">
                <h2>Change Password</h2>
            </div>
            <div class="col-sm-7">
            </div>
        </div>
    </div>
    <div >
        <#list errorMessageList as error>
            <div class="alert alert-danger" role="alert">
                ${error}
            </div>
        </#list>
    </div>
    <form id="login" action="<@ofbizUrl>updatePassword</@ofbizUrl>" method="post">
        <div class="col-md-8 my-4">
            <div class="form-group row required">
                <label for="CrrPswd" class="col-sm-3 col-form-label" >Current Password <span class="mandatory">*</span></label>
                <div class="col-sm-9">
                    <input type="password" class="form-control" placeholder="Current Password" name="PASSWORD" required>
                </div>
            </div>
            <div class="form-group row">
                <label for="nwpswd" class="col-sm-3 col-form-label">New Password <span class="mandatory">*</span></label>
                <div class="col-sm-9">
                <input type="password" class="form-control" placeholder="New Password" name="newPassword" required>
                </div>
            </div>
            <div class="form-group row">
                <label for="cnfnewswd" class="col-sm-3 col-form-label">Confirm New Password <span class="mandatory">*</span></label>
                <div class="col-sm-9">
                <input type="password" class="form-control" placeholder="Confirm Password" name="newPasswordVerify" required>
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-3">&nbsp;</div>
                    <div class="col-sm-9">
                  <button type="submit" class="btn btn-primary">Change Password</button>
                    </div>
                </div>
            </div>
    </form>
</div>


