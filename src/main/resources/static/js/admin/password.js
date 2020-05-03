function changePasswordForm(){
    var modal = $('#modal');
    modal.empty();
    $('#modal').append('<div class="modal fade" id="changePasswordModal" tabindex="-1" role="dialog"' +
        ' aria-labelledby="changePasswordLabel" aria-hidden="true">' +
        '<div class="modal-dialog" role="document">' +
        '<div class="modal-content">' +
        '<div class="modal-header">' +
        '<h3 class="modal-title" id="changePasswordLabel">Change Password</h3>' +
        '</div>' +
        '<div class="modal-body">' +
        '<form>' +
        '<div class="form-group">' +
        '<label for="oldPassword" class="col-form-label">Old Password:</label>' +
        '<input type="password" class="form-control" id="oldPassword">' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="newPassword" class="col-form-label">New Password:</label>' +
        '<input type="password" class="form-control" id="newPassword">' +
        '</div>' +
        '<div class="modal-footer">' +
        '<button type="button" class="btn btn-secondary" data-dismiss="modal" >Close</button>' +
        '<button type="submit" class="btn btn-primary" data-dismiss="modal" id="changePassword">Change</button>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>')
}

function changePassword(oldPassword, newPassword){
    $.ajax({
        type: 'post',
        url: '/user/change-password',
        dataType: 'json',
        data: {
            oldPassword: oldPassword,
            newPassword: newPassword
        },
        cache: false,
        success: function (data) {
            if (data['status'] == 504){
                notificationMessage("danger", data['message']);
            } else {
                notificationMessage("success", "Success");
            }
        },
    })
}

$('#password').click(function () {
    changePasswordForm();
    $('#changePasswordForm').click();
});

$('#conference-div').on('click', '#changePassword', function () {
    var oldPassword = $('#oldPassword').val();
    var newPassword = $('#newPassword').val();
    changePassword(oldPassword, newPassword);
});