import org.apache.ofbiz.base.util.UtilMisc

// Get person data from Database
Map inputs = UtilMisc.toMap("partyId", userLogin.partyId)
person = delegator.findOne("Person", inputs, false)

context.person = person;
context.email = userLogin.userLoginId;



