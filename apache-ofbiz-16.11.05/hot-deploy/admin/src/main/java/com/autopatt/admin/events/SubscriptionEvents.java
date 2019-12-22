package com.autopatt.admin.events;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SubscriptionEvents {

    public final static String module = SubscriptionEvents.class.getName();
    public static final String SUCCESS = "success";
    public static final String ERROR = "error";

    public static String createSubscription(HttpServletRequest request, HttpServletResponse response) {

        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        HttpSession session = request.getSession();
        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
        String orgPartyId = request.getParameter("orgPartyId");
        String productId = request.getParameter("productId");
        String validFrom = request.getParameter("validFrom");
        String validTo = request.getParameter("validTo");

        Debug.log("Received request to assign product " + productId + " subscription to org party " + orgPartyId, module);
        Map<String, Object> resp = null;
        try {
            resp = dispatcher.runSync("assignSubscriptionToTenant",
                    UtilMisc.<String, Object>toMap("orgPartyId", orgPartyId,
                            "productId", productId, "validFrom", validFrom, "validTo", validTo,
                            "userLogin", userLogin));

            if (!ServiceUtil.isSuccess(resp)) {
                Debug.logError("Error assigning product " + productId + " subscription to org party " + orgPartyId, module);
                request.setAttribute("_ERROR_MESSAGE_", resp.get("errorMessage"));
                return ERROR;
            }
        } catch (GenericServiceException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", "Error subscribing org party. ");
            return ERROR;
        }
        return SUCCESS;
    }

    public static String revokeSubscription(HttpServletRequest request, HttpServletResponse response) {

        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        HttpSession session = request.getSession();
        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
        String subscriptionId = request.getParameter("subscriptionId");
        String validTo = request.getParameter("validTo");

        Debug.log("Received request to revoke subscription " + subscriptionId, module);
        Map<String, Object> resp = null;
        try {
            resp = dispatcher.runSync("updateSubscriptionThruDate",
                    UtilMisc.<String, Object>toMap("subscriptionId", subscriptionId, "validTo", validTo,
                            "userLogin", userLogin));

            if (!ServiceUtil.isSuccess(resp)) {
                Debug.logError("Error while revoking subscription " + subscriptionId, module);
                request.setAttribute("_ERROR_MESSAGE_", "Error while revoking subscribing tenant. ");
                return ERROR;
            }
        } catch (GenericServiceException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", "Error subscribing org party. ");
            return ERROR;
        }
        return SUCCESS;
    }
}
