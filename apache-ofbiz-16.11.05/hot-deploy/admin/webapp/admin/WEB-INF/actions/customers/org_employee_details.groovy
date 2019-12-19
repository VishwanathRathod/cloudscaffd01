import org.apache.ofbiz.base.util.UtilMisc
import org.apache.ofbiz.base.util.UtilValidate
import org.apache.ofbiz.entity.DelegatorFactory
import org.apache.ofbiz.entity.GenericDelegator

String orgPartyId = parameters.orgPartyId
String partyId = parameters.partyId
context.orgPartyId = orgPartyId;
context.partyId = partyId;

tenantOrgParties = delegator.findByAnd("TenantOrgParty", UtilMisc.toMap("orgPartyId", orgPartyId), null, false);
if(UtilValidate.isNotEmpty(tenantOrgParties)) {
    tenantOrg = tenantOrgParties.get(0);

    String tenantId = tenantOrg.tenantId

    // Use Customer specific delegator
    tenantDelegator = (GenericDelegator) DelegatorFactory.getDelegator("default#" + tenantId);

    // TODO: Fetch User details (firstname, lastname, email, role) ---- use tenantDelegator (NOT delegator)
    Map inputs = UtilMisc.toMap("partyId", partyId)
    employee = tenantDelegator.findOne("Person", inputs, false)
    context.employee = employee

    partyUserLogins = tenantDelegator.findByAnd("UserLogin",inputs, null, false)
    if(partyUserLogins != null && partyUserLogins.size()>0 ) {
        partyUserLogin = partyUserLogins.get(0)
        context.email = partyUserLogin.userLoginId

    }
}


