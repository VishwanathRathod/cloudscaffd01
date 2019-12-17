<div class="container-fluid">

    <div class="table-title">
        <div class="row">
            <div class="col-sm-6">
                <h2>Products</h2>
            </div>
            <div class="col-sm-6">
                </div>
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
             <td>${product.productId}</td>
             <td><a href="#"><i class="material-icons" style="font-size:1.6em;">cloud_circle</i>${product.productName}</a></td>
             <td>${product.productPrice}</td>
             <td>${product.productAttributes}</td>
             <td>
                 <a href="#" class="settings" title="Edit" data-toggle="tooltip"><i class="material-icons">edit</i></a>
             </td>
         </tr>
         </#list>
         </tbody>
     </table>
 </div>
