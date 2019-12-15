package com.autopatt.admin.services;

import com.autopatt.admin.utils.TenantCommonUtils;
import org.apache.ofbiz.base.util.*;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.order.shoppingcart.CartItemModifyException;
import org.apache.ofbiz.order.shoppingcart.ShoppingCart;
import org.apache.ofbiz.order.shoppingcart.ShoppingCartItem;
import org.apache.ofbiz.service.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * Product subscription to tenant
 */
public class SubscriptionServices {

    public static final String module = CustomerOnboardingServices.class.getName();
    private static Properties SUBSCRIPTION_PROPERTIES = UtilProperties.getProperties("subscription.properties");

    /**
     * Assigns subscription to tenant
     * @param ctx The DispatchContext that this service is operating in.
     * @param context Map containing the input parameters.
     * @return Map with the result of the service, the output parameters.
     */
    public static Map<String, Object> assignSubscriptionToTenant(DispatchContext ctx, Map<String, ? extends Object> context) {

        Map<String, Object> sendResp;
        LocalDispatcher dispatcher = ctx.getDispatcher();
        Delegator delegator = ctx.getDelegator();
        Locale locale = (Locale) context.get("locale");
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String tenantId = (String) context.get("tenantId");
        String productId = (String) context.get("productId");

        //load properties
        String productStoreId = SUBSCRIPTION_PROPERTIES.getProperty("autopatt.product.store", "AUTOPATT_STORE");
        String currency = SUBSCRIPTION_PROPERTIES.getProperty("autopatt.currency", "USD");

        Debug.logInfo("Initiating process to assign product "+productId+" subscription to tenant " + tenantId, module);
        String orderId;
        String orgPartyId = TenantCommonUtils.getOrgPartyId(delegator, tenantId);
        try {
            ShoppingCart cart = new ShoppingCart(delegator, productStoreId, null, locale, currency);
            try {
                cart.setUserLogin(userLogin, dispatcher);
            } catch (CartItemModifyException e) {
                Debug.logError(e, module);
                return ServiceUtil.returnError(e.getMessage());
            }
            cart.setOrderType("SALES_ORDER");
            cart.setOrderPartyId(orgPartyId);

            GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId", productId), false);
            ShoppingCartItem productItem = ShoppingCartItem.makeItem(Integer.valueOf(0), product, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ZERO, null,
                    null, null, null, null, null, null, null,
                    null, "PRODUCT_ORDER_ITEM", null, null, cart, Boolean.FALSE, Boolean.FALSE, null,
                    Boolean.TRUE, Boolean.TRUE);

            // save the order (new tx)
            Map<String, Object> createResp;
            createResp = dispatcher.runSync("createOrderFromShoppingCart", UtilMisc.toMap("shoppingCart", cart), 90, true);
            if (ServiceUtil.isError(createResp)) {
                return createResp;
            }

            orderId = (String) createResp.get("orderId");
            Map<String, Object> statusChangeRequestMap = UtilMisc.<String, Object>toMap("orderId", orderId, "statusId", "ORDER_APPROVED", "setItemStatus", "Y", "userLogin", userLogin);
            Map<String, Object> newSttsResult = null;

            newSttsResult = dispatcher.runSync("changeOrderStatus", statusChangeRequestMap);
            if (ServiceUtil.isError(newSttsResult)) {
                return newSttsResult;
            }
            sendResp = ServiceUtil.returnSuccess();
            sendResp.put("orderId", orderId);
        } catch (GenericServiceException | CartItemModifyException | GenericEntityException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnFailure("Unable to create order, error: " + e.getMessage());
        }

        try {
            GenericValue newSubscription = delegator.makeValue("Subscription");
            newSubscription.set("partyId", orgPartyId);
            newSubscription.set("productId", productId);
            newSubscription.set("orderId", orderId);
            newSubscription.set("fromDate", UtilDateTime.nowTimestamp());

            Map<String, Object> createSubscriptionMap = ctx.getModelService("createSubscription").makeValid(newSubscription, ModelService.IN_PARAM);
            createSubscriptionMap.put("userLogin", EntityQuery.use(delegator).from("UserLogin").where("userLoginId", "system").queryOne());

            Map<String, Object> createSubscriptionResult = dispatcher.runSync("createSubscription", createSubscriptionMap);
            if (ServiceUtil.isError(createSubscriptionResult)) {
                return createSubscriptionResult;
            }
            sendResp.put("subscriptionId", createSubscriptionResult.get("subscriptionId"));
        } catch (GenericEntityException | GenericServiceException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnFailure("Unable to create subscription model, error: " + e.getMessage());
        }
        return sendResp;
    }

}
