import com.autopatt.admin.utils.UserLoginUtils
import org.apache.ofbiz.base.util.UtilMisc
import org.apache.ofbiz.base.util.UtilValidate
import org.apache.ofbiz.entity.GenericValue
import org.apache.ofbiz.party.party.PartyHelper
import org.apache.ofbiz.service.ServiceUtil
import java.sql.Timestamp;
import com.autopatt.admin.constants.UserStatusConstants;
import com.autopatt.admin.utils.TenantCommonUtils

adminDetails = delegator.findByAnd("PartyRoleAndPartyDetail", UtilMisc.toMap("roleTypeId", "AUTOPATT_ADMIN"), null, false);
String productId = parameters.productId

List<Map> adminList = new ArrayList()
for(GenericValue apAdmin : adminDetails) {

    Map entry = UtilMisc.toMap("firstName", apAdmin.firstName)
    entry.put("lastName", apAdmin.lastName)

    String fullName = PartyHelper.getPartyName(delegator, apAdmin.partyId, false)
    entry.put("fullName", fullName)

    def adminUserLoginId = UserLoginUtils.getUserLoginIdForPartyId(delegator, apAdmin.partyId)
    entry.put("adminUserLoginId", adminUserLoginId)

    def adminUserLoginGV  = delegator.findOne("UserLogin", ["userLoginId": adminUserLoginId], false)
    String adminUserLoginEnabled = adminUserLoginGV.enabled
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
    entry.put("userStatus", userStatus)
    adminList.add(entry)
}
context.adminUsers = adminList;

tenantOrgParties = delegator.findByAnd("TenantOrgParty", null, UtilMisc.toList("createdStamp") ,false);

context.totalCustomerCount = tenantOrgParties.size();

// TODO: Later fetch active customers, expired customers

products = delegator.findByAnd("Product", UtilMisc.toMap("productTypeId", "SUBSCRIPTION_PLAN"), null, false);
List<Map> plansList = new ArrayList();
for(GenericValue product : products) {
    Map entry = UtilMisc.toMap("productId", product.productId);
    entry.put("productName", product.productName);
    plansList.add(entry);
}
context.plans = plansList;