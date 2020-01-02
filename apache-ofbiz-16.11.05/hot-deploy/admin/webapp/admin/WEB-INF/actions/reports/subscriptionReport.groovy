import org.apache.ofbiz.base.util.UtilMisc
import org.apache.ofbiz.base.util.UtilValidate
import org.apache.ofbiz.entity.GenericValue
import org.apache.ofbiz.service.ServiceUtil
import org.apache.ofbiz.entity.DelegatorFactory
import org.apache.ofbiz.entity.GenericDelegator

String statusParam = null == parameters.status ? "ACTIVE" : parameters.status
String tenantIdParam = null == parameters.tenantId ? "ALL" : parameters.tenantId
String planIdParam = null == parameters.planId ? "ALL" : parameters.planId

context.status = statusParam
context.tenantId = tenantIdParam
context.planId = planIdParam

tenantOrgParties = delegator.findByAnd("TenantOrgParty", null, UtilMisc.toList("createdStamp"), false);

def subscriptions = new ArrayList();
def tenants = new ArrayList();
if (UtilValidate.isNotEmpty(tenantOrgParties)) {
    for (GenericValue tenantOrg : tenantOrgParties) {
        Map<String, Object> tenantEntry = UtilMisc.toMap();
        def tenantId = tenantOrg.tenantId;
        tenantEntry.put("tenantId", tenantId);
        tenantEntry.put("tenantOrgPartyId", tenantOrg.orgPartyId);
        tenants.add(tenantEntry);
        if (!"ALL".equals(tenantIdParam) && !tenantIdParam.equals(tenantId)) {
            continue;
        }
        resp = dispatcher.runSync("getSubscriptions",
                UtilMisc.<String, Object> toMap("orgPartyId", tenantOrg.orgPartyId, "status", statusParam, "productId", planIdParam, "userLogin", userLogin));

        if (ServiceUtil.isSuccess(resp)) {
            for (Map subscriptionEntry : resp.get("subscriptions")) {
                subscriptionEntry.put("tenantId", tenantId)
                subscriptionEntry.put("orgPartyId", tenantOrg.orgPartyId)
                subscriptions.add(subscriptionEntry)
            }
        }
    }
}

def products = delegator.findByAnd("Product", UtilMisc.toMap("productTypeId", "SUBSCRIPTION_PLAN"), null, false);
List<Map> plansList = new ArrayList()
for(GenericValue product : products) {
    Map entry = UtilMisc.toMap("productId", product.productId)
    entry.put("productName", product.productName)
    plansList.add(entry)
}

context.plans = plansList
context.subscriptions = subscriptions;
context.tenants = tenants;
