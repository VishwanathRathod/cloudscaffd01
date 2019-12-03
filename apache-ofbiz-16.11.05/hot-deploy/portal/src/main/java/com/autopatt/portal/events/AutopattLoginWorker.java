package com.autopatt.portal.events;

import org.apache.ofbiz.base.util.Debug;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AutopattLoginWorker {

    public final static String module = AutopattLoginWorker.class.getName();

    public static String SUCCESS="success";
    public static String ERROR="error";

    public static String extensionCheckLogin(HttpServletRequest request, HttpServletResponse response) {

        String res = org.apache.ofbiz.webapp.control.LoginWorker.extensionCheckLogin(request,response);
        if(!SUCCESS.equals(res)){
            return res;
        }
        HttpSession session = request.getSession();
        String ssid = session.getId();
        //check if this session and db session id of user are same, if not then force logout
        String logout="N";
        if("Y".equals(logout)) {
            try {
                org.apache.ofbiz.webapp.control.LoginWorker.logout(request,response);
            } catch (Exception e) {
                Debug.logError(e, "Exception during one device login: " + e.getMessage(), module);
            }
            return ERROR;
        }
        return  SUCCESS;
    }

    public static String login(HttpServletRequest request, HttpServletResponse response) {
        String res = org.apache.ofbiz.webapp.control.LoginWorker.login(request,response);
        HttpSession session = request.getSession();
        String ssid = session.getId();
        //add this id new table
        return res;
    }

}
