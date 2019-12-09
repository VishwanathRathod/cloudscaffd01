import org.apache.ofbiz.base.util.UtilMisc

tenantOrgParties = delegator.findByAnd("TenantOrgParty", null, UtilMisc.toList("createdStamp") ,false);
context.tenantOrgParties = tenantOrgParties;

//TODO: Fetch more details (Status, etc)
