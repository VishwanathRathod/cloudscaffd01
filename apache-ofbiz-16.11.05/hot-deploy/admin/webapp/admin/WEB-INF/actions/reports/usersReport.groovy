import org.apache.ofbiz.base.util.UtilMisc
import org.apache.ofbiz.base.util.UtilValidate
import org.apache.ofbiz.entity.GenericValue
import org.apache.ofbiz.service.ServiceUtil
import org.apache.ofbiz.entity.DelegatorFactory
import org.apache.ofbiz.entity.GenericDelegator

String statusParam = null == parameters.status ? "ALL" : parameters.status
String tenantIdParam = null == parameters.tenantId ? "ALL" : parameters.tenantId

context.status = statusParam
context.tenantId = tenantIdParam

tenantOrgParties = delegator.findByAnd("TenantOrgParty", null, UtilMisc.toList("createdStamp"), false);

def users = new ArrayList();
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
        tenantDelegator = (GenericDelegator) DelegatorFactory.getDelegator("default#" + tenantOrg.tenantId);
        partyRoles = tenantDelegator.findByAnd("PartyRole", UtilMisc.toMap("roleTypeId", "EMPLOYEE"), null, false);

        getTenantUsersResp = dispatcher.runSync("getTenantUsers", ["userLogin": userLogin, "tenantId": tenantId]);
        if (!ServiceUtil.isSuccess(getTenantUsersResp)) {
            continue;
        }
        for (Map<String, Object> userEntry : getTenantUsersResp.get("users")) {
            userEntry.put("tenantId", tenantId)
            userEntry.put("orgPartyId", tenantOrg.orgPartyId)
            if ("ALL".equals(statusParam) || statusParam.equals(userEntry.get("userStatus"))) {
                users.add(userEntry);
            }
        }
    }
}
context.users = users;
context.tenants = tenants;
