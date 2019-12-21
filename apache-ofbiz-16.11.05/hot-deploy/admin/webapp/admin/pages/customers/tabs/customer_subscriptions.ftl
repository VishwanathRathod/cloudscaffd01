

<div>
    <form class="form-inline">
        <input type="hidden" id="orgPartyId" name="orgPartyId" value="${orgPartyId}"/>
        <div class="form-group mx-sm-3 mb-2 ">
            <label for="filterSubscriptionsByStatus" class="p-2">Status</label>
            <select class="form-control form-control-sm" id="filterSubscriptionsByStatus" name="status">
                <option value="ALL">All</option>
                <option value="ACTIVE" <#if status?? && status=="ACTIVE">selected</#if>>Active</option>
                <option value="EXPIRED" <#if status?? && status=="EXPIRED">selected</#if>>Expired</option>
            </select>
        </div>

        <div class="form-group mx-sm-3 mb-2">
            <label for="filterSubscriptionsByProduct" class="p-2">Product</label>
            <select class="form-control form-control-sm" id="filterSubscriptionsByProduct"name="productId">
                <option value="ALL">All</option>
                <option value="Demo" <#if productId?? && productId=="Demo">selected</#if>>Demo</option>
                <option value="P1" <#if productId?? && productId=="P1">selected</#if>>Basic Planner - P1</option>
                <option value="P2" <#if productId?? && productId=="P2">selected</#if>>Advanced Planner - P2</option>
                <option value="EP1" <#if productId?? && productId=="EP1">selected</#if>>Enterprise Planner - EP1</option>
            </select>
        </div>
        <a href="javascript:void(0);" class="btn btn-outline-primary btn-sm mb-2" onclick="listSubscriptions()">Apply</a>
    </form>
</div>

<div>
    <table class="table table-striped table-hover">
        <#if subscriptions?? && subscriptions?size &gt; 0 >
        <caption>Total Subscriptions - <b>${subscriptions!?size}</b></caption>
        </#if>
        <thead class="thead-dark">
        <tr>
            <th>#</th>
            <th>Product</th>
            <th>Date Created</th>
            <th>Valid From</th>
            <th>Valid Till</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <#if subscriptions?? && subscriptions?size &gt; 0 >
            <#list subscriptions as subscription>
                <tr>
                    <td>${subscription_index + 1}</td>
                    <td class="user-name">
                        <i class="material-icons" style="font-size:1.6em;">cloud_circle</i>
                        ${subscription.productId}
                        <#assign subscribedProduct= (delegator.findOne("Product", {"productId" : subscription.productId}, false))/>
                        <#if subscribedProduct??>
                            - ${subscribedProduct.productName!}
                        </#if>
                    </td>
                    <td>${subscription.createdDate!?date}</td>
                    <td>${subscription.fromDate!?date}</td>
                    <td>
                        <#if subscription.thruDate??>${subscription.thruDate!?date}
                        <#else>
                            NA
                        </#if>
                    </td>
                    <td>
                        <#if subscription.status?? && subscription.status == "ACTIVE">
                            <span class="status text-success">&#8226;</span> <span>Active</span>
                        <#else>
                            <span class="status text-danger">&bull;</span> Expired
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
        <#else>
            <tr>
                <td colspan="10" align="center"> No subscriptions found.</td>
            </tr>
        </#if>
        </tbody>
    </table>
</div>

