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
                // showSuccessToast("User Deleted Successfully");
                //TODO: Show toast msg
                _loadUsers();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log("Error: " + errorThrown);
            }
        });
}

function _loadUsers () {
    $("#users_list_section").load(getAppUrl("users_list_section"), function() {
        //
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