<div class="container-fluid">
    <div>&nbsp;</div>
    <#if requestParameters.createSuccess?? && requestParameters.createSuccess=="Y">
        <div class="alert alert-success" role="alert">
            <i class="material-icons">check</i> User has been addedd successfully.
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </#if>

    <#if requestParameters.updateSuccess?? && requestParameters.updateSuccess=="Y">
        <div class="alert alert-success" role="alert">
            <i class="material-icons">check</i> User details updated successfully.
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </#if>

    <div class="table-title">
        <div class="row">
            <div class="col-sm-5">
                <h2>Users</h2>
            </div>
            <div class="col-sm-7">
                <#if security.hasEntityPermission("PORTAL", "_ADD_USER", session)>
                    <a href="<@ofbizUrl>new_user</@ofbizUrl>" class="btn btn-primary"><i class="material-icons">&#xE147;</i> <span>Add New User</span></a>
                </#if>
                <#--<a href="#" class="btn btn-primary"><i class="material-icons">&#xE24D;</i> <span>Export to Excel</span></a>-->
            </div>
        </div>
    </div>

    <table class="table table-striped table-hover">
        <thead>
        <tr>
            <th>#</th>
            <th>Name</th>
            <th>Date Created</th>
            <th>Role</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <#if users??>
            <#list users as user>
                <tr>
                    <td>${user_index + 1}</td>
                    <td>
                    <a href="<@ofbizUrl>edit_user?partyId=${user.partyId!}</@ofbizUrl>"><i class="material-icons" style="font-size:1.6em;">account_circle</i> ${user.partyName!}</a>
                    <#if user.partyId == userLogin.partyId> &nbsp;&nbsp;<span class="badge badge-primary">You</span></#if>
                        <div class="small text-muted">${user.userLogin.userLoginId!}</div>
                    </td>
                    <td><#if user.userLogin.createdStamp??>${user.userLogin.createdStamp!?date}</#if></td>
                    <td>${user.roleName!}</td>
                    <td>
                        <#if user.userStatus?? && user.userStatus == "ACTIVE">
                            <span class="status text-success" >&#8226;</span> <span>Active</span>
                        <#elseif user.userStatus?? && user.userStatus == "INACTIVE">
                            <div title="User hasn't logged in yet"><span class="status text-info">&bull;</span> In-Active</div>
                        <#elseif user.userStatus?? && user.userStatus == "LOCKED">
                            <div title="User locked due to failed logins"><span class="status text-warning">&bull;</span> Locked </div>
                        <#else>
                            <div title="User has been disabled"><span class="status text-danger">&bull;</span> Suspended </div>
                        </#if>
                    </td>
                    <td>
                        <a href="<@ofbizUrl>edit_user?partyId=${user.partyId!}</@ofbizUrl>" class="settings" title="Edit" data-toggle="tooltip"><i class="material-icons">edit</i></a>
                        <a href="#" class="delete" title="Remove" data-toggle="modal" data-target="#deleteConfirmModal"
                           data-party-id="${user.partyId}" data-party-name="${user.firstName!} ${user.lastName!}"><i class="material-icons">delete</i></a>
                    </td>
                </tr>
            </#list>
        </#if>
        </tbody>
    </table>
</div>


<div class="modal fade" id="deleteConfirmModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">Confirm Remove</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        Are you sure you want to remove <b><span id="deletePartyName"></span></b>?
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" data-dismiss="modal">Cancel</button>
        <button type="button" class="btn btn-danger">Remove</button>
      </div>
    </div>
  </div>
</div>

