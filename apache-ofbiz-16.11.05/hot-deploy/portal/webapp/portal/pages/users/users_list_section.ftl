
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
                <td width="30%">
                    <a href="<@ofbizUrl>edit_user?partyId=${user.partyId!}</@ofbizUrl>" class="btn btn-outline-primary" title="Edit" data-toggle="tooltip">
                        <i  class="fa fa-edit" aria-hidden="true"></i></a>
                    <#if user.userStatus?? && user.userStatus == "ACTIVE" && user.partyId != userLogin.partyId>
                        <a href="#"
                           data-target="#suspendUserConfirmModal"
                           class="btn btn-outline-danger" title="Suspend" data-toggle="modal"
                           data-party-id="${user.partyId}" data-party-name="${user.partyName!}">
                            <i class="fa fa-lock" aria-hidden="true"></i>
                        </a>
                        <#if user.partyId != userLogin.partyId>
                        <a href="#"
                           data-target="#resetPasswordUserConfirmModal"
                           class="btn btn-outline-info" title="Reset Password" data-toggle="modal"
                           data-party-id="${user.partyId}" data-party-name="${user.partyName!}"
                           data-user-login-id="${user.userLogin.userLoginId!}">
                            <i class="fa fa-key" aria-hidden="true"></i>
                        </a>
                        </#if>
                    <#elseif user.partyId != userLogin.partyId>
                        <a href="#"
                           data-target="#activateUserConfirmModal"
                           class="btn btn-outline-success" title="Activate" data-toggle="modal"
                           data-party-id="${user.partyId}" data-party-name="${user.partyName!}">
                            <i class="fa fa-unlock" aria-hidden="true"></i>
                        </a>
                    </#if>
                    <#if user.partyId != userLogin.partyId>
                        <a href="#" class="btn btn-outline-danger" title="Remove" data-toggle="modal"
                           data-target="#deleteUserConfirmModal"
                           data-party-id="${user.partyId}" data-party-name="${user.partyName!}">
                            <i class="fa fa-trash-o" aria-hidden="true"></i>
                        </a>
                    </#if>

                </td>
            </tr>
        </#list>
    </#if>
    </tbody>
</table>

<form id="suspend_user_form" action="<@ofbizUrl>ajaxSuspendUser</@ofbizUrl>">
    <input type="hidden" id="suspendUser_partyId">
</form>

<form id="enable_user_form" action="<@ofbizUrl>ajaxActivateUser</@ofbizUrl>">
    <input type="hidden" id="enableUser_partyId">
</form>

<div class="modal fade" id="activateUserConfirmModal" tabindex="-1" role="dialog" aria-labelledby="activateUserModal" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">Confirm Activate</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                Are you sure you want to activate account for <b><span id="activateUserPartyName"></span></b>?
                <br/>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-success" onclick="activateUser()">Activate</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="suspendUserConfirmModal" tabindex="-1" role="dialog" aria-labelledby="suspendUserModal" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">Confirm Suspend</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                Are you sure you want to suspend account for <b><span id="suspendUserPartyName"></span></b>?
                <br/>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-danger" onclick="suspendUser()">Suspend</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="resetPasswordUserConfirmModal" tabindex="-1" role="dialog" aria-labelledby="resetPasswordUserModal" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">Confirm Password Reset</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                Are you sure you want to reset password for <b><span id="resetPasswordForPartyName"></span></b>?
                <br/>
                <small>An email will be sent to user with a link to set their password.</small>
            </div>
            <input type="hidden" name="resetPasswordUserLoginId" id="resetPasswordUserLoginId" value=""/>
            <input type="hidden" name="resetPasswordForPartyId" id="resetPasswordForPartyId" value=""/>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-primary" onclick="initResetUserPwd()">Reset Password</button>
            </div>
        </div>
    </div>
</div>


