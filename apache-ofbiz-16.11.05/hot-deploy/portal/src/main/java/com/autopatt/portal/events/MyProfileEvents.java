package com.autopatt.portal.events;

import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class MyProfileEvents {
    public final static String module = MyProfileEvents.class.getName();
    public static String SUCCESS = "success";
    public static String ERROR = "error";

    public static String updateMyProfile(HttpServletRequest request, HttpServletResponse response) {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        HttpSession session = request.getSession();
        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

        // get firstname and lastname from frontend
        String firstname=request.getParameter("firstname");
        System.out.println("first name is "+firstname);
        String lastname=request.getParameter( "lastname");
        System.out.println("last name is "+lastname);
        // get person object from db
        Map<String, Object> inputs = UtilMisc.toMap("partyId", userLogin.get("partyId"));
        try {
            GenericValue person = delegator.findOne("Person", inputs , false);
            // set new values for firstname, lastname
            person.set("firstName",firstname);
            person.set("lastName",lastname);

           // store the person object back to db
            delegator.store(person);
        } catch (GenericEntityException e) {
            e.printStackTrace();
            // return error message to front-end
            request.setAttribute("_ERROR_MESSAGE_", "Unable to update the profile details.");
            return ERROR;
        }

        // return success messsage to front-end
        request.setAttribute("_EVENT_MESSAGE_", "Profile details updated successfully.");
        return SUCCESS;
    }

}
