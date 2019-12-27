import org.apache.ofbiz.base.util.*
import org.apache.ofbiz.base.util.UtilMisc
import org.apache.ofbiz.service.ServiceUtil
import org.apache.ofbiz.entity.GenericValue

String orgPartyId = parameters.orgPartyId
context.orgPartyId = orgPartyId;

String productId = parameters.productId
String status = parameters.status
if (UtilValidate.isEmpty(status)) status = "ACTIVE";

// Get list of subscriptions for this customer
List subscriptions = new ArrayList();
resp = dispatcher.runSync("getSubscriptions",
        UtilMisc.<String, Object> toMap("orgPartyId", orgPartyId, "status", status, "productId", productId, "userLogin", userLogin));

context.productId = productId
context.status = status

if (ServiceUtil.isSuccess(resp)) {
    subscriptions = resp.get("subscriptions")
}

context.subscriptions = subscriptions;

def products = delegator.findByAnd("Product", UtilMisc.toMap("productTypeId", "SUBSCRIPTION_PLAN"), null, false);
List<Map> plansList = new ArrayList()
for(GenericValue product : products) {
    Map entry = UtilMisc.toMap("productId", product.productId)
    entry.put("productName", product.productName)
    plansList.add(entry)
}
context.plans = plansList