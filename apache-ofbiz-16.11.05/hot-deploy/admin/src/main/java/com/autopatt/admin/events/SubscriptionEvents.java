package com.autopatt.admin.events;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilDateTime;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

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
        String validFromStr = request.getParameter("validFrom");
        String validToStr = request.getParameter("validTo");

        Debug.log("Received request to assign product " + productId + " subscription to org party " + orgPartyId, module);
        Map<String, Object> resp = null;

        Timestamp validFrom;
        Timestamp validTo = null;
        try {
            TimeZone tz = TimeZone.getDefault();
            if (UtilValidate.isEmpty(validFromStr)) {
                validFrom = UtilDateTime.nowTimestamp();
            } else {
                validFrom = UtilDateTime.stringToTimeStamp(validFromStr, "yyyy-MM-dd", tz, null);
            }
            validFrom = UtilDateTime.getDayStart(validFrom);
            if (UtilValidate.isNotEmpty(validToStr)) {
                validTo = UtilDateTime.stringToTimeStamp(validToStr, "yyyy-MM-dd", tz, null);
                validTo = UtilDateTime.getDayEnd(validTo);
            }
            //check from date is greater than to date
            if (null != validTo && validFrom.after(validTo)) {
                Debug.logError("ValidFrom date is greater than ValidTo date", module);
                request.setAttribute("_ERROR_MESSAGE_", "ValidFrom date is greater than ValidTo date");
                return ERROR;
            }
        } catch (ParseException e) {
            Debug.logError(e, module);
            Debug.logError("Failed to parse From or To date", module);
            request.setAttribute("_ERROR_MESSAGE_", "Failed to parse From or To date");
            return ERROR;
        }

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
        String validToStr = request.getParameter("validTo");
        String immediate = request.getParameter("immediate");

        Debug.log("Received request to revoke subscription " + subscriptionId, module);
        Map<String, Object> resp = null;

        if("revokeLater".equals(immediate) && UtilValidate.isEmpty(validToStr)){
            Debug.logError("ValidTo date is mandatory if revoking later", module);
            request.setAttribute("_ERROR_MESSAGE_", "ValidTo date is mandatory if revoking later");
            return ERROR;
        }

        Timestamp validTo = null;
        try {
            TimeZone tz = TimeZone.getDefault();
            if("revokeLater".equals(immediate)) {
                validTo = UtilDateTime.stringToTimeStamp(validToStr, "yyyy-MM-dd", tz, null);
                validTo = UtilDateTime.getDayEnd(validTo);
            }else{
                validTo = UtilDateTime.nowTimestamp();
            }
        } catch (ParseException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", "Failed to parse ValidTo date");
            return ERROR;
        }

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
