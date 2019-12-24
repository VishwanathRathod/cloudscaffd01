import com.autopatt.common.utils.SecurityGroupUtils
import org.apache.ofbiz.base.util.UtilMisc
import org.apache.ofbiz.base.util.UtilValidate
import org.apache.ofbiz.entity.GenericValue

// Get person data from Database
Map inputs = UtilMisc.toMap("partyId", userLogin.partyId)
person = delegator.findOne("Person", inputs, false)

context.person = person;
context.email = userLogin.userLoginId;

def roleName = "";
GenericValue userSecurityGroup = SecurityGroupUtils.getUserActiveSecurityGroup(delegator, userLogin.userLoginId);
if(UtilValidate.isNotEmpty(userSecurityGroup)) {
    roleName = userSecurityGroup.getString("description")
}
context.roleName = roleName