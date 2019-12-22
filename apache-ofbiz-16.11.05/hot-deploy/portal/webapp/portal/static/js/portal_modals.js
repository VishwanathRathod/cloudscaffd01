$(function() {
    initiateUsersMgmtModals();
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