import org.apache.ofbiz.base.util.UtilMisc

// Get list of Org users (Role=Employee)


users = delegator.findByAnd("PartyRoleAndPartyDetail", UtilMisc.toMap("roleTypeId", "EMPLOYEE"), null, false);

context.users = users;