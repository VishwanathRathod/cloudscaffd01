package com.autopatt.admin.events;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import com.autopatt.admin.utils.*;

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

}