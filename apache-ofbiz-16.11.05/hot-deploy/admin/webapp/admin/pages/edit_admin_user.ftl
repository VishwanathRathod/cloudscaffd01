<#if requestAttributes.errorMessageList?has_content><#assign errorMessageList=requestAttributes.errorMessageList></#if>
<#if requestAttributes.eventMessageList?has_content><#assign eventMessageList=requestAttributes.eventMessageList></#if>

<#if !errorMessage?has_content>
    <#assign errorMessage = requestAttributes._ERROR_MESSAGE_!>
</#if>
<#if !errorMessageList?has_content>
    <#assign errorMessageList = requestAttributes._ERROR_MESSAGE_LIST_!>
</#if>
<div class="container-fluid">
    <div>
        <#list errorMessageList as error>
            <div class="alert alert-danger" role="alert">
                ${error}
            </div>
        </#list>
    </div>
    <div class="table-title">
        <div class="row">
            <div class="col-sm-4">
                <h4>Edit Admin User</h4>
            </div>
            <div class="col-sm-8">
                <a href="<@ofbizUrl>manage_users</@ofbizUrl>" class="btn btn-primary"><i class="material-icons">keyboard_backspace</i> <span>Back</span></a>
                <#--<a href="#" class="btn btn-primary"><i class="material-icons">&#xE24D;</i> <span>Export to Excel</span></a>-->
            </div>
        </div>
    </div>
</div>

<form action="<@ofbizUrl>UpdateAdminUser</@ofbizUrl>" method="post">
    <input type="hidden" name="partyId" value="${partyId!}"/>
    <div class="col-md-9 my-3">
        <div class="form-group row required">
            <label for="fnamacc" class="col-sm-2 col-form-label">First Name <span class="mandatory">*</span></label>
            <div class="col-sm-10">
                <input type="text" class="form-control" value="${admin.firstName!}" name="firstname" required>
            </div>
        </div>
        <div class="form-group row">
            <label for="lnameacc" class="col-sm-2 col-form-label">Last Name <span class="mandatory">*</span></label>
            <div class="col-sm-10">
                <input type="text" class="form-control" value="${admin.lastName!}" name="lastname" required>
            </div>
        </div>
        <div class="form-group row">
            <label for="emailacc" class="col-sm-2 col-form-label ">E-mail</label>
            <div class="col-sm-10">
                <input type="email" class="form-control" readonly value="${email!}" name="email" readonly required >
            </div>
        </div>
        <div class="form-group row">
            <div class="col-sm-2">&nbsp;</div>
            <div class="col-sm-10">
                <button type="submit" class="btn btn-primary">Update</button>
                <a href="<@ofbizUrl>manage_users</@ofbizUrl>" class="btn btn-secondary">Cancel</a>
            </div>
        </div>
    </div>
    </div>
</form>
