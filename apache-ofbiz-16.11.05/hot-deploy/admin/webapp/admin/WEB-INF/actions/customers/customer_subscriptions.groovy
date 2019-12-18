import org.apache.ofbiz.base.util.UtilMisc

String orgPartyId = parameters.orgPartyId

context.orgPartyId = orgPartyId;

// Get list of subscriptions for this customer
List subscriptions =new ArrayList();
resp = dispatcher.runSync("listSubscriptions", UtilMisc.<String, Object>toMap("orgPartyId", orgPartyId,
        "userLogin", userLogin));

if (resp != null && resp.get("responseMessage") != null) {
    subscriptions = resp.get("subscriptions")
}

context.subscriptions = subscriptions;

