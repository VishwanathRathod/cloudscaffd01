package com.autopatt.portal.events;

import com.autopatt.admin.utils.JWTHelper;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class PasswordMgmtEvents {

    public static final String SUCCESS = "success";
    public static final String ERROR = "error";
    public final static String module = PasswordMgmtEvents.class.getName();

    public static String sendPasswordResetLink(HttpServletRequest request, HttpServletResponse response) {
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        HttpSession session = request.getSession();
        String emailId = request.getParameter("USERNAME");

        Map<String, Object> result = null;
        try {
            result = dispatcher.runSync("generatePasswordResetToken", UtilMisc.<String, Object>toMap("userLoginId", emailId));
            if (!ServiceUtil.isSuccess(result)) {
                request.setAttribute("_ERROR_MESSAGE_", result.get("errorMessage"));
                return ERROR;
            }
        } catch (GenericServiceException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", "Failed to authenticate with current password");
            return ERROR;
        }
        System.out.println(result.get("token"));
        request.setAttribute("_EVENT_MESSAGE_", result.get("token"));
        return SUCCESS;
    }

    public static String validateJWTToken(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getParameter("token");
        request.setAttribute("token", token);
        Map<String, Object> result = JWTHelper.parseJWTToken(token);
        if (!ServiceUtil.isSuccess(result)) {
            request.setAttribute("_ERROR_MESSAGE_", result.get("errorMessage"));
            return ERROR;
        }
        return SUCCESS;
    }

    public static String resetPassword(HttpServletRequest request, HttpServletResponse response) {
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        String token = request.getParameter("token");
        String newPasswordVerify = request.getParameter("newPasswordVerify");
        String newPassword = request.getParameter("newPassword");
        request.setAttribute("token", token);

        Map<String, Object> result = JWTHelper.parseJWTToken(token);
        if (!ServiceUtil.isSuccess(result)) {
            request.setAttribute("_ERROR_MESSAGE_", result.get("errorMessage"));
            return ERROR;
        }
        String userLoginId = (String) result.get("USERNAME");
        String userTenantId = (String) result.get("userTenantId");
        try {
            result = dispatcher.runSync("resetPassword",
                    UtilMisc.<String, Object>toMap("userLoginId", userLoginId, "userTenantId", userTenantId,
                            "newPassword", newPassword, "newPasswordVerify", newPasswordVerify));
            if (!ServiceUtil.isSuccess(result)) {
                if (result.containsKey("errorMessage")) {
                    request.setAttribute("_ERROR_MESSAGE_", result.get("errorMessage"));
                } else {
                    request.setAttribute("_ERROR_MESSAGE_LIST_", result.get("errorMessageList"));
                }
                return ERROR;
            }
        } catch (GenericServiceException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", "Failed to update the password");
            return ERROR;
        }
        return SUCCESS;
    }
}
