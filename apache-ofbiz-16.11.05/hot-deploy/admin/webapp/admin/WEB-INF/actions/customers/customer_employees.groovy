import org.apache.ofbiz.base.util.UtilMisc
import org.apache.ofbiz.base.util.UtilValidate
import org.apache.ofbiz.entity.DelegatorFactory
import org.apache.ofbiz.entity.GenericDelegator
import org.apache.ofbiz.entity.GenericValue

String orgPartyId = parameters.orgPartyId
context.orgPartyId = orgPartyId

// Get list of employees for this customer
tenantOrgParties = delegator.findByAnd("TenantOrgParty", UtilMisc.toMap("orgPartyId", orgPartyId), null, false);
if(UtilValidate.isNotEmpty(tenantOrgParties)) {
    tenantOrg =tenantOrgParties.get(0);

    String tenantId = tenantOrg.tenantId

    tenantDelegator = (GenericDelegator) DelegatorFactory.getDelegator("default#" + tenantId);
    partyRoles = tenantDelegator.findByAnd("PartyRole", UtilMisc.toMap("roleTypeId", "EMPLOYEE"), null, false);

    List employees =new ArrayList();
    for(GenericValue partyRole: partyRoles) {
        Map entry = UtilMisc.toMap()

        entry.putAll(partyRole)
        party = partyRole.getRelatedOne("Party", false);
        entry.putAll(party)

        person = party.getRelatedOne("Person", false);
        entry.putAll(person)

        userLogins = tenantDelegator.findByAnd("UserLogin", UtilMisc.toMap("partyId", party.partyId), null, false);
        if(UtilValidate.isNotEmpty(userLogins)) {
            userLoginEntry = userLogins.get(0);
            entry.put("userLogin", userLoginEntry);

            if(userLoginEntry.enabled == "Y") {
                entry.put("userStatus", "ACTIVE");
            } else {
                if( UtilValidate.isEmpty(userLoginEntry.disabledDateTime )) {
                    entry.put("userStatus", "SUSPENDED")
                } else {
                    entry.put("userStatus", "INACTIVE")
                }
            }

            userSecurityGroups = tenantDelegator.findByAnd("UserLoginSecurityGroup", UtilMisc.toMap("userLoginId", userLoginEntry.userLoginId), null, false)
            if(UtilValidate.isNotEmpty(userSecurityGroups)) {
                GenericValue userSecurityGroup = userSecurityGroups.get(0);
                securityGroup = userSecurityGroup.getRelatedOne("SecurityGroup", false);
                if(UtilValidate.isNotEmpty(securityGroup)) {
                    userRoleName = securityGroup.get("description")
                    entry.put("roleName", userRoleName)
                }
            }
        }


        employees.add(entry);
    }


    context.employees = employees;
}

