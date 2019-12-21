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
    <div>&nbsp;</div>
    <div class="table-title">
        <div class="row">
            <div class="col-sm-4">
                <h2>Edit Product</h2>
            </div>
            <div class="col-sm-8">
                <a href="<@ofbizUrl>manage_products</@ofbizUrl>" class="btn btn-primary"><i class="material-icons">keyboard_backspace</i> <span>Back</span></a>
                <#--<a href="#" class="btn btn-primary"><i class="material-icons">&#xE24D;</i> <span>Export to Excel</span></a>-->
            </div>
        </div>
    </div>
</div>
<div class="col-sm-7">

</div>

<form action="<@ofbizUrl>updateProduct</@ofbizUrl>" method="post">
        <div class="col-md-9 my-3">
        <div class="form-group row required">
            <label for="productid" class="col-sm-2 col-form-label">Product Id <span class="mandatory">*</span></label>
            <div class="col-sm-10">
                <input type="text" class="form-control " readonly value="${product.productId!}" name="productId" required/>
            </div>
        </div>
        <div class="form-group row required">
            <label for="productm" class="col-sm-2 col-form-label">Product Name <span class="mandatory">*</span></label>
            <div class="col-sm-10">
                <input type="text" class="form-control" value="${product.productName!}" name="productName" required/>
            </div>
        </div>
        <div class="form-group row">
            <label for="productprice" class="col-sm-2 col-form-label ">Price <span class="mandatory">*</span></label>
            <div class="col-sm-10">
                <input  type="number" step="any" class="form-control"  value="${priceGv.price}" name="price"  >
            </div>
        </div>
        <div class="form-group row">
            <label for="productfeatures" class="col-sm-2 col-form-label">Max Admins <span class="mandatory">*</span></label>
            <div class="col-sm-10">
            <input type="text" class="form-control" placeholder="No. of Users"  value="${maxAdmins!}" name="" />
                </div>
        </div>
            <div class="form-group row">
                <label for="productfeatures" class="col-sm-2 col-form-label">Max Users <span class="mandatory">*</span></label>
            <div class="col-sm-10">
                <input type="text" class="form-control" placeholder="No. of Patterns" value="${maxUserLogins!}" name="" />
            </div>
                </div>


        <div class="form-group row">
            <div class="col-sm-2">&nbsp;</div>
            <div class="col-sm-10">
                <button type="submit" class="btn btn-primary">Update</button>
                <a href="<@ofbizUrl>manage_products</@ofbizUrl>" class="btn btn-secondary">Cancel</a>
            </div>
        </div>
    </div>
    </div>

</form>
