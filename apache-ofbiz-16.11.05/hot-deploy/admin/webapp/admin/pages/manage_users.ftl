<div class="container-fluid">
    <div class="table-title">
        <div class="row">
            <div class="col-sm-5">
                <h4>Admin Users</h4>
            </div>
            <div class="col-sm-7">
                <a href="<@ofbizUrl>new_admin_user</@ofbizUrl>" class="btn btn-primary"><i class="material-icons">&#xE147;</i> <span>New Admin</span></a>
                <#--<a href="#" class="btn btn-primary"><i class="material-icons">&#xE24D;</i> <span>Export to Excel</span></a>-->
            </div>
        </div>
    </div>

    <div class="table-content">
        <table class="table table-striped table-hover">
            <thead>
            <tr>
                <th>#</th>
                <th>Name</th>
                <th>Date Created</th>
                <th>Status</th>
                <th>Action</th>
            </tr>
            </thead>
            <tbody>
            <#list adminUsers as admin>
                <tr>
                    <td>${admin_index + 1}</td>
                    <td><a href="<@ofbizUrl>edit_admin</@ofbizUrl>">${admin.fullName!}</a>
                        <div class="small text-muted">${admin.adminUserLoginId!}</div>
                    </td>
                    <td><#if admin.createdDate??>${admin.createdDate!?date}</#if></td>
                     <td>
                         <#if admin.userStatus?? && admin.userStatus == "ACTIVE">
                             <span class="status text-success" >&#8226;</span> <span>Active</span>
                         <#elseif admin.userStatus?? && admin.userStatus == "INACTIVE">
                             <div title="User hasn't logged in yet"><span class="status text-info">&bull;</span> In-Active</div>
                         <#elseif admin.userStatus?? && admin.userStatus == "LOCKED">
                             <div title="User locked due to failed logins"><span class="status text-warning">&bull;</span> Locked </div>
                         <#else>
                             <div title="User has been disabled"><span class="status text-danger">&bull;</span> Suspended </div>
                         </#if>
                     </td>
                    <td>
                      <a href="#" class="settings" title="Edit" data-toggle="tooltip"><i class="material-icons">edit</i></a>
                      <a href="#" class="delete" title="Delete" data-toggle="tooltip"><i class="material-icons">delete</i></a>
                    </td>
                  </tr>
            </#list>

            </tbody>
        </table>
    </div>


</div>