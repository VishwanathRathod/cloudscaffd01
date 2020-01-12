import org.apache.ofbiz.base.util.UtilMisc
import org.apache.ofbiz.base.util.UtilProperties
import org.apache.ofbiz.base.util.UtilValidate
import org.apache.ofbiz.entity.GenericValue
import org.apache.ofbiz.service.ServiceUtil
import org.apache.ofbiz.base.util.Debug

Properties SUBSCRIPTION_PROPERTIES = UtilProperties.getProperties("subscription.properties");
String productStoreId = SUBSCRIPTION_PROPERTIES.getProperty("autopatt.product.store", "AUTOPATT_STORE");

Debug.logInfo("-=-=-=- TEST GROOVY SERVICE -=-=-=-", "")
result = ServiceUtil.returnSuccess()
String module = "SampleEmailService.groovy"

String emailTo = context.emailTo;
if (emailTo) {
    Debug.logInfo("----- Sending email to: $emailTo -----", module)

    String emailType = "TEST_EMAIL"
    GenericValue productStoreEmailSetting = delegator.findOne("ProductStoreEmailSetting",
            UtilMisc.toMap("productStoreId",productStoreId, "emailType", emailType), false)

    if(UtilValidate.isNotEmpty(productStoreEmailSetting)) {
        Map bodyParameters = UtilMisc.toMap("yourName", context.yourName )

        dispatcher.runSync("sendMailFromScreen",
                UtilMisc.toMap("userLogin", userLogin,
                        "sendTo", emailTo,
                        "sendFrom", productStoreEmailSetting.getString("fromAddress"),
                        "subject", productStoreEmailSetting.getString("subject"),
                        "bodyScreenUri", productStoreEmailSetting.getString("bodyScreenLocation"),
                        "bodyParameters", bodyParameters));

        result.successMessage = (String) "Email Sent to [$emailTo] "
        result.result = "Email Sent"
    } else  {
        result.successMessage = (String) "No Product store email setting found"
        result.result = "Setting not found"
    }

} else {
    result.successMessage = (String) "Got no SendTo email id"
    result.result = (String) "Got no SendTo email id"
    Debug.logInfo("Got no SendTo email id", module)
}

return result
