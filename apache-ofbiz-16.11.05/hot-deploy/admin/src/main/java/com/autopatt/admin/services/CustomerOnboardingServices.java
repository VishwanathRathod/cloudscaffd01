/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.autopatt.admin.services;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilDateTime;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ModelService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.*;

/**
 * Services for Customer Onboarding
 * Tenant Creation
 * Org Setup
 */
public class CustomerOnboardingServices {

    public static final String module = CustomerOnboardingServices.class.getName();
    private static Properties ONBOARDING_PROPERTIES = UtilProperties.getProperties("onboarding.properties");

    /**
     * Onboard a new Customer.
     * Creates a new tenant DB.
     * Creates Org Party record
     * Creates First User (admin) for tenant
     * @param ctx The DispatchContext that this service is operating in.
     * @param context Map containing the input parameters.
     * @return Map with the result of the service, the output parameters.
     */
    public static Map<String, Object> onboardNewCustomer(DispatchContext ctx, Map<String, ? extends Object> context) {
        Map<String, Object> result = new HashMap<String, Object>();
        Delegator delegator = ctx.getDelegator();
        Locale locale = (Locale) context.get("locale");
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String tenantId = (String) context.get("tenantId");
        String organizationName = (String) context.get("organizationName");

        String contactFirstName = (String) context.get("contactFirstName");
        String contactLastName = (String) context.get("contactLastName");
        String contactEmail = (String) context.get("contactEmail");
        String contactPassword = (String) context.get("contactPassword");
        String sendNotificationToContact = (String) context.get("sendNotificationToContact");

        if(UtilValidate.isEmpty(tenantId)) {
            // Generate tenantId from organizationName
        }

        // TODO: 1. Create OrgEntry in Main DB (along with status)
        System.out.println("Creating OrgEntry................");

        // 2. Initiate Tenant DB Creation
        String dbHostIp = ONBOARDING_PROPERTIES.getProperty("onboarding.database.mysql.hostname", "127.0.0.1");
        String dbHostPort = ONBOARDING_PROPERTIES.getProperty("onboarding.database.mysql.port", "3306");
        String dbUsername = ONBOARDING_PROPERTIES.getProperty("onboarding.database.mysql.username");
        String dbPassword = ONBOARDING_PROPERTIES.getProperty("onboarding.database.mysql.password");

        String tenantDbUsername = "user" + tenantId;
        String tenantDbPassword = "p" + RandomStringUtils.random(15, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwyz1234567890@".toCharArray());

        createTenantDatabase(dbHostIp, dbHostPort, dbUsername, dbPassword, tenantId, tenantDbUsername, tenantDbPassword);

        // Create Tenant entries
        System.out.println("Creating Tenant entries ");

        // TODO: 3. Create Org Entry in TenantDB,
        System.out.println("Creating Org Entry in Tenant DB");

        // TODO: 4. create user login for given contact in TennatDB, and build relationship

        // TODO: 5. Send EMail notification if marked YES

        result.put(ModelService.RESPONSE_MESSAGE, ModelService.RESPOND_SUCCESS);
        result.put("tenantId", tenantId);
        return result;
    }


    private static void createTenantDatabase(String dbHostname, String dbHostPort, String dbUsername, String dbPassword,
                                      String tenantId, String tenantDbUsername, String tenantDbPassword) {
        String cloneScriptFile = getCloneScriptFilePath();
        Debug.logInfo("Cloning Database from template for new tenant: " + tenantId, module);
        Debug.logInfo("Running Script: " + cloneScriptFile, module);
        try {
            Debug.logInfo(cloneScriptFile + " " + dbHostname + " " + dbHostPort + " " + dbHostname + " "
                    + dbUsername + " " + dbPassword
                    + " " + tenantId + " " + tenantDbUsername + " " + tenantDbPassword, module);

            ProcessBuilder pb = null;
            if (SystemUtils.IS_OS_WINDOWS) {
                pb = new ProcessBuilder(cloneScriptFile, dbHostname, dbUsername, dbPassword, dbHostPort,
                        tenantId, tenantDbUsername, tenantDbPassword);
            } else {
                pb = new ProcessBuilder("sh", cloneScriptFile, dbHostname, dbUsername, dbPassword, dbHostPort,
                        tenantId, tenantDbUsername, tenantDbPassword);
            }
            Process p = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String s;
            while ((s = reader.readLine()) != null) {
                Debug.logInfo(">>>" + s, module);
            }
            p.waitFor();
            Debug.logInfo("Tenant Database creation completed for tenant: " + tenantId, module);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Return the script filename based on OS
    private static String getCloneScriptFilePath() {
        if (SystemUtils.IS_OS_WINDOWS) {
            return "tools/scripts/windows/clone-tenant-db.cmd";
        } else {
            return "tools/scripts/linux/clone-tenant-db.sh";
        }
    }




}
