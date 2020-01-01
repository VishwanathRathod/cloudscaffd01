<div>
    <div class="row ">
        <div class="col-md-6">
            <form class="form-inline">
                <div class="form-group mx-sm-3 mb-2 ">
                    <label for="filterUsersByStatus" class="p-2">Status</label>
                    <select class="form-control form-control-sm" id="filterUsersByStatus" name="status">
                        <option value="ALL">All</option>
                        <option value="ACTIVE" <#if status?? && status=="ACTIVE">selected</#if>>Active</option>
                        <option value="INACTIVE" <#if status?? && status=="INACTIVE">selected</#if>>Expired</option>
                        <option value="LOCKED" <#if status?? && status=="LOCKED">selected</#if>>Locked</option>
                        <option value="SUSPENDED" <#if status?? && status=="SUSPENDED">selected</#if>>Suspended</option>
                    </select>
                </div>

                <div class="form-group mx-sm-3 mb-2">
                    <label for="filterUsersByTenant" class="p-2">Tenant</label>
                    <select class="form-control form-control-sm" id="filterUsersByTenant" name="tenantId">
                        <option value="ALL">All</option>
                        <#list tenants as tenant>
                            <option value="${tenant.tenantId}" <#if tenantId?? && tenantId==tenant.tenantId>selected</#if>>
                                ${tenant.tenantId}
                            </option>
                        </#list>
                    </select>
                </div>
                <a href="javascript:void(0);" class="btn btn-outline-primary btn-sm mb-2"
                   onclick="filterUsersForReport()">Apply</a>
            </form>
        </div>
    </div>

    <hr/>
    <table class="table table-striped table-hover">
        <caption>Total Users - <b>${users!?size}</b></caption>
        <thead class="thead-dark">
        <tr>
            <th>#</th>
            <th>Tenant</th>
            <th>Name</th>
            <th>Email</th>
            <th>Date Created</th>
            <th>Role</th>
            <th >Status</th>
        </tr>
        </thead>
        <tbody>
        <#if users??>
            <#list users as user>
                <tr>
                    <td>${user_index + 1}</td>
                    <td>${user.tenantId!}</td>
                    <td class="user-name">
                        ${user.partyName!}
                    </td>
                    <td class="user-name">
                        <div class="">${user.userLogin.userLoginId!}</div>
                    </td>

                    <td><#if user.userLogin.createdStamp??>${user.userLogin.createdStamp!?date}</#if></td>
                    <td>${user.roleName!}</td>
                    <td width="15%">
                        <#if user.userStatus?? && user.userStatus == "ACTIVE">
                            <span class="status text-success" >&#8226;</span> <span>Active</span>
                        <#elseif user.userStatus?? && user.userStatus == "INACTIVE">
                            <div title="User hasn't logged in yet"><span class="status text-info">&bull;</span> In-Active </div>
                        <#elseif user.userStatus?? && user.userStatus == "LOCKED">
                            <div title="User locked due to failed logins"><span class="status text-warning">&bull;</span> Locked </div>
                        <#else>
                            <div title="User has been disabled"><span class="status text-danger">&bull;</span> Suspended </div>
                        </#if>
                    </td>
                </tr>
            </#list>
        </#if>
        </tbody>
    </table>
</div>