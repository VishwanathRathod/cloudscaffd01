<div>
    <form action="<@ofbizUrl>subscriptionsExcelReport</@ofbizUrl>" method="post">
        <div class="row">
            <div class="col-md-6 form-inline">
                <div class="form-group mx-sm-3 mb-2">
                    <label for="filterSubscriptionsReportByStatus" class="px-2">Status</label>
                    <select class="form-control form-control-sm" id="filterSubscriptionsReportByStatus" name="status">
                        <option value="ALL">All</option>
                        <option value="ACTIVE" <#if status?? && status=="ACTIVE">selected</#if>>Active</option>
                        <option value="INACTIVE" <#if status?? && status=="INACTIVE">selected</#if>>Expired</option>
                    </select>
                </div>
                <div class="form-group mx-sm-3 mb-2">
                    <label for="filterSubscriptionReportByTenant" class="px-2">Customer</label>
                    <select class="form-control form-control-sm" id="filterSubscriptionReportByTenant" name="filterSubscriptionReportByTenant">
                        <option value="ALL">All</option>
                        <#list tenants as tenant>
                            <option value="${tenant.tenantId}"
                                    <#if tenantId?? && tenantId==tenant.tenantId>selected</#if>>
                                ${tenant.tenantId}
                            </option>
                        </#list>
                    </select>
                </div>
                <div class="form-group mx-sm-3 mb-2">
                    <label for="filterSubscriptionsReportByProduct" class="px-2">Plan</label>
                    <select class="form-control form-control-sm" id="filterSubscriptionsReportByProduct" name="filterSubscriptionsReportByProduct">
                        <option value="ALL">All</option>
                        <#list plans as plan>
                            <option value="${plan.productId}"
                                    <#if planId?? && planId==plan.productId>selected</#if>>${plan.productId}
                                - ${plan.productName}</option>
                        </#list>
                    </select>
                </div>
                <div class="form-group mx-sm-3 mb-2">
                    <a href="javascript:void(0);" class="btn btn-outline-primary btn-sm px-1"
                       onclick="filterSubscriptionsForReport()">Apply</a>
                </div>
            </div>
            <div class="col-md-6">
                <div class="form-group float-right justify-content-end">
                    <button type="submit" class="btn btn-outline-primary btn-sm mb-2"><i class="fa fa-file-excel-o" aria-hidden="true"></i>
                         <span>Export to Excel</span></button>
                </div>
            </div>
        </div>
    </form>

    <hr/>
    <div>
        <table class="table table-striped table-hover">
            <#if subscriptions?? && subscriptions?size &gt; 0 >
                <caption>Total Subscriptions - <b>${subscriptions!?size}</b></caption>
            </#if>
            <thead class="thead-dark">
            <tr>
                <th>#</th>
                <th>Customer</th>
                <th>Plan</th>
                <th>Date Created</th>
                <th>Valid From</th>
                <th>Valid Till</th>
                <th>Status</th>
            </tr>
            </thead>
            <tbody>
            <#if subscriptions?? && subscriptions?size &gt; 0 >
                <#list subscriptions as subscription>
                    <tr>
                        <td>${subscription_index + 1}</td>
                        <td>${subscription.tenantId!}</td>
                        <td class="user-name">
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
                        <td width="15%">
                            <#if subscription.status?? && subscription.status == "ACTIVE">
                                <span class="status text-success">&#8226;</span> <span>Active</span>
                            <#elseif subscription.status?? && subscription.status == "FUTURE">
                                <span class="status text-success">&#8226;</span> <span>Future</span>
                            <#else>
                                <span class="status text-danger">&bull;</span> <span>Expired</span>
                            </#if>
                        </td>
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
</div>