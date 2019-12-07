package com.autopatt.portal.events;

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
import java.util.Map;

public class UserMgmtEvents {
    public final static String module = UserMgmtEvents.class.getName();
    public static final String SUCCESS = "success";
    public static final String ERROR = "error";

    public static String createUser(HttpServletRequest request, HttpServletResponse response) {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        HttpSession session = request.getSession();
        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

        String firstName = request.getParameter("firstname");
        String lastName = request.getParameter("lastname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // TODO: Validations - check for duplicate email

        try {
            // Create Party & Person
            Map<String, Object> createPersonResp = dispatcher.runSync("createPerson", UtilMisc.<String, Object> toMap("firstName", firstName,
                    "lastName", lastName,
                    "userLogin", userLogin));
            if(!ServiceUtil.isSuccess(createPersonResp)) {
                Debug.logError("Error creating new user for " + email, module);
                request.setAttribute("_ERROR_MESSAGE_", "Unable to add new user. ");
                return ERROR;
            }
            String partyId = (String) createPersonResp.get("partyId");

            // Create UserLogin
            Map<String,Object> userLoginCtx = UtilMisc.toMap("userLogin", userLogin);
            userLoginCtx.put("userLoginId", email);
            userLoginCtx.put("currentPassword", password);
            userLoginCtx.put("currentPasswordVerify", password);
            userLoginCtx.put("requirePasswordChange", "Y"); // enforce password change for new user
            userLoginCtx.put("partyId", partyId);

            Map<String, Object> createUserLoginResp = dispatcher.runSync("createUserLogin", userLoginCtx);
            if(!ServiceUtil.isSuccess(createUserLoginResp)) {
                Debug.logError("Error creating userLogin for " + email, module);
                request.setAttribute("_ERROR_MESSAGE_", "Unable to add new user. ");
                return ERROR;
            }
        } catch (GenericServiceException e) {
            e.printStackTrace();
            return ERROR;
        }
        request.setAttribute("createSuccess", "Y");
        return SUCCESS;
    }

}
