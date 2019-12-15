

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
    })

    $('#suspendEmployeeConfirmModal').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget) // Button that triggered the modal
        var deletingPartyId = button.data('party-id') // Extract info from data-* attributes
        var deletingPartyName = button.data('party-name');
        if(deletingPartyName == null) deletingPartyName = "";

        var modal = $(this)
        modal.find('#suspendPartyName').text(deletingPartyName)
    })


}