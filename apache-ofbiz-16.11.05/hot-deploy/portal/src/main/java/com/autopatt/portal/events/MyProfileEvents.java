package com.autopatt.portal.events;

import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class MyProfileEvents {
    public final static String module = MyProfileEvents.class.getName();
    public static String SUCCESS = "success";
    public static String ERROR = "error";

    public static String updateMyProfile(HttpServletRequest request, HttpServletResponse response) {
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        HttpSession session = request.getSession();
        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

        //TODO: update firstname and lastname in DB


        return SUCCESS;
    }

}
