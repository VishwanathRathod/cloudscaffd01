$(function () {
    // initialize things..
});


$("#new_customer_form").submit(function (event) {
    event.preventDefault();

    var postData = $(this).serializeArray();
    var formURL = $(this).attr("action");
    console.log(postData);

    $("#newCustomerFormSubmitButton").attr("disabled", true);
    $('#newCustomerFormCancelButton').addClass('disabled');
    $('#newCustomerForm_Processing').removeClass("d-none");

    $.ajax(
        {
            url: formURL,
            type: "POST",
            data: postData,
            success: function (data, textStatus, jqXHR) {
                //data: return data from server
                console.log("request completed... redirecting to.. " + getUrl("customers"))
                window.location.replace(getUrl("customers") + "?createInitiated=Y");
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log("Error: " + errorThrown);
                $("#newCustomerFormSubmitButton").attr("disabled", false);
                $('#newCustomerFormCancelButton').removeClass('disabled');
                $('#newCustomerForm_Processing').addClass("d-none");
                $('#newCustomerForm_Error').removeClass("d-none");
            }
        });
    //e.unbind(); //unbind. to stop multiple form submit.
});

function listSubscriptions() {
    var orgPartyId = $('input[name="orgPartyId"]').val();
    var status = $('select[id="filterSubscriptionsByStatus"]').val();
    var productId = $('select[id="filterSubscriptionsByProduct"]').val();
    $("#customer_subscriptions").load(getUrl("filter_subscriptions?orgPartyId=" + orgPartyId + "&status=" + status + "&productId=" + productId),
    function() {
        initializeOrgSubscriptionModals();
    });
}


function loadOrgEmployees() {
    var orgPartyId = $('#orgPartyId').val();
    $("#customer_employees").load(getUrl("org_employees?orgPartyId=" + orgPartyId), function () {
        initializeOrgEmployeeModals();
    });
}

function suspendOrgEmployee() {
    var employeePartyId = $("#suspendEmployee_partyId").val()
    var orgPartyId = $('input[name="orgPartyId"]').val();
    var postData = {orgPartyId: orgPartyId, orgEmployeePartyId: employeePartyId};
    var formURL = $("#suspend_org_employee_form").attr("action");
    $.ajax(
        {
            url: formURL,
            type: "POST",
            data: postData,
            success: function (data, textStatus, jqXHR) {
                $('#suspendEmployeeConfirmModal').modal('hide');
                showSuccessToast("User Suspended Successfully");
                setTimeout(function () {
                    loadOrgEmployees();
                }, 500);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log("Error: " + errorThrown);
            }
        });
}


function activateOrgEmployee() {
    var employeePartyId = $("#enableEmployee_partyId").val()
    var orgPartyId = $('input[name="orgPartyId"]').val();
    var postData = {orgPartyId: orgPartyId, orgEmployeePartyId: employeePartyId};
    var formURL = $("#enable_org_employee_form").attr("action");
    $.ajax(
        {
            url: formURL,
            type: "POST",
            data: postData,
            success: function (data, textStatus, jqXHR) {
                $('#activateEmployeeConfirmModal').modal('hide');
                showSuccessToast("User Activated Successfully");
                setTimeout(function () {
                    loadOrgEmployees();
                }, 500);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log("Error: " + errorThrown);
            }
        });
}

function deleteOrgEmployee() {
    var employeePartyId = $("#deleteEmployee_partyId").val()
    var orgPartyId = $('input[name="orgPartyId"]').val();
    var postData = {orgPartyId: orgPartyId, orgEmployeePartyId: employeePartyId};
    var formURL = $("#delete_org_employee_form").attr("action");
    $.ajax(
        {
            url: formURL,
            type: "POST",
            data: postData,
            success: function (data, textStatus, jqXHR) {
                $('#deleteEmployeeConfirmModal').modal('hide');
                showSuccessToast("User Deleted Successfully");
                setTimeout(function () {
                    loadOrgEmployees();
                }, 500);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log("Error: " + errorThrown);
            }
        });
}

function addNewSubscription() {
    var orgPartyId = $('input[name="orgPartyId"]').val();
    var productId = $('select[id="productId"]').val();
    var validFrom = $('input[name="validFrom"]').val();
    var validTo = $('input[name="validTo"]').val();
    var postData = {"orgPartyId": orgPartyId, productId: productId, "validFrom": validFrom, "validTo": validTo};
    var formURL = getUrl("createSubscription");
    $.ajax(
        {
            url: formURL,
            type: "POST",
            data: postData,
            success: function (data, textStatus, jqXHR) {
                $('#createSubscriptionModal').modal('hide');
                showSuccessToast("Subscription added successfully");
                setTimeout(function () {
                    listSubscriptions();
                }, 500);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log("Error: " + errorThrown);
            }
        });
}

function revokeSubscription() {
    var orgPartyId = $('input[name="orgPartyId"]').val();
    var subscriptionId = $('input[id="subscriptionId"]').val();
    var revokeEffective = $('input[name="revokeNow"]:checked').val();
    var validTo = $('input[name="revokeValidTo"]').val();
    var postData = {"orgPartyId": orgPartyId, "subscriptionId":subscriptionId, "revokeEffective": revokeEffective, "validTo": validTo};
    var formURL = getUrl("revokeSubscription");
    $.ajax(
        {
            url: formURL,
            type: "POST",
            data: postData,
            success: function (data, textStatus, jqXHR) {
                $('#revokeSubscriptionModal').modal('hide');
                showSuccessToast("Subscription revoked successfully");
                setTimeout(function () {
                    listSubscriptions();
                }, 500);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log("Error: " + errorThrown);
            }
        }); 
}

function renewSubscription() {
    var orgPartyId = $('input[name="orgPartyId"]').val();
    var renewSubscriptionId = $('input[id="renewSubscriptionId"]').val();
    var renewEffective = $('input[name="renewEffective"]:checked').val();
    var validTo = $('input[name="renewTillDate"]').val();
    var postData = {"orgPartyId": orgPartyId, "subscriptionId":renewSubscriptionId, "renewEffective": renewEffective, "validTo": validTo};
    var formURL = getUrl("renewSubscription");
    $.ajax(
        {
            url: formURL,
            type: "POST",
            data: postData,
            success: function (data, textStatus, jqXHR) {
                $('#renewSubscriptionModal').modal('hide');
                showSuccessToast("Subscription renewed successfully");
                setTimeout(function () {
                    listSubscriptions();
                }, 500);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log("Error: " + errorThrown);
            }
        });
}

function saveEmployeeDetails() {
    var userPartId = $("#updateEmployee_partyId").val();
    var userOrgPartId = $("#updateEmployee_orgPartyId").val();
    var firstName = $("#updateEmployee_firstName").val();
    var lastName = $("#updateEmployee_lastName").val();
    var userEmail = $("#updateEmployee_email").val();
    var userRole = $("#updateEmployee_role").val();
    var postData = {partyId: userPartId, orgPartyId:userOrgPartId, firstname: firstName, lastname: lastName, email: userEmail, securityGroupId: userRole};
    var formURL = $("#update-employee-form").attr("action");
    $.ajax(
        {
            url: formURL,
            type: "POST",
            data: postData,
            success: function (data, textStatus, jqXHR) {
                $('#editEmployeeModal').modal('hide');
                showSuccessToast("Employee User Updated Successfully");
                setTimeout(function () {
                    loadOrgEmployees();
                }, 500);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log("Error: " + errorThrown);
            }
        });
}
function addEmployeeDetails() {
    var userOrgPartId = $("#createEmployee_orgPartyId").val();
    var firstName = $("#createEmployee_firstName").val();
    var lastName = $("#createEmployee_lastName").val();
    var empEmail = $("#createEmployee_email").val();
    var empRole = $("#createEmployee_role").val();
    var empPassword = $("#createEmployee_password").val();
    var postData = {
        orgPartyId: userOrgPartId,
        firstName: firstName,
        lastName: lastName,
        email: empEmail,
        securityGroupId: empRole,
        empPassword: empPassword
    };
    var formURL = getUrl("createEmployee");
    $.ajax(
        {
            url: formURL,
            type: "POST",
            data: postData,
            success: function (data, textStatus, jqXHR) {
                $('#createEmployeeModal').modal('hide');
                showSuccessToast("Employee has been added successfully");
                setTimeout(function () {
                    loadOrgEmployees();
                }, 500);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log("Error: " + errorThrown);
            }
        });
    console.log(postData);
}

function initResetEmployeePwd() {
    var orgPartyId = $('input[id="resetPasswordOrgPartyId"]').val();
    var userLoginId = $('input[id="resetPasswordUserLoginId"]').val();
    var postData = {"orgPartyId": orgPartyId, "userLoginId":userLoginId};
    var formURL = getUrl("initResetEmployeePwd");
    $.ajax(
        {
            url: formURL,
            type: "POST",
            data: postData,
            success: function (data, textStatus, jqXHR) {
                $('#resetPasswordEmployeeConfirmModal').modal('hide');
                showSuccessToast("Reset password initiated successfully, User will receive mail with reset link");
                setTimeout(function () {
                }, 500);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log("Error: " + errorThrown);
            }
        });
}

function filterUsersForReport() {
    var status = $('select[id="filterUsersByStatus"]').val();
    var tenantId = $('select[id="filterUsersByTenant"]').val();
    $("#users_report").load(getUrl("filterUsersForReport?status=" + status + "&tenantId=" + tenantId),
        function () {
            showSuccessToast("Users loaded successfully");
        });
}

function filterSubscriptionsForReport() {
    var status = $('select[id="filterSubscriptionsReportByStatus"]').val();
    var tenantId = $('select[id="filterSubscriptionReportByTenant"]').val();
    var planId = $('select[id="filterSubscriptionsReportByProduct"]').val();
    $("#subscriptions_report").load(getUrl("filterSubscriptionsForReport?status=" + status + "&tenantId=" + tenantId + "&planId=" + planId),
        function () {
            showSuccessToast("Subscriptions loaded successfully");
        });
}
function checkEmailEmp() {
    var email = $("#createEmployee_email").val()
    var postData = {email: email};
    var formURL = getUrl("checkEmailForEmp");
    $("#email_notExists").addClass("d-none");
    $.ajax(
        {
            url: formURL,
            type: "POST",
            data: postData,
            success: function(resp) {
                if(resp.EMAIL_EXISTS === "YES") {
                    $("#email_notExists").removeClass("d-none");
                } else {
                }
            },
            error: function (EMAIL_EXISTS) {
            }
        });
}
