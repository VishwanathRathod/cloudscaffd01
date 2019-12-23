<div class="container-fluid">
    <#if requestParameters.updateSuccess?? && requestParameters.updateSuccess=="Y">
        <div class="alert alert-success" role="alert">
            <i class="material-icons">check</i> Product details updated successfully.
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </#if>
    <div class="table-title">
        <div class="row">
            <div class="col-sm-6">
                <h4>Products</h4>
            </div>
            <div class="col-sm-6"></div>
        </div>
    </div>

 <div class="table-content">
     <table class="table table-striped table-hover">
         <thead>
         <tr>
             <th width="15%">#</th>
             <th>Product Name</th>
             <th>Price</th>
             <th>Features</th>
             <th>Actions</th>
         </tr>
         </thead>
         <tbody>
         <#list products as product>
         <tr>
             <td>${product.productId!}</td>
             <td><a href="<@ofbizUrl>edit_product?productId=${product.productId!}</@ofbizUrl>"><i class="material-icons" style="font-size:1.6em;">cloud_circle</i>${product.productName!}</a></td>
             <td>${product.productPrice!}</td>
             <td>
                 Admins: ${product.maxAdmins!}<br/>
                 Users: ${product.maxUserLogins!}<br/>
             </td>
             <td>
                 <a href="<@ofbizUrl>edit_product?productId=${product.productId!}</@ofbizUrl>" class="settings" title="Edit" data-toggle="tooltip"><i class="material-icons">edit</i></a>
             </td>
         </tr>
         </#list>
         </tbody>
     </table>
 </div>
