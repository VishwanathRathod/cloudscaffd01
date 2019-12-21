package com.autopatt.admin.utils;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericDelegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.DelegatorFactory;
import org.apache.ofbiz.service.GenericDispatcherFactory;
import org.apache.ofbiz.service.LocalDispatcher;

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

    public static String getTenantIdForOrgPartyId(Delegator delegator, String orgPartyId) {
        String tenantId = null;
        try {
            List<GenericValue> tenantOrgParties = delegator.findByAnd("TenantOrgParty", UtilMisc.toMap("orgPartyId", orgPartyId), null, false);
            if (UtilValidate.isNotEmpty(tenantOrgParties)) {
                GenericValue tenantOrg = tenantOrgParties.get(0);
                tenantId = tenantOrg.getString("tenantId");
            }
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            e.printStackTrace();
        }
        return tenantId;
    }

    public static GenericDelegator getTenantDelegator(String tenantId) {
        GenericDelegator tenantDelegator = (GenericDelegator) DelegatorFactory.getDelegator("default#" + tenantId);
        return tenantDelegator;
    }

    public static Delegator getTenantDelegatorByOrgPartyId(String orgPartyId) {
        GenericDelegator mainDelegator = (GenericDelegator) DelegatorFactory.getDelegator("default");
        String tenantId = getTenantIdForOrgPartyId(mainDelegator, orgPartyId);
        return getTenantDelegator(tenantId);
    }

    public static LocalDispatcher getTenantDispatcherByOrgPartyId(String orgPartyId) {
        GenericDelegator mainDelegator = (GenericDelegator) DelegatorFactory.getDelegator("default");
        String tenantId = getTenantIdForOrgPartyId(mainDelegator, orgPartyId);
        GenericDelegator tenantDelegator = getTenantDelegator(tenantId);
        LocalDispatcher tenantDispatcher = new GenericDispatcherFactory().createLocalDispatcher("default#" + tenantId, tenantDelegator);
        return tenantDispatcher;
    }
}
