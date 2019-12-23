import org.apache.ofbiz.base.util.*
import org.apache.ofbiz.service.ServiceUtil

String orgPartyId = parameters.orgPartyId
context.orgPartyId = orgPartyId;

String productId = parameters.productId
String status = parameters.status
if(UtilValidate.isEmpty(status)) status = "ACTIVE";

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