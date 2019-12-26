package com.autopatt.portal.services;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import java.util.Date;
import java.util.Map;

public class UserManagementServices {

    public static final String module = AutopattSubscriptionServices.class.getName();

    public static Map<String, Object> generatePasswordResetToken(DispatchContext ctx, Map<String, ? extends Object> context) {
        Delegator delegator = ctx.getDelegator();
        LocalDispatcher dispatcher = ctx.getDispatcher();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        String userLoginId = (String) context.get("emailId");
        GenericValue userLogin = null;
        try {
            userLogin = EntityQuery.use(delegator).from("UserLogin").where("userLoginId", userLoginId).queryOne();
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnFailure("Failed to fetch user login details");
        }
        if (null == userLogin) {
            return ServiceUtil.returnFailure("User not found");
        }
        try {
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);
            long expMinutes = 60;
            Date exp = new Date(nowMillis + 60000 * expMinutes);

            JwtBuilder builder = Jwts.builder();
            builder.setIssuedAt(now);
            builder.setSubject(userLoginId);
            builder.setExpiration(exp);
            resultMap.put("token", builder.compact());
            return resultMap;
        } catch (Exception e) {
            Debug.logError(e, module);
            return ServiceUtil.returnFailure("Failed to generate token");
        }
    }
}

