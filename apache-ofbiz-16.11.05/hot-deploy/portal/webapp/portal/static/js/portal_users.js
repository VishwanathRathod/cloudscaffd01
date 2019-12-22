var users = {
    removeUser: _removeUser,
    loadUsers: _loadUsers
};


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