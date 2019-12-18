<div>
    <form class="form-inline">
        <div class="form-group mx-sm-3 mb-2 ">
            <label for="filterSubscriptionsByStatus" class="p-2">Status</label>
            <select class="form-control form-control-sm" id="filterSubscriptionsByStatus">
                <option>All</option>
                <option>Active</option>
                <option>Expired</option>
            </select>
        </div>

        <div class="form-group mx-sm-3 mb-2">
            <label for="filterSubscriptionsByProduct" class="p-2">Product</label>
            <select class="form-control form-control-sm" id="filterSubscriptionsByProduct">
                <option>All</option>
                <option>Demo</option>
                <option>Basic Planner - P1</option>
                <option>Advanced Planner - P2</option>
                <option>Enterprise Planner - EP1</option>
            </select>
        </div>
        <button class="btn btn-outline-primary btn-sm mb-2">Apply</button>
    </form>
</div>

<p>
<hr/>
</p>
<div>
    <table class="table table-striped table-hover">
        <caption>Total Subscriptions - <b>${subscriptions!?size}</b> of 5 <span class="small text-muted">(max)</span>
        </caption>
        <thead class="thead-dark">
        <tr>
            <th>#</th>
            <th>Product</th>
            <th>Date Created</th>
            <th>From Date</th>
            <th>Thru Date</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <#if subscriptions??>
            <#list subscriptions as subscription>
                <tr>
                    <td>${subscription_index + 1}</td>
                    <td class="user-name">
                        <i class="material-icons" style="font-size:1.6em;">account_circle</i>
                        ${subscription.productId}
                    </td>
                    <td>${subscription.createdDate}</td>
                    <td>${subscription.fromDate}</td>
                    <td>
                        <#if subscription.thruDate??>${subscription.thruDate}
                        <#else>
                            NA
                        </#if>
                    </td>
                    <td>
                        <#if subscription.status?? && subscription.status == "ACTIVE">
                            <span class="status text-success" >&#8226;</span> <span>Active</span>
                        <#else>
                            <span class="status text-warning">&bull;</span> Inactive
                        </#if>
                    </td>
                    <td width="20%">
                        <#if subscription.status?? && subscription.status == "ACTIVE">
                            <a href="#"
                               data-target="#suspendEmployeeConfirmModal"
                               class="btn btn-outline-danger" title="Suspend" data-toggle="modal"
                               data-party-id="${orgPartyId}" data-party-name="${subscription.productId!}"
                               data-org-party-id="${orgPartyId!}">
                                <i class="fa fa-lock" aria-hidden="true"></i>
                            </a>
                        <#else>
                            <a href="#"
                               data-target="#activateEmployeeConfirmModal"
                               class="btn btn-outline-primary" title="Enable" data-toggle="modal"
                               data-party-id="${orgPartyId}" data-party-name="${subscription.productId!}"
                               data-org-party-id="${orgPartyId!}">
                                <i class="fa fa-unlock" aria-hidden="true"></i>
                            </a>
                        </#if>
                </tr>
            </#list>
        </#if>
        </tbody>
    </table>
</div>

