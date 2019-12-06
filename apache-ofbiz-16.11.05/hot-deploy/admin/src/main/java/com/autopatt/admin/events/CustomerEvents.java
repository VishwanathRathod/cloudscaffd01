package com.autopatt.admin.events;

import org.apache.ofbiz.base.util.Debug;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomerEvents {
    public final static String module = CustomerEvents.class.getName();

    public static String createCustomer(HttpServletRequest request, HttpServletResponse response) {

        Debug.log("Initiating the process to onboard new customer", module);

        // TODO: 1. clone DB and create tenant DB

        // TODO: 2. Create Org Party in Main DB, and add Roles (CUSTOMER)

        // TODO: 3. Create Org Party in Tenant DB

        // TODO: 4. Create first admin user (party, userlogin...) in Tenant DB, and add Roles (ADMIN)


        return "success";
    }
}
