package com.autopatt.portal.services;

import com.autopatt.admin.services.CustomerOnboardingServices;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilDateTime;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Checks user has valid subscription or not
 *
 */
public class AutopattSubscriptionServices {

    public static final String module = CustomerOnboardingServices.class.getName();

    /**
     * Checks user has valid subscription or not
     * @param ctx The DispatchContext that this service is operating in.
     * @param context Map containing the input parameters.
     * @return Map with the result of the service, the output parameters.
     */
    public static Map<String, Object> hasValidSubscription(DispatchContext ctx, Map<String, ? extends Object> context) {
        Delegator delegator = ctx.getDelegator();
        String tenantId = (String) context.get("tenantId");
        String orgPartyId = "10121"; //find party id by tenant

        try {
            List<GenericValue> subscriptions = delegator.findByAnd("Subscription", UtilMisc.toMap("partyId", orgPartyId), null, false);
            if (UtilValidate.isNotEmpty(subscriptions) && subscriptions.size() > 0) {
                for (GenericValue subscription : subscriptions) {
                    Timestamp fromDate = subscription.getTimestamp("fromDate");
                    Timestamp thruDate = subscription.getTimestamp("thruDate");
                    Timestamp currentTimeStamp = UtilDateTime.nowTimestamp();
                    if (fromDate.before(currentTimeStamp) && (null == thruDate || thruDate.after(currentTimeStamp))) {
                        return ServiceUtil.returnSuccess();
                    }
                }
            }
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnFailure("Failed to fetch subscription, error: " + e.getMessage());
        }
        return ServiceUtil.returnFailure("You don't have valid subscription");
    }


}
