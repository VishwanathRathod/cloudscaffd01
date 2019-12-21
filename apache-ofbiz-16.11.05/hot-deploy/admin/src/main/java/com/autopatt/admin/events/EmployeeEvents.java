package com.autopatt.admin.events;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import com.autopatt.admin.utils.*;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;
import org.owasp.esapi.User;

public class EmployeeEvents {
    public final static String module = EmployeeEvents.class.getName();
    public static String SUCCESS = "success";
    public static String ERROR = "error";

    public static String updateEmployee(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
        String orgPartyId = request.getParameter("orgPartyId");
        request.setAttribute("orgPartyId", orgPartyId);

        Delegator tenantDelegator = TenantCommonUtils.getTenantDelegatorByOrgPartyId(orgPartyId);

        String firstname=request.getParameter("firstname");
        String lastname=request.getParameter("lastname");
        Map<String, Object> inputs = UtilMisc.toMap("partyId", request.getParameter("partyId")); // party id should come from request
        try {
            GenericValue person = tenantDelegator.findOne("Person", inputs , false);
            person.set("firstName",firstname);
            person.set("lastName",lastname);
            tenantDelegator.store(person);
        } catch (GenericEntityException e) {
            e.printStackTrace();
            request.setAttribute("_ERROR_MESSAGE_", "Unable to update the profile details.");
            return ERROR;
        }
        request.setAttribute("_EVENT_MESSAGE_", "Profile details updated successfully.");
        return SUCCESS;
    }

    /**
     * Suspend an Org employee user
     * @param request
     * @param response
     * @return
     */
    public static String suspendEmployee(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
        String orgPartyId = request.getParameter("orgPartyId");
        request.setAttribute("orgPartyId", orgPartyId);

        String orgEmployeePartyId = request.getParameter("orgEmployeePartyId");
        request.setAttribute("orgEmployeePartyId", orgEmployeePartyId);

        //TODO: Add user permission check

        Delegator tenantDelegator = TenantCommonUtils.getTenantDelegatorByOrgPartyId(orgPartyId);
        LocalDispatcher tenantDispatcher = TenantCommonUtils.getTenantDispatcherByOrgPartyId(orgPartyId);
        String userLoginId = UserLoginUtils.getUserLoginIdForPartyId(tenantDelegator, orgEmployeePartyId);
        try {
            if (UtilValidate.isEmpty(userLoginId)) {
                request.setAttribute("_ERROR_MESSAGE_", "Employee user with id "+ orgEmployeePartyId+" not found.");
                return ERROR;
            }
            Map<String,Object> updateUserLoginResp = tenantDispatcher.runSync("updateUserLoginSecurity",
                    UtilMisc.toMap("userLogin", UserLoginUtils.getSystemUserLogin(tenantDelegator), "userLoginId", userLoginId, "enabled", "N"));

            if(!ServiceUtil.isSuccess(updateUserLoginResp)) {
                request.setAttribute("_ERROR_MESSAGE_", "Error trying to suspend user with id "+ orgEmployeePartyId);
                return ERROR;
            }
        } catch (GenericServiceException e) {
            e.printStackTrace();
            request.setAttribute("_ERROR_MESSAGE_", "Error trying to suspend user with id "+ orgEmployeePartyId);
            return ERROR;
        }
        request.setAttribute("_EVENT_MESSAGE_", "User suspending successfully.");
        return SUCCESS;
    }


    /**
     * Enable an inactive/suspended user login id
     * @param request
     * @param response
     * @return
     */
    public static String activateEmployee(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
        String orgPartyId = request.getParameter("orgPartyId");
        request.setAttribute("orgPartyId", orgPartyId);

        String orgEmployeePartyId = request.getParameter("orgEmployeePartyId");
        request.setAttribute("orgEmployeePartyId", orgEmployeePartyId);

        //TODO: Add user permission check

        Delegator tenantDelegator = TenantCommonUtils.getTenantDelegatorByOrgPartyId(orgPartyId);
        LocalDispatcher tenantDispatcher = TenantCommonUtils.getTenantDispatcherByOrgPartyId(orgPartyId);
        String userLoginId = UserLoginUtils.getUserLoginIdForPartyId(tenantDelegator, orgEmployeePartyId);
        try {
            if (UtilValidate.isEmpty(userLoginId)) {
                request.setAttribute("_ERROR_MESSAGE_", "Employee user with id "+ orgEmployeePartyId+" not found.");
                return ERROR;
            }
            Map<String,Object> updateUserLoginResp = tenantDispatcher.runSync("updateUserLoginSecurity",
                    UtilMisc.toMap("userLogin", UserLoginUtils.getSystemUserLogin(tenantDelegator),
                            "userLoginId", userLoginId,
                            "enabled", "Y",
                            "disabledDateTime", null));

            if(!ServiceUtil.isSuccess(updateUserLoginResp)) {
                request.setAttribute("_ERROR_MESSAGE_", "Error trying to enable user with id "+ orgEmployeePartyId);
                return ERROR;
            }
        } catch (GenericServiceException e) {
            e.printStackTrace();
            request.setAttribute("_ERROR_MESSAGE_", "Error trying to enable user with id "+ orgEmployeePartyId);
            return ERROR;
        }
        request.setAttribute("_EVENT_MESSAGE_", "User enabled successfully.");
        return SUCCESS;
    }

}