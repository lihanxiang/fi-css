function inputEmailForm() {
    var modal = $('#modal');
    modal.empty();
    $('#modal').append('<div class="modal fade" id="inputEmailModal" tabindex="-1" role="dialog"' +
        ' aria-labelledby="inputEmailLabel" aria-hidden="true">' +
        '<div class="modal-dialog" role="document">' +
        '<div class="modal-content">' +
        '<div class="modal-header">' +
        '<h3 class="modal-title" id="inputEmailLabel">Input your login email address</h3>' +
        '</div>' +
        '<div class="modal-body">' +
        '<form>' +
        '<div class="form-group">' +
        '<label for="loginEmail" class="col-form-label">Email address:</label>' +
        '<input type="email" class="form-control" id="loginEmail">' +
        '</div>' +
        '<div class="modal-footer">' +
        '<button type="button" class="btn btn-secondary" data-dismiss="modal" >Close</button>' +
        '<button type="submit" class="btn btn-primary" data-dismiss="modal" id="sendRandomPassword">Submit</button>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>')
}

function sendRandomPassword(loginEmail) {

    $.ajax({
        type: 'post',
        url: '/user/send-password',
        dataType: 'json',
        data: {
            loginEmail: loginEmail
        },
        cache: false,
        success: function (data) {
            if (data['status'] == 409){
                $('#email-not-registered').click();
            } else {
                $('#check-inbox-for-password').click();
            }
        }
    })
}

$('#forgetPassword').click(function () {
    inputEmailForm();
    $('#inputEmailForm').click();
});

$('.index').on('click', '#sendRandomPassword', function () {
    var loginEmail = $('#loginEmail').val();
    sendRandomPassword(loginEmail);
});