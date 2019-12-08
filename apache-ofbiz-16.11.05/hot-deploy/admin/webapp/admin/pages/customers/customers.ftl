

<div class="container-fluid">

    <div class="table-title">
        <div class="row">
            <div class="col-sm-5">
                <h2>Customers</h2>
            </div>
            <div class="col-sm-7">
                <a href="<@ofbizUrl>new_customer</@ofbizUrl>" class="btn btn-primary"><i class="material-icons">&#xE147;</i> <span>New Customer</span></a>
                <#--<a href="#" class="btn btn-primary"><i class="material-icons">&#xE24D;</i> <span>Export to Excel</span></a>-->
            </div>
        </div>
    </div>

    <#if requestParameters.createInitiated?? && requestParameters.createInitiated=="Y">
        <div class="alert alert-success" role="alert">
            <i class="material-icons">check</i> Customer creation process has been initiated....
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </#if>

    <div class="table-content">
        <p> list of available tenants </p>
    </div>


</div>