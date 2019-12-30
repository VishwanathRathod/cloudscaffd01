
getTenantUsersResp = dispatcher.runSync("getTenantUsers", ["userLogin": userLogin, "tenantId": delegator.getDelegatorTenantId()]);

List users = getTenantUsersResp.get("users");
context.users = users;


