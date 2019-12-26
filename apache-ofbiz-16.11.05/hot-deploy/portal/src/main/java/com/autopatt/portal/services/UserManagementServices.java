package com.autopatt.portal.services;

import com.autopatt.admin.utils.TenantCommonUtils;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.ofbiz.base.crypto.HashCrypt;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
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

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserManagementServices {

    public static final String module = UserManagementServices.class.getName();
    public static final String resource = "SecurityextUiLabels";

    public static Map<String, Object> generatePasswordResetToken(DispatchContext ctx, Map<String, ? extends Object> context) {
        Delegator delegator = ctx.getDelegator();
        LocalDispatcher dispatcher = ctx.getDispatcher();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        String userLoginId = (String) context.get("emailId");
        Locale locale = (Locale) context.get("locale");
        String errMsg = null;
        GenericValue userLogin = null;
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
        try {
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);
            long expMinutes = 60;
            Date exp = new Date(nowMillis + 60000 * expMinutes);

            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
            byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("AUTOPATT");
            Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

            JwtBuilder builder = Jwts.builder();
            builder.setId(userLoginId);
            builder.setIssuedAt(now);
            builder.setSubject(delegator.getDelegatorTenantId());
            builder.setExpiration(exp);
            builder.signWith(signatureAlgorithm, signingKey);
            resultMap.put("token", builder.compact());
            return resultMap;
        } catch (Exception e) {
            Debug.logError(e, module);
            return ServiceUtil.returnFailure("Failed to generate token");
        }
    }

    public static Map<String, Object> resetPassword(DispatchContext ctx, Map<String, ? extends Object> context) {
        Delegator delegator = ctx.getDelegator();
        LocalDispatcher dispatcher = ctx.getDispatcher();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        String userLoginId = (String) context.get("emailId");
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
        LoginServices.checkNewPassword(userLoginToUpdate,null,newPassword,newPasswordVerify,null,errorMessageList,true,locale);
        if (errorMessageList.size() > 0) {
            return ServiceUtil.returnError(errorMessageList);
        }

        if ("true".equals(EntityUtilProperties.getPropertyValue("security", "password.lowercase", delegator))) {
            newPassword = newPassword.toLowerCase();
            newPasswordVerify = newPasswordVerify.toLowerCase();
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

