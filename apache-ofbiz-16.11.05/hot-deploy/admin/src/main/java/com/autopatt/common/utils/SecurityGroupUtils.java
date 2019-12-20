package com.autopatt.common.utils;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericDelegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;

import java.util.*;

public class SecurityGroupUtils {

    public static final String module = SecurityGroupUtils.class.getName();

    /**
     * Get list of available Security Groups
     * Only Get roles prefixed with AP_
     * @param delegator
     * @return availableSecurityGroups
     */
    public static List<GenericValue> getAvailableSecurityGroups(Delegator delegator) {
        String orgPartyId = null;
        List<GenericValue> availableSecurityGroups = new ArrayList<>();
        try {
            List<GenericValue> securityGroups = delegator.findByAnd("SecurityGroup", null, null, true);
            if (UtilValidate.isNotEmpty(securityGroups)) {
                for(GenericValue group: securityGroups) {
                    if(group.getString("groupId").startsWith("AP_")) {
                        availableSecurityGroups.add(group);
                    }
                }
            }
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            e.printStackTrace();
        }
        return availableSecurityGroups;
    }
}
