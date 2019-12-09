package com.autopatt.portal.events;

import org.apache.commons.lang.StringUtils;
import org.apache.ofbiz.base.util.*;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ModelService;
import org.apache.ofbiz.service.ServiceUtil;
import org.apache.ofbiz.webapp.control.LoginWorker;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AutopattLoginWorker {

    public final static String module = AutopattLoginWorker.class.getName();

    public static String SUCCESS = "success";
    public static String ERROR = "error";

    public static String extensionCheckLogin(HttpServletRequest request, HttpServletResponse response) {
        String res = LoginWorker.extensionCheckLogin(request, response);
        if (!SUCCESS.equals(res)) {
            return res;
        }
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
        if (userLogin != null && StringUtils.isNotEmpty(userLogin.getString("userLoginId")) && StringUtils.isNotEmpty(sessionId)) {
            try {
                String userLoginId = userLogin.getString("userLoginId");
                GenericValue userLoginSessionInfo = delegator.findOne("UserLoginSessionInfo", false, "userLoginId", userLoginId);
                if (null != userLoginSessionInfo) {
                    String currentSesionId = userLoginSessionInfo.getString("sessionId");
                    if (sessionId.equals(currentSesionId)) {
                        return SUCCESS;
                    }
                    session.invalidate();
                }
            } catch (GenericEntityException e) {
                Debug.logError(e, "Exception during storing session id in UserLoginSessionInfo : " + e.getMessage(), module);
            }
        }
        String errMsg = "You have been logged out since you have logged in from another device or browser.";
        request.setAttribute("_ERROR_MESSAGE_", errMsg);
        return ERROR;
    }

    public static String login(HttpServletRequest request, HttpServletResponse response) {
        String res = LoginWorker.login(request, response);
        request.setAttribute("USERNAME",request.getParameter("USERNAME"));
        if (!SUCCESS.equals(res)) {
            return res;
        }
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        HttpSession session = request.getSession();
        String sessionId = session.getId();
        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
        if (userLogin != null && StringUtils.isNotEmpty(userLogin.getString("userLoginId")) && StringUtils.isNotEmpty(sessionId)) {
            try {
                String userLoginId = userLogin.getString("userLoginId");
                GenericValue userLoginSessionInfo = delegator.findOne("UserLoginSessionInfo", false, "userLoginId", userLoginId);
                if (null == userLoginSessionInfo) {
                    GenericValue userAccessToken = delegator.makeValue("UserLoginSessionInfo", UtilMisc.<String, Object>toMap(
                            "userLoginId", userLoginId,
                            "sessionId", sessionId));
                    delegator.create(userAccessToken);
                    return SUCCESS;
                }
                String currentSesionId = userLoginSessionInfo.getString("sessionId");
                if (currentSesionId.equals(sessionId)) {
                    return SUCCESS;
                }
                userLoginSessionInfo.setString("sessionId", sessionId);
                delegator.store(userLoginSessionInfo);
                return SUCCESS;
            } catch (GenericEntityException e) {
                Debug.logError(e, "Exception during storing session id in UserLoginSessionInfo : " + e.getMessage(), module);
            }
        }
        String errMsg = "Exception while managing One Device login feature";
        request.setAttribute("_ERROR_MESSAGE_", errMsg);
        return ERROR;
    }

    public static String changePassword(HttpServletRequest request, HttpServletResponse response) {
        String login = LoginWorker.login(request, response);
        return login;
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
            Debug.logError(e, "Error calling updatePassword service", module);
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
