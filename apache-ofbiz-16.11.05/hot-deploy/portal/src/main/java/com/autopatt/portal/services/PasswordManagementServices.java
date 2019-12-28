package com.autopatt.portal.services;

import com.autopatt.common.utils.JWTHelper;
import com.autopatt.admin.utils.TenantCommonUtils;
import org.apache.ofbiz.base.crypto.HashCrypt;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.common.login.LoginServices;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericDelegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.entity.util.EntityUtilProperties;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PasswordManagementServices {

    public static final String module = PasswordManagementServices.class.getName();
    public static final String resource = "SecurityextUiLabels";

    public static Map<String, Object> generatePasswordResetToken(DispatchContext ctx, Map<String, ? extends Object> context) {
        Delegator delegator = ctx.getDelegator();
        String userLoginId = (String) context.get("userLoginId");
        Locale locale = (Locale) context.get("locale");
        String errMsg = null;
        GenericValue userLogin = null;
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        try {
            userLogin = EntityQuery.use(delegator).from("UserLogin").where("userLoginId", userLoginId).queryOne();
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            Map<String, String> messageMap = UtilMisc.toMap("errorMessage", e.getMessage());
            errMsg = UtilProperties.getMessage(resource, "loginservices.could_not_change_password_read_failure", messageMap, locale);
            return ServiceUtil.returnError(errMsg);
        }
        if (null == userLogin) {
            Map<String, String> messageMap = UtilMisc.toMap("userLoginId", userLoginId);
            errMsg = UtilProperties.getMessage(resource, "loginservices.could_not_change_password_userlogin_with_id_not_exist", messageMap, locale);
            return ServiceUtil.returnError(errMsg);
        }
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("id", userLoginId);
        inputParams.put("subject", delegator.getDelegatorTenantId());
        try {
            String token = JWTHelper.generateJWTToken(inputParams);
            resultMap.put("token", token);
            return resultMap;
        } catch (Exception e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError(e.getMessage());
        }
    }

    public static Map<String, Object> resetPassword(DispatchContext ctx, Map<String, ? extends Object> context) {
        Delegator delegator = ctx.getDelegator();
        LocalDispatcher dispatcher = ctx.getDispatcher();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        String userLoginId = (String) context.get("userLoginId");
        String userTenantId = (String) context.get("userTenantId");
        String newPassword = (String) context.get("newPassword");
        String newPasswordVerify = (String) context.get("newPasswordVerify");
        Locale locale = (Locale) context.get("locale");
        String errMsg = null;

        GenericDelegator tenantDelegator = TenantCommonUtils.getTenantDelegator(userTenantId);

        GenericValue userLoginToUpdate = null;
        try {
            userLoginToUpdate = EntityQuery.use(tenantDelegator).from("UserLogin").where("userLoginId", userLoginId).queryOne();
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            Map<String, String> messageMap = UtilMisc.toMap("errorMessage", e.getMessage());
            errMsg = UtilProperties.getMessage(resource, "loginservices.could_not_change_password_read_failure", messageMap, locale);
            return ServiceUtil.returnError(errMsg);
        }
        if (null == userLoginToUpdate) {
            Map<String, String> messageMap = UtilMisc.toMap("userLoginId", userLoginId);
            errMsg = UtilProperties.getMessage(resource, "loginservices.could_not_change_password_userlogin_with_id_not_exist", messageMap, locale);
            return ServiceUtil.returnError(errMsg);
        }

        ArrayList<String> errorMessageList = new ArrayList<>();
        LoginServices.checkNewPassword(userLoginToUpdate, null, newPassword, newPasswordVerify, null, errorMessageList, true, locale);
        if (errorMessageList.size() > 0) {
            return ServiceUtil.returnError(errorMessageList);
        }

        if ("true".equals(EntityUtilProperties.getPropertyValue("security", "password.lowercase", delegator))) {
            newPassword = newPassword.toLowerCase();
        }

        boolean useEncryption = "true".equals(EntityUtilProperties.getPropertyValue("security", "password.encrypt", delegator));
        userLoginToUpdate.set("currentPassword", useEncryption ? HashCrypt.cryptUTF8(LoginServices.getHashType(), null, newPassword) : newPassword, false);
        userLoginToUpdate.set("requirePasswordChange", "N");
        try {
            userLoginToUpdate.store();
            LoginServices.createUserLoginPasswordHistory(delegator, userLoginId, newPassword);
        } catch (GenericEntityException e) {
            Map<String, String> messageMap = UtilMisc.toMap("errorMessage", e.getMessage());
            errMsg = UtilProperties.getMessage(resource, "loginservices.could_not_change_password_write_failure", messageMap, locale);
            return ServiceUtil.returnError(errMsg);
        }
        return resultMap;
    }
}

