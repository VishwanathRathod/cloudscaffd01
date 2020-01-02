import org.apache.ofbiz.party.party.PartyHelper
import com.autopatt.admin.utils.TenantCommonUtils
import com.autopatt.admin.utils.UserLoginUtils
import org.apache.ofbiz.base.util.UtilMisc
import org.apache.ofbiz.base.util.UtilValidate
import org.apache.ofbiz.entity.GenericValue
import org.apache.ofbiz.service.ServiceUtil
import com.autopatt.admin.utils.*
import com.autopatt.admin.constants.*;

adminDetails = delegator.findByAnd("PartyRoleAndPartyDetail", UtilMisc.toMap("roleTypeId", "AUTOPATT_ADMIN"), null, false);

List<Map> adminList = new ArrayList();
for(GenericValue apAdmin : adminDetails) {
    Map entry = UtilMisc.toMap("firstName", apAdmin.firstName);
    entry.put("lastName", apAdmin.lastName);

    String fullName = PartyHelper.getPartyName(delegator, apAdmin.partyId, false)
    entry.put("fullName", fullName);

    def adminUserLoginId = UserLoginUtils.getUserLoginIdForPartyId(delegator, apAdmin.partyId)
    entry.put("adminUserLoginId", adminUserLoginId)

    def adminUserLoginGV  = delegator.findOne("UserLogin", ["userLoginId": adminUserLoginId], false)
    String adminUserLoginEnabled = adminUserLoginGV.enabled;
    def userStatus = null;
    if(adminUserLoginEnabled == null) {
        userStatus = UserStatusConstants.INACTIVE
    } else if(adminUserLoginEnabled.equalsIgnoreCase("Y")){
        userStatus = UserStatusConstants.ACTIVE
    } else {
        if(adminUserLoginGV.disabledDateTime == null){
            userStatus = UserStatusConstants.SUSPENDED
        } else {
            userStatus = UserStatusConstants.LOCKED
        }
    }
    entry.put("userStatus", userStatus);
    adminList.add(entry);
}
context.adminUsers = adminList;

tenantOrgParties = delegator.findByAnd("TenantOrgParty", null, UtilMisc.toList("createdStamp") ,false);

def customers = new ArrayList();
if(UtilValidate.isNotEmpty(tenantOrgParties)) {
    for(GenericValue tenantOrg: tenantOrgParties) {
        Map<String,Object> customer = UtilMisc.toMap()

        customer.put("tenantId", tenantOrg.tenantId)
        customer.put("orgPartyId", tenantOrg.orgPartyId)
        customer.put("createdStamp", tenantOrg.createdStamp)

        def tenantDispatcher = TenantCommonUtils.getTenantDispatcherByOrgPartyId(tenantOrg.orgPartyId)
        def hasValidSubCheckResp = tenantDispatcher.runSync("hasValidSubscriptionCheck",
                UtilMisc.toMap("userLogin", UserLoginUtils.getSystemUserLogin(tenantDispatcher.getDelegator())))

        customer.put("hasActiveSubscription", hasValidSubCheckResp.get("hasPermission"))

        def getSubscriptionsResp = dispatcher.runSync("getSubscriptions",
                UtilMisc.toMap("orgPartyId", tenantOrg.orgPartyId,
                        "status", "ACTIVE",
                        "userLogin", userLogin))
        customers.add(customer)
    }
}
context.customers = customers;

products = delegator.findByAnd("Product", UtilMisc.toMap("productTypeId", "SUBSCRIPTION_PLAN"), null, false);

List<Map> plansList = new ArrayList();
for(GenericValue product : products) {
    Map entry = UtilMisc.toMap("productId", product.productId);
    entry.put("productName", product.productName);
    plansList.add(entry);
}
context.plans = plansList;