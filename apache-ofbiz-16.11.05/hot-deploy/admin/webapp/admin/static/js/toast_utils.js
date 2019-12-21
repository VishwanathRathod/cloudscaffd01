$(function () {
    // initialize things..
});

function showSuccessToast(msg) {
    if(!msg) msg = "Transaction completed successfully."
    $("#success_toaster_message").text(msg)
    $('#success_toaster').toast('show');
}

function showErrorToast(msg) {
    //TODO:
}