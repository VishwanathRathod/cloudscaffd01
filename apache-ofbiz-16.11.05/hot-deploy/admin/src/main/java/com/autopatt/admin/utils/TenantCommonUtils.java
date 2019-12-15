package com.autopatt.admin.utils;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericDelegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;

import java.util.List;

public class TenantCommonUtils {

    public static final String module = TenantCommonUtils.class.getName();

    /**
     *
     * @param delegator
     * @param tenantId
     * @return orgPartyId
     */
    public static String getOrgPartyId(Delegator delegator, String tenantId) {
        String orgPartyId = null;
        try {
            List<GenericValue> tenantOrgParties = delegator.findByAnd("TenantOrgParty", UtilMisc.toMap("tenantId", tenantId), null, false);
            if (UtilValidate.isNotEmpty(tenantOrgParties)) {
                GenericValue tenantOrg = tenantOrgParties.get(0);
                orgPartyId = tenantOrg.getString("orgPartyId");
            }
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            e.printStackTrace();
        }
        return orgPartyId;
    }
}
