package com.autopatt.portal.events;

import com.autopatt.admin.utils.UserLoginUtils;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilDateTime;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityUtilProperties;
import org.apache.ofbiz.security.Security;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class UserMgmtEvents {
    public final static String module = UserMgmtEvents.class.getName();
    public static final String SUCCESS = "success";
    public static final String ERROR = "error";

    public static String createUser(HttpServletRequest request, HttpServletResponse response) {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        HttpSession session = request.getSession();
        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

        String firstName = request.getParameter("firstname");
        String lastName = request.getParameter("lastname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String securityGroupId = request.getParameter("securityGroupId");

        // Check permission
        Security security = dispatcher.getSecurity();
        if (!security.hasPermission("PORTAL_ADD_USER", userLogin)) {
            request.setAttribute("_ERROR_MESSAGE_", "You do not have permission to add a new user. ");
            return ERROR;
        }

        // check tenant has valid subscription to add new user
        /*try {
            Map<String, Object> resp = dispatcher.runSync("hasValidSubscriptionToAddUser",
                    UtilMisc.<String, Object>toMap("userLogin", userLogin));
            if (!ServiceUtil.isSuccess(resp)) {
                String errorMessage = (String) resp.get("errorMessage");
                Debug.logError(errorMessage, module);
                request.setAttribute("_ERROR_MESSAGE_", errorMessage);
                return ERROR;
            }
        } catch (GenericServiceException e) {
            Debug.logError(e, module);
            request.setAttribute("_ERROR_MESSAGE_", "Failed to fetch subscription");
            return ERROR;
        }*/


        // TODO: Validations - check for duplicate email

        try {
            // Create Party & Person
            Map<String, Object> createPersonResp = dispatcher.runSync("createPerson", UtilMisc.<String, Object>toMap("firstName", firstName,
                    "lastName", lastName,
                    "userLogin", userLogin));
            if (!ServiceUtil.isSuccess(createPersonResp)) {
                Debug.logError("Error creating new user for " + email, module);
                request.setAttribute("_ERROR_MESSAGE_", "Unable to add new user. ");
                return ERROR;
            }
            String partyId = (String) createPersonResp.get("partyId");

            // Create UserLogin
            Map<String, Object> userLoginCtx = UtilMisc.toMap("userLogin", userLogin);
            userLoginCtx.put("userLoginId", email);
            userLoginCtx.put("currentPassword", password);
            userLoginCtx.put("currentPasswordVerify", password);
            userLoginCtx.put("requirePasswordChange", "Y"); // enforce password change for new user
            userLoginCtx.put("partyId", partyId);

            Map<String, Object> createUserLoginResp = dispatcher.runSync("createUserLogin", userLoginCtx);
            if (!ServiceUtil.isSuccess(createUserLoginResp)) {
                Debug.logError("Error creating userLogin for " + email, module);
                request.setAttribute("_ERROR_MESSAGE_", "Unable to add new user. ");
                return ERROR;
            }

            // All Org Users should have EMPLOYEE role
            Map<String, Object> partyRole = UtilMisc.toMap(
                    "partyId", partyId,
                    "roleTypeId", "EMPLOYEE",
                    "userLogin", userLogin
            );
            Map<String, Object> createPartyRoleResp = dispatcher.runSync("createPartyRole", partyRole);
            if (!ServiceUtil.isSuccess(createPartyRoleResp)) {
                Debug.logError("Error creating party role for " + email, module);
                request.setAttribute("_ERROR_MESSAGE_", "Unable to add new user. ");
                return ERROR;
            }

            // Add partyRelationship with ORG Party (once Tenant is ready)
            String tenantOrganizationPartyId = EntityUtilProperties.getPropertyValue("general", "ORGANIZATION_PARTY",null, delegator);
            Map<String, Object> partyRelationship = UtilMisc.toMap(
                    "partyIdFrom", tenantOrganizationPartyId,
                    "partyIdTo", partyId,
                    "roleTypeIdFrom", "ORGANIZATION_ROLE",
                    "roleTypeIdTo", "EMPLOYEE",
                    "partyRelationshipTypeId", "EMPLOYMENT",
                    "userLogin", UserLoginUtils.getSystemUserLogin(delegator)
            );
            Map<String,Object> createPartyRelationResp = dispatcher.runSync("createPartyRelationship", partyRelationship);
            if(!ServiceUtil.isSuccess(createPartyRelationResp)) {
                Debug.logError("Error creating new Party Relationship between " + tenantOrganizationPartyId + " and "
                        + partyId+" in tenant " + delegator.getDelegatorTenantId(), module);
            }

            // Assign SecurityGroup to user
            GenericValue userLoginSecurityGroup = delegator.makeValue("UserLoginSecurityGroup",
                    UtilMisc.toMap("userLoginId", email,
                            "groupId", securityGroupId,
                            "fromDate", UtilDateTime.nowTimestamp()));
            try {
                userLoginSecurityGroup.create();
            } catch (GenericEntityException e) {
                request.setAttribute("_ERROR_MESSAGE_", "Unable to assign role to the user. ");
                return ERROR;
            }
        } catch (GenericServiceException e) {
            e.printStackTrace();
            return ERROR;
        }
        request.setAttribute("createSuccess", "Y");
        return SUCCESS;
    }

    public static String updateUser(HttpServletRequest request, HttpServletResponse response) {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        HttpSession session = request.getSession();
        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

        // get firstname and lastname from frontend
        String firstname = request.getParameter("firstname");
        System.out.println("first name is " + firstname);
        String lastname = request.getParameter("lastname");
        System.out.println("last name is " + lastname);
        // get person object from db
        Map<String, Object> inputs = UtilMisc.toMap("partyId", request.getParameter("partyId"));
        try {
            GenericValue person = delegator.findOne("Person", inputs, false);
            // set new values for firstname, lastname
            person.set("firstName", firstname);
            person.set("lastName", lastname);

            // store the person object back to db
            delegator.store(person);
        } catch (GenericEntityException e) {
            e.printStackTrace();
            // return error message to front-end
            request.setAttribute("_ERROR_MESSAGE_", "Unable to update the profile details.");
            return ERROR;
        }

        // return success messsage to front-end
        request.setAttribute("updateSuccess", "Y");
        return SUCCESS;
    }

    public static String deleteUser(HttpServletRequest request, HttpServletResponse response) {
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        HttpSession session = request.getSession();
        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

        String partyId = request.getParameter("userPartyId");

        // TODO: Check permission
        try {
            Map<String,Object> removeOrgEmpResp = dispatcher.runSync("removeOrgEmployee",
                    UtilMisc.toMap("userLogin", userLogin,
                            "orgEmployeePartyId", partyId));
            if(!ServiceUtil.isSuccess(removeOrgEmpResp)) {
                request.setAttribute("_ERROR_MESSAGE_", "Error trying to delete user with party id "+ partyId);
                return ERROR;
            }
        } catch (GenericServiceException e) {
            e.printStackTrace();
            request.setAttribute("_ERROR_MESSAGE_", "Error trying to delete user with party id "+ partyId);
            return ERROR;
        }
        request.setAttribute("_EVENT_MESSAGE_", "User deleted successfully.");
        return SUCCESS;
    }

}
