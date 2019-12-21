import org.apache.ofbiz.base.util.UtilMisc
import org.apache.ofbiz.base.util.UtilValidate
import org.apache.ofbiz.entity.DelegatorFactory
import org.apache.ofbiz.entity.GenericDelegator

String orgPartyId = parameters.orgPartyId
context.orgPartyId = orgPartyId

// Get list of employees for this customer
tenantOrgParties = delegator.findByAnd("TenantOrgParty", UtilMisc.toMap("orgPartyId", orgPartyId), null, false);
if(UtilValidate.isNotEmpty(tenantOrgParties)) {
    tenantOrg =tenantOrgParties.get(0);

    String tenantId = tenantOrg.tenantId

    tenantDelegator = (GenericDelegator) DelegatorFactory.getDelegator("default#" + tenantId);
    partyRoles = tenantDelegator.findByAnd("PartyRole", UtilMisc.toMap("roleTypeId", "EMPLOYEE"), null, false);

    getTenantUsersResp = dispatcher.runSync("getTenantUsers", ["userLogin": userLogin, "tenantId": tenantId]);
    // TODO: Check for error/failure

    List employees = getTenantUsersResp.get("users");
    context.employees = employees;
}

