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

import org.apache.ofbiz.base.util.UtilDateTime;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ModelService;

import java.sql.Timestamp;
import java.util.*;

/**
 * Services for Customer Onboarding
 * Tenant Creation
 * Org Setup
 */
public class CustomerOnboardingServices {

    public static final String module = CustomerOnboardingServices.class.getName();

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
        Timestamp now = UtilDateTime.nowTimestamp();
        List<GenericValue> toBeStored = new LinkedList<GenericValue>();
        Locale locale = (Locale) context.get("locale");
        // in most cases userLogin will be null, but get anyway so we can keep track of that info if it is available
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

        // TODO: 2. Initiate Tenant DB Creation

        // TODO: 3. Create Org Entry in TenantDB,

        // TODO: 4. create user login for given contact in TennatDB, and build relationship

        // TODO: 5. Send EMail notification if marked YES

        result.put(ModelService.RESPONSE_MESSAGE, ModelService.RESPOND_SUCCESS);
        result.put("tenantId", tenantId);
        return result;
    }



}
