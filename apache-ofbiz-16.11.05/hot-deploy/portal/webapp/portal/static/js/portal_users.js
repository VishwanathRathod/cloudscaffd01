var users = {
    removeUser: _removeUser,
    loadUsers: _loadUsers,
};

$(function () {
    // initialize things..

});
function _removeUser() {
    var userPartyId = $("#deleteUser_partyId").val()
    var postData = {userPartyId: userPartyId};
    var formURL = $("#delete_user_form").attr("action");
    $.ajax(
        {
            url: formURL,
            type: "POST",
            data: postData,
            success: function (data, textStatus, jqXHR) {
                $('#deleteUserConfirmModal').modal('hide');
                showSuccessToast("User Deleted Successfully");
                _loadUsers();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log("Error: " + errorThrown);
            }
        });
}

function _loadUsers () {
    $("#users_list_section").load(getAppUrl("users_list_section"), function() {
        initiateUsersMgmtModals();
    });
}

function activateUser() {
    var employeePartyId = $("#enableUser_partyId").val()
    var postData = {partyId: employeePartyId};
    var formURL = $("#enable_user_form").attr("action");
    $.ajax(
        {
            url: formURL,
            type: "POST",
            data: postData,
            success: function (resp) {
                if(resp.Success === "Y") {
                    $('#activateUserConfirmModal').modal('hide');
                    showSuccessToast("User Activated Successfully");
                    setTimeout(function () {
                        _loadUsers();
                    }, 500);
                } else {
                    //showErrorToast("user cannot be activated");
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log("Error: " + errorThrown);
            }
        });
}
function suspendUser() {
    var employeePartyId = $("#suspendUser_partyId").val()
    var postData = {partyId: employeePartyId};
    var formURL = $("#suspend_user_form").attr("action");
    $.ajax(
        {
            url: formURL,
            type: "POST",
            data: postData,
            success: function (resp) {
                if(resp.Success === "Y"){
                $('#suspendUserConfirmModal').modal('hide');
                showSuccessToast("User Suspended Successfully");
                setTimeout(function () {
                    _loadUsers();
                }, 500);
                } else {
                    //showErrorToast("user cannot be suspended");
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log("Error: " + errorThrown);
            }
        });
}
function initResetUserPwd() {
    var PartyId = $('input[id="resetPasswordForPartyId"]').val();
    var userLoginId = $('input[id="resetPasswordUserLoginId"]').val();
    var postData = {"partyId": PartyId, "userLoginId":userLoginId};
    var formURL = getAppUrl("initResetUserPwd");
    $.ajax(
        {
            url: formURL,
            type: "POST",
            data: postData,
            success: function (resp) {
                if(resp.Success === "Y") {
                    $('#resetPasswordUserConfirmModal').modal('hide');
                    showSuccessToast("Reset password initiated successfully, User will receive an e-mail with reset link");
                    setTimeout(function () {
                    }, 500);
                } else {
                    //showErrorToast("Not able to update password");
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log("Error: " + errorThrown);
            }
        });
}

function checkIfEmailExists() {
    console.log("checkemail invoked...")
    var email = $("#userEmail").val()
    var postData = {email: email};
    var formURL = getAppUrl("checkEmailAlreadyExists");
    console.log("invoking.. " + formURL)

    $("#email_notavailable").addClass("d-none");
    $.ajax(
        {
            url: formURL,
            type: "POST",
            data: postData,
            dataType : "html",
            success: function(resp) {
                var respObj = JSON.parse(resp);
                if(respObj.EMAIL_EXISTS === "YES") {
                    $("#email_notavailable").removeClass("d-none");
                } else {
                    //$("#emailInfo").html("FALSE");
                }
            },
            error: function (EMAIL_EXISTS) {
            }
        });
}