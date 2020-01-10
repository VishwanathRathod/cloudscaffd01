$(function() {
    initiateUsersMgmtModals();
    initializeUserModals();
});


function initiateUsersMgmtModals() {
    $('#deleteUserConfirmModal').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget) // Button that triggered the modal
        var deletingPartyId = button.data('party-id') // Extract info from data-* attributes
        var deletingPartyName = button.data('party-name');
        if(deletingPartyName == null) deletingPartyName = "";

        var modal = $(this)
        modal.find('#deletePartyName').text(deletingPartyName);
        $("#deleteUser_partyId").val(deletingPartyId);
    })
}
function initializeUserModals() {

    $('#suspendUserConfirmModal').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget);
        var suspendingPartyId = button.data('party-id');
        var suspendUserPartyName = button.data('party-name');
        if (suspendUserPartyName == null) suspendUserPartyName = "";

        var modal = $(this);
        modal.find('#suspendUserPartyName').text(suspendUserPartyName)
        $("#suspendUser_partyId").val(suspendingPartyId)
    });

    $('#activateUserConfirmModal').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget);
        var activateUserPartyId = button.data('party-id');
        var activateUserPartyName = button.data('party-name');
        if (activateUserPartyName == null) activateUserPartyName = "";

        var modal = $(this)
        modal.find('#activateUserPartyName').text(activateUserPartyName);
        $("#enableUser_partyId").val(activateUserPartyId)
    });
    $('#resetPasswordUserConfirmModal').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget);
        var resetPasswordForPartyId = button.data('party-id');
        var resetPasswordForPartyName = button.data('party-name');
        var resetPasswordOrgPartyId=button.data('org-party-id');
        var resetPasswordUserLoginId=button.data('user-login-id');
        if(resetPasswordForPartyName == null) resetPasswordForPartyName = "";

        var modal = $(this)
        modal.find('#resetPasswordForPartyName').text(resetPasswordForPartyName);
        modal.find('#resetPasswordOrgPartyId').val(resetPasswordOrgPartyId);
        modal.find('#resetPasswordUserLoginId').val(resetPasswordUserLoginId);
    });
}
