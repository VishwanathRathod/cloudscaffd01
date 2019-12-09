package com.autopatt.admin.utils;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;

public class UserLoginUtils {

    public static final String module = UserLoginUtils.class.getName();


    public static GenericValue getSystemUserLogin(Delegator delegator) {
        GenericValue userLogin = null;
        try {
            userLogin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", "system"), true);
        } catch (GenericEntityException e) {
            Debug.logError(e, "Error finding party in getPartyByPartyId", module);
        }
        return userLogin;
    }

}
