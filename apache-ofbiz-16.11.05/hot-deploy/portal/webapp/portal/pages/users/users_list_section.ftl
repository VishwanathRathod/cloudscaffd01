
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
                    <#if user.partyId != userLogin.partyId>
                        <a href="#" class="delete" title="Remove" data-toggle="modal" data-target="#deleteUserConfirmModal"
                       data-party-id="${user.partyId!}" data-party-name="${user.partyName!}"><i class="material-icons">delete</i></a>
                    </#if>
                </td>
            </tr>
        </#list>
    </#if>
    </tbody>
</table>
