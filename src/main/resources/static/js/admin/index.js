function notificationMessage(status, message) {
    var button;
    if (status == "success"){
        button = $('.btn-success');
        button.attr("data-message", message);
        button.click();
    } else if (status == "warning"){
        button = $('.btn-warning');
        button.attr("data-message", message);
        button.click();
    } else if (status == "danger"){
        button = $('.btn-danger');
        button.attr("data-message", message);
        button.click();
    }
}

$(document).ready(function () {
    $('#overview').click()
});
