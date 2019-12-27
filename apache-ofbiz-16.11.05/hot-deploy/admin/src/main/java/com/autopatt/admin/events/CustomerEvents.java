package com.autopatt.admin.events;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;
import org.apache.ofbiz.entity.GenericEntityException;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class CustomerEvents {
    public final static String module = CustomerEvents.class.getName();
    public static final String SUCCESS = "success";
    public static final String ERROR = "error";

    public static String createCustomer(HttpServletRequest request, HttpServletResponse response) {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        HttpSession session = request.getSession();
        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

        Debug.log("Initiating the process to onboard new customer", module);

        String tenantId = request.getParameter("tenantId");
        String organizationName = request.getParameter("organizationName");
        String contactFirstName = request.getParameter("contactFirstName");
        String contactLastName = request.getParameter("contactLastName");
        String contactEmail = request.getParameter("contactEmail");
        String contactPassword = request.getParameter("contactPassword");
        String sendNotificationToContact = request.getParameter("sendNotificationToContact");
        if(UtilValidate.isEmpty(sendNotificationToContact)) sendNotificationToContact = "N";

        Map<String, Object> onboardCustomerResp = null;
        try {
            //Async call - use status on Org Party to know the result
            dispatcher.runAsync("onboardNewCustomer", UtilMisc.<String, Object> toMap("tenantId", tenantId,
                    "organizationName", organizationName,
                    "contactFirstName",contactFirstName,
                    "contactLastName",contactLastName,
                    "contactEmail",contactEmail,
                    "contactPassword",contactPassword,
                    "sendNotificationToContact",sendNotificationToContact,
                    "userLogin", userLogin));

            /*if(!ServiceUtil.isSuccess(onboardCustomerResp)) {
                Debug.logError("Error onboarding new customer with organization Id: " + tenantId, module);
                request.setAttribute("_ERROR_MESSAGE_", "Error onboarding new customer. ");
                return ERROR;
            }*/

        } catch (GenericServiceException e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }

    // TODO: add that method here.. no need of new Java file always


        public static String UpdateCustomerDetails(HttpServletRequest request, HttpServletResponse response) {
            HttpSession session = request.getSession();
            GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
            String orgPartyId = request.getParameter("orgPartyId");
            request.setAttribute("orgPartyId", orgPartyId);
            Delegator delegator = (Delegator) request.getAttribute("delegator");
            String organizationName = request.getParameter("organizationName");

            Map<String, Object> inputs = UtilMisc.toMap("partyId", orgPartyId);
            try {
                GenericValue partygroup = delegator.findOne("PartyGroup", inputs, false);
                partygroup.set("groupName", organizationName);
                delegator.store(partygroup);
            } catch (GenericEntityException e) {
                e.printStackTrace();
                request.setAttribute("_ERROR_MESSAGE_", "Unable to update the customer details.");
                return ERROR;
            }
            request.setAttribute("_EVENT_MESSAGE_", "Profile details updated successfully.");
            return SUCCESS;
        }
    }
