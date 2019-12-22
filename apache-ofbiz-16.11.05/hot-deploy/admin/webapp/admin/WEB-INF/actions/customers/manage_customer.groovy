import com.autopatt.admin.utils.TenantCommonUtils
import com.autopatt.admin.utils.UserLoginUtils
import org.apache.ofbiz.base.util.UtilMisc
import org.apache.ofbiz.base.util.UtilValidate
import org.apache.ofbiz.party.party.PartyHelper
import org.apache.ofbiz.service.ServiceUtil

String orgPartyId = parameters.orgPartyId

context.orgPartyId = orgPartyId;
String organizationName = PartyHelper.getPartyName(delegator, orgPartyId, false);

context.organizationName = organizationName

//TODO: Fetch more details (Status, etc)

tenantOrgParties = delegator.findByAnd("TenantOrgParty", UtilMisc.toMap("orgPartyId", orgPartyId), null, false);
if(UtilValidate.isNotEmpty(tenantOrgParties)) {
    def tenantOrg = tenantOrgParties.get(0);
    context.tenantId = tenantOrg.tenantId
}

def tenantDispatcher = TenantCommonUtils.getTenantDispatcherByOrgPartyId(orgPartyId)
def hasValidSubCheckResp = tenantDispatcher.runSync("hasValidSubscriptionCheck",
        UtilMisc.toMap("userLogin", UserLoginUtils.getSystemUserLogin(tenantDispatcher.getDelegator())))

context.put("hasActiveSubscription", hasValidSubCheckResp.get("hasPermission"))

def getSubscriptionsResp = dispatcher.runSync("getSubscriptions",
        UtilMisc.toMap("orgPartyId", orgPartyId, "status", "ACTIVE", "userLogin", userLogin))

List subscriptions = []
if(ServiceUtil.isSuccess(getSubscriptionsResp)) {
    subscriptions = getSubscriptionsResp.get("subscriptions")

}
context.put("subscriptions", subscriptions)