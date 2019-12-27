var admins = {
    removeUser: _removeUser
};


function _removeUser() {
    var userPartyId = $("#deleteAdminUser_partyId").val();
    console.log("deleting " + userPartyId)
    var postData = {adminPartyId: userPartyId};
    var formURL = $("#delete_admin_user_form").attr("action");
    $.ajax(
        {
            url: formURL,
            type: "POST",
            data: postData,
            success: function (data, textStatus, jqXHR) {
                $('#deleteAdminUserConfirmModal').modal('hide');
                showSuccessToast("Admin User Deleted Successfully");
                location.reload();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log("Error: " + errorThrown);
            }
        });
}
