import com.autopatt.common.utils.SecurityGroupUtils
import org.apache.ofbiz.base.util.UtilMisc
import org.apache.ofbiz.entity.GenericValue

partyId = request.getParameter("partyId")
context.partyId = partyId;

Map inputs = UtilMisc.toMap("partyId", partyId)
person = delegator.findOne("Person", inputs, false)
context.admin = person

partyUserLogins = delegator.findByAnd("UserLogin", inputs, null, false)
if(partyUserLogins != null && partyUserLogins.size()>0) {
    partyUserLogin = partyUserLogins.get(0)
    context.email = partyUserLogin.userLoginId
}