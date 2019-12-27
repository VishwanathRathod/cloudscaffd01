package com.autopatt.admin.events;

import org.apache.ofbiz.webapp.control.LoginWorker;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminLoginWorker {

    public final static String module = AdminLoginWorker.class.getName();

    public static String SUCCESS = "success";
    public static String ERROR = "error";

    public static String login(HttpServletRequest request, HttpServletResponse response) {
        String res = LoginWorker.login(request, response);
        request.setAttribute("USERNAME", request.getParameter("USERNAME"));
        if (!SUCCESS.equals(res)) {
            return res;
        }
        return SUCCESS;
    }

    public static String changePassword(HttpServletRequest request, HttpServletResponse response) {
        String res = LoginWorker.login(request, response);
        if (!SUCCESS.equals(res)) {
            boolean requirePasswordChange = "Y".equals(request.getParameter("requirePasswordChange"));
            if(requirePasswordChange) {
                String username = request.getParameter("USERNAME");
                request.setAttribute("USERNAME", username);
            }
            return res;
        }
        return SUCCESS;
    }

}
