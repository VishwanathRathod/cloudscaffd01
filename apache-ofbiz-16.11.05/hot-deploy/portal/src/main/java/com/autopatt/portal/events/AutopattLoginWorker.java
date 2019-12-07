package com.autopatt.portal.events;

import org.apache.commons.lang.StringUtils;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;
import org.apache.ofbiz.webapp.control.LoginWorker;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class AutopattLoginWorker {

    public final static String module = AutopattLoginWorker.class.getName();

    public static String SUCCESS = "success";
    public static String ERROR = "error";

    public static String extensionCheckLogin(HttpServletRequest request, HttpServletResponse response) {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String res = LoginWorker.extensionCheckLogin(request, response);
        if (!SUCCESS.equals(res)) {
            return res;
        }
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
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String res = LoginWorker.login(request, response);
        if (!SUCCESS.equals(res)) {
            return res;
        }
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
        return LoginWorker.login(request, response);
    }


}
