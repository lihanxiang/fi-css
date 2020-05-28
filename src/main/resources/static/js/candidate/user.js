function getProfileEventForm() {
    var modal = $('#modal');
    modal.empty();
    $('#modal').append('<div class="modal fade" id="profileModal" tabindex="-1" role="dialog"' +
        ' aria-labelledby="profileLabel" aria-hidden="true">' +
        '<div class="modal-dialog" role="document">' +
        '<div class="modal-content">' +
        '<div class="modal-header">' +
        '<h3 class="modal-title" id="profileLabel" >My Profile</h3>' +
        '</div>' +
        '<div class="modal-body">' +
        '<form>' +
        '<div class="form-group">' +
        '<input type="hidden" class="form-control" id="userID">' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="cnName" class="col-form-label">Chinese Name:</label>' +
        '<input type="text" class="form-control" id="cnName">' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="enName" class="col-form-label">English Name:</label>' +
        '<input type="text" class="form-control" id="enName">' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="email" class="col-form-label">Email</label>' +
        '<input type="text" class="form-control" id="email">' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="phone" class="col-form-label">Phone Number</label>' +
        '<input type="text" class="form-control" id="phone">' +
        '</div>' +
        '</form>' +
        '</div>' +
        '<div class="modal-footer">' +
        '<button type="button" class="btn btn-secondary" data-dismiss="modal" >Close</button>' +
        '<button type="submit" class="btn btn-primary" data-dismiss="modal" id="editProfile">Edit</button>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>');
}

function getProfileDetail(){
    $.ajax({
        type: 'get',
        url: '/user/profile',
        dataType: 'json',
        data: {},
        cache: false,
        success(data) {
            var object = data['data']['result'];
            $('#userID').attr('value', object['userID']);
            $('#cnName').attr('value', object['cnName']);
            $('#enName').attr('value', object['enName']);
            $('#email').attr('value', object['email']);
            $('#phone').attr('value', object['phone']);
        }
    })
}

function editProfile() {
    var userID = $('#userID').val();
    var cnName = $('#cnName').val();
    var enName = $('#enName').val();
    var email = $('#email').val();
    var phone = $('#phone').val();

    $.ajax({
        type: 'post',
        url: '/user/profile/edit',
        dataType: 'json',
        data: {
            userID:userID,
            cnName:cnName,
            enName:enName,
            email:email,
            phone:phone,
        },
        cache: false,
        success() {
            $('#edit-profile-success').click();
        }
    })
}

$('#profile').click(function () {
    getProfileEventForm();
    getProfileDetail();
    $('#profileForm').click();
});

$('#index').on('click', '#editProfile', function f() {
    editProfile();
});