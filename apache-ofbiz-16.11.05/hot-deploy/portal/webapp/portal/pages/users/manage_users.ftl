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
                <#assign name = Static["org.apache.ofbiz.party.party.PartyHelper"].getPartyName(orgParty) />
                <tr>
                    <td>${user_index + 1}</td>
                    <td>
                    <a href="<@ofbizUrl>edit_user?partyId=${user.partyId!}</@ofbizUrl>"><i class="material-icons" style="font-size:1.6em;">account_circle</i> ${user.firstName!} ${user.lastName!}</a>
                    <#if user.partyId == userLogin.partyId> <span class="badge badge-primary">This is you</span></#if>
                    </td>
                    <td><#if user.createdDate??>${user.createdDate!?date}</#if></td>
                    <td>-</td>
                    <td><span class="status text-success" >&bull;</span> <span>Active</span></td>
                    <td>
                        <a href="<@ofbizUrl>edit_user?partyId=${user.partyId!}</@ofbizUrl>" class="settings" title="Edit" data-toggle="tooltip"><i class="material-icons">edit</i></a>
                        <a href="#" class="delete" title="Remove" data-toggle="modal" data-target="#deleteConfirmModal"
                           data-party-id="${user.partyId}" data-party-name="${user.firstName!} ${user.lastName!}"><i class="material-icons">delete</i></a>
                    </td>
                </tr>
            </#list>
        </#if>
        <#--<tr>
            <td>1</td>
            <td><a href="#"><i class="material-icons" style="font-size:1.6em;">account_circle</i> Michael Holz</a></td>
            <td>04/10/2013</td>
            <td>Admin</td>
            <td><span class="status text-success" >&bull;</span> <span>Active</span></td>
            <td>
                <a href="#" class="settings" title="Edit" data-toggle="tooltip"><i class="material-icons">edit</i></a>
                <a href="#" class="delete" title="Remove" data-toggle="modal" data-target="#deleteConfirmModal"
                data-party-id="michael" data-party-name="Michael Holz"><i class="material-icons">delete</i></a>
            </td>
        </tr>
        <tr>
            <td>2</td>
            <td><a href="#"><i class="material-icons" style="font-size:1.6em;">account_circle</i> Paula Wilson</a></td>
            <td>05/08/2014</td>
            <td>Publisher</td>
            <td><span class="status text-success">&bull;</span> Active</td>
            <td>
                <a href="#" class="settings" title="Edit" data-toggle="tooltip"><i class="material-icons">edit</i></a>
                <a href="#" class="delete" title="Remove" data-toggle="modal" data-target="#deleteConfirmModal"><i class="material-icons">delete</i></a>
            </td>
        </tr>
        <tr>
            <td>3</td>
            <td><a href="#"><i class="material-icons" style="font-size:1.6em;">account_circle</i> Antonio Moreno</a></td>
            <td>11/05/2015</td>
            <td>Publisher</td>
            <td><span class="status text-danger">&bull;</span> Suspended</td>
            <td>
                <a href="#" class="settings" title="Edit" data-toggle="tooltip"><i class="material-icons">edit</i></a>
                <a href="#" class="delete" title="Remove" data-toggle="modal" data-target="#deleteConfirmModal"><i class="material-icons">delete</i></a>
            </td>
        </tr>
        <tr>
            <td>4</td>
            <td><a href="#"><i class="material-icons" style="font-size:1.6em;">account_circle</i> Mary Saveley</a></td>
            <td>06/09/2016</td>
            <td>Reviewer</td>
            <td><span class="status text-success">&bull;</span> Active</td>
            <td>
                <a href="#" class="settings" title="Edit" data-toggle="tooltip"><i class="material-icons">edit</i></a>
                <a href="#" class="delete" title="Remove" data-toggle="modal" data-target="#deleteConfirmModal"><i class="material-icons">delete</i></a>
            </td>
        </tr>
        <tr>
            <td>5</td>
            <td><a href="#"><i class="material-icons" style="font-size:1.6em;">account_circle</i> Martin Sommer</a></td>
            <td>12/08/2017</td>
            <td>Moderator</td>
            <td><span class="status text-warning">&bull;</span> Inactive</td>
            <td>
                <a href="#" class="settings" title="Edit" data-toggle="tooltip"><i class="material-icons">edit</i></a>
                <a href="#" class="delete" title="Remove" data-toggle="modal" data-target="#deleteConfirmModal"><i class="material-icons">delete</i></a>
            </td>
        </tr>-->
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

