import org.apache.ofbiz.base.util.UtilMisc
import org.apache.ofbiz.entity.GenericValue
import org.apache.ofbiz.party.party.PartyHelper

adminDetails = delegator.findByAnd("PartyRoleAndPartyDetail", UtilMisc.toMap("roleTypeId", "AUTOPATT_ADMIN"), null, false);

List<Map> adminList = new ArrayList();
for(GenericValue apAdmin : adminDetails) {
    Map entry = UtilMisc.toMap("firstName", apAdmin.firstName);
    entry.put("lastName", apAdmin.lastName);

    String fullName = PartyHelper.getPartyName(delegator, apAdmin.partyId, false)
    entry.put("fullName", fullName);

    // TODO: You will need partyId also, so that we can use for Edit & Delete user
    adminList.add(entry);
}
context.adminUsers = adminList;






