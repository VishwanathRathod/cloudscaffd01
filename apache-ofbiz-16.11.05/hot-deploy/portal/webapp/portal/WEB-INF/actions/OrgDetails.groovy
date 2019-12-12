import org.apache.ofbiz.base.util.UtilMisc
import org.apache.ofbiz.base.util.UtilValidate
import org.apache.ofbiz.entity.DelegatorFactory
import org.apache.ofbiz.entity.GenericDelegator

// Get Org details
tenantId = delegator.getDelegatorTenantId()
if(UtilValidate.isNotEmpty(tenantId)) {
    mainDelegator = (GenericDelegator) DelegatorFactory.getDelegator("default");

    tenantOrgParties = mainDelegator.findByAnd("TenantOrgParty", UtilMisc.toMap("tenantId", tenantId), null, false);
    if(UtilValidate.isNotEmpty(tenantOrgParties)) {
        tenantOrg =tenantOrgParties.get(0);
        context.orgParty = mainDelegator.findOne("Party", UtilMisc.toMap("partyId", tenantOrg.orgPartyId), false)
    }
}
context.tenantId = tenantId