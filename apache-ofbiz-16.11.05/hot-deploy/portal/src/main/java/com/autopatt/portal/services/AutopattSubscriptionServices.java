package com.autopatt.portal.services;

import com.autopatt.admin.utils.TenantCommonUtils;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.*;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;

import java.util.List;
import java.util.Map;

/**
 * Checks user has valid subscription or not
 */
public class AutopattSubscriptionServices {

    public static final String module = AutopattSubscriptionServices.class.getName();
    private static final String MAX_USER_LOGINS = "maxUserLogins";

    /**
     * Checks user has valid subscription or not
     * implements permissionInterface
     *
     * @param ctx     The DispatchContext that this service is operating in.
     * @param context Map containing the input parameters.
     * @return Map with the result of the service, the output parameters.
     */
    public static Map<String, Object> hasValidSubscriptionCheck(DispatchContext ctx, Map<String, ? extends Object> context) {
        Delegator delegator = ctx.getDelegator();
        String tenantId = delegator.getDelegatorTenantId();
        GenericDelegator mainDelegator = (GenericDelegator) DelegatorFactory.getDelegator("default");

        Map<String,Object> response = UtilMisc.toMap();
        if (UtilValidate.isEmpty(tenantId)) {
            return ServiceUtil.returnFailure("Tenant Id is missing");
        }
        String orgPartyId = TenantCommonUtils.getOrgPartyId(mainDelegator, tenantId);
        if (UtilValidate.isEmpty(orgPartyId)) {
            return ServiceUtil.returnFailure("Org party Id is missing");
        }
        try {
            List<GenericValue> subscriptions = mainDelegator.findByAnd("Subscription", UtilMisc.toMap("partyId", orgPartyId), null, false);
            List<GenericValue> activeSubscriptions = EntityUtil.filterByDate(subscriptions);
            if (UtilValidate.isNotEmpty(activeSubscriptions)) {
                response.put("hasPermission", true);
            } else {
                response.put("hasPermission", false);
            }
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnFailure("Failed to fetch subscription, error: " + e.getMessage());
        }
        return response;
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
        String orgPartyId = TenantCommonUtils.getOrgPartyId(mainDelegator, tenantId);
        if (UtilValidate.isEmpty(orgPartyId)) {
            return ServiceUtil.returnFailure("Org party Id is missing");
        }

        try {
            List<GenericValue> subscriptions = mainDelegator.findByAnd("Subscription", UtilMisc.toMap("partyId", orgPartyId), null, false);
            List<GenericValue> activeSubscriptions = EntityUtil.filterByDate(subscriptions);
            for (GenericValue subscription : activeSubscriptions) {
                String productId = subscription.getString("productId");
                GenericValue productAttribute = delegator.findOne("ProductAttribute", UtilMisc.toMap("productId", productId, "attrName", MAX_USER_LOGINS), false);
                int maxUserLogins = Integer.parseInt(productAttribute.getString("attrValue"));
                Debug.logInfo("max user logins " + maxUserLogins, module);
                List<GenericValue> partyRoles = delegator.findByAnd("PartyRole", UtilMisc.toMap("roleTypeId", "EMPLOYEE"), null, false);
                if (partyRoles != null && partyRoles.size() < maxUserLogins) {
                    return ServiceUtil.returnSuccess();
                }
                return ServiceUtil.returnFailure("Exceeded max user limit");
            }
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnFailure("Failed to fetch subscription, error: " + e.getMessage());
        }
        return ServiceUtil.returnFailure("You don't have valid subscription");
    }

}
