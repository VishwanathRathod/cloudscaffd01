

$(function() {
    initializeDeleteUserModal();
});


function initializeDeleteUserModal() {
    $('#editEmployeeModal').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget) // Button that triggered the modal
        var editPartyId = button.data('party-id') // Extract info from data-* attributes
        var editPartyName = button.data('party-name');
        if(editPartyName == null) editPartyName = "";

        var modal = $(this)
        modal.find('#editEmployee_name').text(editPartyName)
    });

    $('#suspendEmployeeConfirmModal').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget);
        var deletingPartyId = button.data('party-id');
        var deletingPartyName = button.data('party-name');
        if(deletingPartyName == null) deletingPartyName = "";

        var modal = $(this)
        modal.find('#suspendPartyName').text(deletingPartyName)
    });

    $('#activateEmployeeConfirmModal').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget);
        var activatePartyId = button.data('party-id');
        var activatePartyName = button.data('party-name');
        if(activatePartyName == null) activatePartyName = "";

        var modal = $(this)
        modal.find('#activatePartyName').text(activatePartyName)
    });
    $('#resetPasswordEmployeeConfirmModal').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget);
        var resetPasswordForPartyId = button.data('party-id');
        var resetPasswordForPartyName = button.data('party-name');
        if(resetPasswordForPartyName == null) resetPasswordForPartyName = "";

        var modal = $(this)
        modal.find('#resetPasswordForPartyName').text(resetPasswordForPartyName)
    });
}