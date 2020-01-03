import org.apache.ofbiz.base.util.UtilMisc
import org.apache.ofbiz.base.util.UtilValidate
import org.apache.ofbiz.entity.DelegatorFactory
import org.apache.ofbiz.entity.GenericDelegator
import org.apache.ofbiz.party.party.PartyHelper

String orgPartyId = parameters.orgPartyId
context.orgPartyId = orgPartyId;
tenantId = delegator.getDelegatorTenantId()
if(UtilValidate.isNotEmpty(tenantId)) {
    mainDelegator = (GenericDelegator) DelegatorFactory.getDelegator("default");

    tenantOrgParties = mainDelegator.findByAnd("TenantOrgParty", UtilMisc.toMap("tenantId", tenantId), null, false);
    if(UtilValidate.isNotEmpty(tenantOrgParties)) {
        tenantOrg =tenantOrgParties.get(0);
        context.orgParty = mainDelegator.findOne("Party", UtilMisc.toMap("partyId", tenantOrg.orgPartyId), false)
        context.organizationName = mainDelegator.findOne("PartyGroup", UtilMisc.toMap("partyId", tenantOrg.orgPartyId), false)
        context.orgPartyId = tenantOrg.orgPartyId;
    }
}
context.tenantId = tenantId

