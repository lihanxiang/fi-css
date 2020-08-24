function signUpForm1() {
    var modal = $('#modal');
    modal.empty();
    $('#modal').append('<div class="modal fade" id="sendAuthCodeModal" tabindex="-1" role="dialog"' +
        ' aria-labelledby="sendAuthCodeLabel" aria-hidden="true">' +
        '<div class="modal-dialog" role="document">' +
        '<div class="modal-content">' +
        '<div class="modal-header">' +
        '<h3 class="modal-title" id="sendAuthCodeLabel">Create a new account</h3>' +
        '</div>' +
        '<div class="modal-body">' +
        '<form>' +
        '<div class="form-group">' +
        '<label for="loginEmail" class="col-form-label">Email address:</label>' +
        '<input type="email" class="form-control" id="loginEmail" required>' +
        '</div>' +
        '<div class="modal-footer">' +
        '<button type="button" class="btn btn-secondary" data-dismiss="modal" >Close</button>' +
        '<button type="submit" class="btn btn-primary" data-dismiss="modal" id="sendAuthCode">Next</button>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>')
}

function signUpForm2() {
    var modal = $('#modal');
    modal.empty();
    $('#modal').append('<div class="modal fade" id="signUpModal" tabindex="-1" role="dialog"' +
        ' aria-labelledby="signUpLabel" aria-hidden="true">' +
        '<div class="modal-dialog" role="document">' +
        '<div class="modal-content">' +
        '<div class="modal-header">' +
        '<h3 class="modal-title" id="signUpLabel">Create a new account</h3>' +
        '</div>' +
        '<div class="modal-body">' +
        '<form>' +
        '<div class="form-group">' +
        '<input type="hidden" class="form-control" id="loginEmail">' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="loginPassword" class="col-form-label">Password:</label>' +
        '<input type="password" class="form-control" id="loginPassword" required>' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="cnName" class="col-form-label">Chinese Name:</label>' +
        '<input type="text" class="form-control" id="cnName" required>' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="enName" class="col-form-label">English Name:</label>' +
        '<input type="text" class="form-control" id="enName" required>' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="phone" class="col-form-label">Phone Number:</label>' +
        '<input type="text" class="form-control" id="phone" required>' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="code" class="col-form-label">Auth Code:</label>' +
        '<input type="text" class="form-control" id="code" required>' +
        '</div>' +
        '<div class="modal-footer">' +
        '<button type="button" class="btn btn-secondary" data-dismiss="modal" >Close</button>' +
        '<button type="submit" class="btn btn-primary" data-dismiss="modal" id="createAccount">Create</button>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>')
}

function signUp1() {
    var loginEmail = $('#loginEmail').val();

    $.ajax({
        type: 'post',
        url: '/user/sign-up-1',
        dataType: 'json',
        data: {
            loginEmail: loginEmail,
        },
        cache: false,
        success: function (data) {
            if (data['status'] == 304){
                $('#email-already-exists').click();
            } else if (data['status'] == 508){
                $('#invalid-email-address').click();
            } else {
                $('#check-inbox-for-auth-code').click();
                signUpForm2();
                $('#loginEmail').attr('value', loginEmail);
                $('#signUpForm2').click();
            }
        }
    })
}

function signUp2(){
    var loginEmail = $('#loginEmail').val();
    var loginPassword = $('#loginPassword').val();
    var cnName = $('#cnName').val();
    var enName = $('#enName').val();
    var phone = $('#phone').val();
    var code = $('#code').val();

    if (loginPassword == ""){
        $('#password-cannot-be-empty').click();
        return;
    } else if (cnName == ""){
        $('#cnName-cannot-be-empty').click();
        return;
    } else if (enName == ""){
        $('#enName-cannot-be-empty').click();
        return;
    } else if (phone == ""){
        $('#phone-cannot-be-empty').click();
        return;
    } else if (code == ""){
        $('#auth-code-cannot-be-empty').click();
        return;
    }

    $.ajax({
        type: 'post',
        url: '/user/sign-up',
        dataType: 'json',
        data: {
            loginEmail: loginEmail,
            loginPassword: loginPassword,
            cnName: cnName,
            enName: enName,
            phone: phone,
            code:code
        },
        cache: false,
        success: function (data) {
            if (data['status'] == 304){
                $('#email-already-exists').click();
            } else if (data['status'] == 305){
                $('#phone-already-exists').click();
            } else if (data['status'] == 507){
                $('#wrong-auth-code').click();
            } else {
                $('#create-account-success').click();
            }
        }
    })
}


$('#newAccount').click(function () {
    signUpForm1();
    $('#signUpForm1').click();
});

$('.index').on('click', '#sendAuthCode', function () {
    signUp1();
});

$('.index').on('click', '#createAccount', function () {
    signUp2();
});