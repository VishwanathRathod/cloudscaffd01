package com.autopatt.portal.services;

import com.autopatt.admin.services.CustomerOnboardingServices;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilDateTime;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.*;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Checks user has valid subscription or not
 */
public class AutopattSubscriptionServices {

    public static final String module = CustomerOnboardingServices.class.getName();
    private static final String MAX_USER_LOGINS = "maxUserLogins";

    /**
     * Checks user has valid subscription or not
     *
     * @param ctx     The DispatchContext that this service is operating in.
     * @param context Map containing the input parameters.
     * @return Map with the result of the service, the output parameters.
     */
    public static Map<String, Object> hasValidSubscription(DispatchContext ctx, Map<String, ? extends Object> context) {
        Delegator delegator = ctx.getDelegator();
        String tenantId = delegator.getDelegatorTenantId();
        GenericDelegator mainDelegator = (GenericDelegator) DelegatorFactory.getDelegator("default");

        if (UtilValidate.isEmpty(tenantId)) {
            return ServiceUtil.returnFailure("Tenant Id is missing");
        }
        String orgPartyId = getOrgPartyId(tenantId, mainDelegator);
        if (UtilValidate.isEmpty(orgPartyId)) {
            return ServiceUtil.returnFailure("Org party Id is missing");
        }
        try {
            List<GenericValue> subscriptions = mainDelegator.findByAnd("Subscription", UtilMisc.toMap("partyId", orgPartyId), null, false);
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

    /**
     * Checks user has valid subscription to add new user
     *
     * @param ctx     The DispatchContext that this service is operating in.
     * @param context Map containing the input parameters.
     * @return Map with the result of the service, the output parameters.
     */
    public static Map<String, Object> hasValidSubscriptionToAddUser(DispatchContext ctx, Map<String, ? extends Object> context) {
        Delegator delegator = ctx.getDelegator();
        String tenantId = delegator.getDelegatorTenantId();
        GenericDelegator mainDelegator = (GenericDelegator) DelegatorFactory.getDelegator("default");

        if (UtilValidate.isEmpty(tenantId)) {
            return ServiceUtil.returnFailure("Tenant Id is missing");
        }
        String orgPartyId = getOrgPartyId(tenantId, mainDelegator);
        if (UtilValidate.isEmpty(orgPartyId)) {
            return ServiceUtil.returnFailure("Org party Id is missing");
        }

        try {
            List<GenericValue> subscriptions = mainDelegator.findByAnd("Subscription", UtilMisc.toMap("partyId", orgPartyId), null, false);
            if (UtilValidate.isNotEmpty(subscriptions) && subscriptions.size() > 0) {
                for (GenericValue subscription : subscriptions) {
                    Timestamp fromDate = subscription.getTimestamp("fromDate");
                    Timestamp thruDate = subscription.getTimestamp("thruDate");
                    Timestamp currentTimeStamp = UtilDateTime.nowTimestamp();
                    if (fromDate.before(currentTimeStamp) && (null == thruDate || thruDate.after(currentTimeStamp))) {
                        String productId = subscription.getString("productId");
                        GenericValue productAttribute = delegator.findOne("ProductAttribute", UtilMisc.toMap("productId", productId, "attrName", MAX_USER_LOGINS), false);
                        int maxUserLogins = Integer.parseInt(productAttribute.getString("attrValue"));
                        Debug.logInfo("max user logins " + maxUserLogins, module);
                        List<GenericValue> partyRoles = delegator.findByAnd("PartyRole", UtilMisc.toMap("roleTypeId", "EMPLOYEE"), null, false);
                        if (partyRoles != null && partyRoles.size()<maxUserLogins) {
                            return ServiceUtil.returnSuccess();
                        }
                        return ServiceUtil.returnFailure("Exceeded max user limit");
                    }
                }
            }
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnFailure("Failed to fetch subscription, error: " + e.getMessage());
        }
        return ServiceUtil.returnFailure("You don't have valid subscription");
    }

    /**
     *
     * @param tenantId
     * @param mainDelegator
     * @return orgPartyId
     */
    private static String getOrgPartyId(String tenantId, GenericDelegator mainDelegator) {
        String orgPartyId = null;
        try {
            List<GenericValue> tenantOrgParties = mainDelegator.findByAnd("TenantOrgParty", UtilMisc.toMap("tenantId", tenantId), null, false);
            if (UtilValidate.isNotEmpty(tenantOrgParties)) {
                GenericValue tenantOrg = tenantOrgParties.get(0);
                orgPartyId = tenantOrg.getString("orgPartyId");
            }
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            e.printStackTrace();
        }
        return orgPartyId;
    }
}
