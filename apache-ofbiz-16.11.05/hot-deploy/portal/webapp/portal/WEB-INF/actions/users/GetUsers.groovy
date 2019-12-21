

getTenantUsersResp = dispatcher.runSync("getTenantUsers", ["userLogin": userLogin, "tenantId": delegator.getDelegatorTenantId()]);
// TODO: Check for error/failure

List users = getTenantUsersResp.get("users");
context.users = users;

//users = delegator.findByAnd("PartyRoleAndPartyDetail", UtilMisc.toMap("roleTypeId", "EMPLOYEE"), null, false);
//context.users = users;