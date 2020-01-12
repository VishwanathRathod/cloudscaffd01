import org.apache.ofbiz.base.util.Debug
import org.apache.ofbiz.base.util.UtilMisc
import org.apache.ofbiz.base.util.UtilProperties
import org.apache.ofbiz.base.util.UtilValidate
import org.apache.ofbiz.entity.GenericValue
import org.apache.ofbiz.service.ServiceUtil

Properties SUBSCRIPTION_PROPERTIES = UtilProperties.getProperties("subscription.properties");
String productStoreId = SUBSCRIPTION_PROPERTIES.getProperty("autopatt.product.store", "AUTOPATT_STORE");


Debug.logInfo("-=-=-=- Sending New Customer Onboarded Email -=-=-=-", "")
result = ServiceUtil.returnSuccess()
String module = "NewCustomerOnboardedEmail.groovy"

String customerOrganizationName = context.customerOrganizationName
String customerContactPartyId = context.customerContactPartyId
String customerContactPartyName = context.customerContactPartyName
String customerContactEmail = context.customerContactEmail
String customerContactInitialPassword = context.customerContactInitialPassword


if (customerContactEmail) {
    Debug.logInfo("----- Sending email to: $customerContactEmail -----", module)

    String emailType = "NEW_CUST_ONBOARDED"
    GenericValue productStoreEmailSetting = delegator.findOne("ProductStoreEmailSetting",
            UtilMisc.toMap("productStoreId",productStoreId, "emailType", emailType), false)

    if(UtilValidate.isNotEmpty(productStoreEmailSetting)) {
        Map bodyParameters = UtilMisc.toMap("customerContactPartyId", customerContactPartyId )
        bodyParameters.put("customerContactEmail", customerContactEmail)
        bodyParameters.put("customerContactPartyName", customerContactPartyName)
        bodyParameters.put("customerContactInitialPassword", customerContactInitialPassword)
        bodyParameters.put("customerOrganizationName", customerOrganizationName)

        dispatcher.runSync("sendMailFromScreen",
                UtilMisc.toMap("userLogin", userLogin,
                        "sendTo", emailTo,
                        "sendFrom", productStoreEmailSetting.getString("fromAddress"),
                        "subject", productStoreEmailSetting.getString("subject"),
                        "bodyScreenUri", productStoreEmailSetting.getString("bodyScreenLocation"),
                        "bodyParameters", bodyParameters));

        result.successMessage = (String) "Email Sent to [$customerContactEmail] "
        result.result = "Email Sent"
    } else  {
        return ServiceUtil.returnFailure("No Product store email setting found")
    }

} else {
    Debug.logError("Got no SendTo email id", module)
    return ServiceUtil.returnFailure("Got no SendTo email id")
}

return result
