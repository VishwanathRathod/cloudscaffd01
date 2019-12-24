package com.autopatt.admin.events;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ModelService;
import org.apache.ofbiz.service.ServiceUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class MyProfileEvents {
    public final static String module = MyProfileEvents.class.getName();
    public static String SUCCESS = "success";
    public static String ERROR = "error";

    public static String updateMyProfile(HttpServletRequest request, HttpServletResponse response) {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        HttpSession session = request.getSession();
        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

        String firstname=request.getParameter("firstname");
        String lastname=request.getParameter("lastname");
        Map<String, Object> inputs = UtilMisc.toMap("partyId", userLogin.get("partyId"));
        try {
            GenericValue person = delegator.findOne("Person", inputs , false);
            person.set("firstName",firstname);
            person.set("lastName",lastname);
            delegator.store(person);
        } catch (GenericEntityException e) {
            e.printStackTrace();
            request.setAttribute("_ERROR_MESSAGE_", "Unable to update the profile details.");
            return ERROR;
        }
        request.setAttribute("_EVENT_MESSAGE_", "Profile details updated successfully.");
        return SUCCESS;
    }

    public static String updatePassword(HttpServletRequest request, HttpServletResponse response) {
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        HttpSession session = request.getSession();
        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
        String username = userLogin.getString("userLoginId");
        String password = request.getParameter("PASSWORD");
        Map<String, Object> inMap = UtilMisc.<String, Object>toMap("login.username", username, "login.password", password, "locale", UtilHttp.getLocale(request));
        inMap.put("userLoginId", username);
        inMap.put("currentPassword", password);
        inMap.put("newPassword", request.getParameter("newPassword"));
        inMap.put("newPasswordVerify", request.getParameter("newPasswordVerify"));
        Map<String, Object> resultPasswordChange = null;
        try {
            resultPasswordChange = dispatcher.runSync("updatePassword", inMap);
        } catch (GenericServiceException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", "Failed to authenticate with current password");
            return ERROR;
        }
        if (ServiceUtil.isError(resultPasswordChange)) {
            String errorMessage = (String) resultPasswordChange.get(ModelService.ERROR_MESSAGE);
            if (UtilValidate.isNotEmpty(errorMessage)) {
                request.setAttribute("_ERROR_MESSAGE_", errorMessage);
            }
            request.setAttribute("_ERROR_MESSAGE_LIST_", resultPasswordChange.get(ModelService.ERROR_MESSAGE_LIST));
            return ERROR;
        }
        return SUCCESS;
    }

}