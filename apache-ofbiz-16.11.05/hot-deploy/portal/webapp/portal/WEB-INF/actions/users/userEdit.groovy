import org.apache.ofbiz.base.util.UtilMisc

partyId = request.getParameter("partyId")
context.partyId = partyId;

Map inputs = UtilMisc.toMap("partyId", partyId)
person = delegator.findOne("Person", inputs, false)
context.person = person

partyUserLogins = delegator.findByAnd("UserLogin", inputs, null, false)
if(partyUserLogins != null) {
    partyUserLogin = partyUserLogins.get(0)
    context.email = partyUserLogin.userLoginId
}
