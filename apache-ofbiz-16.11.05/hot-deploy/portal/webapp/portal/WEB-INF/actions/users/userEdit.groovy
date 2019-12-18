import org.apache.ofbiz.base.util.UtilMisc
import org.apache.ofbiz.base.util.UtilValidate
import org.apache.ofbiz.entity.GenericValue
import com.autopatt.common.utils.SecurityGroupUtils
import java.util.*;

partyId = request.getParameter("partyId")
context.partyId = partyId;

Map inputs = UtilMisc.toMap("partyId", partyId)
person = delegator.findOne("Person", inputs, false)
context.person = person

partyUserLogins = delegator.findByAnd("UserLogin", inputs, null, false)
if(partyUserLogins != null && partyUserLogins.size()>0) {
    partyUserLogin = partyUserLogins.get(0)
    context.email = partyUserLogin.userLoginId

    userSecurityGroups = delegator.findByAnd("UserLoginSecurityGroup", UtilMisc.toMap("userLoginId", partyUserLogin.userLoginId), null, false)

    if(UtilValidate.isNotEmpty(userSecurityGroups) && userSecurityGroups.size()>0) {
        GenericValue userSecurityGroup = userSecurityGroups.get(0);
        securityGroup = userSecurityGroup.getRelatedOne("SecurityGroup", false);
        context.userSecurityGroup = securityGroup;
    }
}


availableSecurityGroups = SecurityGroupUtils.getAvailableSecurityGroups(delegator);
context.availableSecurityGroups = availableSecurityGroups