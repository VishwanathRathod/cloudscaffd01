<div class="container-fluid">
    <#if requestParameters.updateSuccess?? && requestParameters.updateSuccess=="Y">
        <div class="alert alert-success" role="alert">
            <i class="material-icons">check</i> Plan details updated successfully.
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </#if>
    <div class="table-title">
        <div class="row">
            <div class="col-sm-6">
                <h4>Manage Plans</h4>
            </div>
            <div class="col-sm-6"></div>
        </div>
    </div>

 <div class="table-content">
     <table class="table table-striped table-hover">
         <thead>
         <tr>
             <th width="15%">Id</th>
             <th width="25%">Plan Name</th>
             <th class="number-right">Price ($)</th>
<#--             <th class="number-right">Max Admins</th>-->
             <th class="number-right">Max Users</th>
             <th class="number-right">Max Patterns</th>
             <th  >Actions</th>
         </tr>
         </thead>
         <tbody>
         <#if plans?? && plans?size &gt; 0>
             <#list plans as plan>
                 <tr>
                     <td>${plan.productId!}</td>
                     <td><a href="<@ofbizUrl>edit_plan?planId=${plan.productId!}</@ofbizUrl>">${plan.productName!}</a></td>
                     <td class="number-right">${plan.productPrice!?string.currency}</td>
                     <#--<td class="number-right">${plan.maxAdmins!}</td>-->
                     <td class="number-right">${plan.maxUserLogins!}</td>
                     <td class="number-right">${plan.maxPatterns!"0"}</td>
                     <td width="15%">
                         <a href="<@ofbizUrl>edit_plan?planId=${plan.productId!}</@ofbizUrl>" class="btn btn-outline-primary"
                            title="Edit Plan" data-toggle="tooltip"><i class="material-icons">edit</i></a>
                     </td>
                 </tr>
             </#list>
         <#else>
             <tr>
                 <td colspan="5" align="center">No plans configured</td>
             </tr>
         </#if>

         </tbody>
     </table>
 </div>
