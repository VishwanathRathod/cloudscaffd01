import org.apache.ofbiz.base.util.UtilMisc
import org.apache.ofbiz.base.util.UtilValidate
import org.apache.ofbiz.party.party.PartyHelper

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

