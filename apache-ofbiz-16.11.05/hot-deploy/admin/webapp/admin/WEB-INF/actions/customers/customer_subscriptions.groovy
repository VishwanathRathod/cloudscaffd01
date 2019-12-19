import org.apache.ofbiz.base.util.UtilMisc
import org.apache.ofbiz.service.ServiceUtil

String orgPartyId = parameters.orgPartyId

context.orgPartyId = orgPartyId;

// Get list of subscriptions for this customer
List subscriptions = new ArrayList();
resp = dispatcher.runSync("getSubscriptions",
        UtilMisc.<String, Object> toMap("orgPartyId", orgPartyId, "status", parameters.status, "productId", parameters.productId, "userLogin", userLogin));

if (ServiceUtil.isSuccess(resp)) {
    subscriptions = resp.get("subscriptions")
}

context.subscriptions = subscriptions;