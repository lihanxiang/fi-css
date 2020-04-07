
function newSessionForm(eventID) {
    var modal = $('#modal');
    modal.empty();
    $('#modal').append('<div class="modal fade" id="createSessionModal" tabindex="-1" role="dialog"' +
                            ' aria-labelledby="createSessionLabel" aria-hidden="true">' +
                            '<div class="modal-dialog" role="document">' +
                                '<div class="modal-content">' +
                                    '<div class="modal-header">' +
                                        '<h5 class="modal-title" id="createSessionLabel" >New Session</h5>' +
                                    '</div>' +
                                    '<div class="modal-body">' +
                                        '<form>' +
                                            '<div class="form-group">' +
                                                '<input type="hidden" class="form-control" id="eventID" value="' + eventID + '">' +
                                            '</div>' +
                                            '<div class="form-group">' +
                                                '<label for="eventName" class="col-form-label"><span style="color:red">*</span>Name:</label>' +
                                                '<input type="text" class="form-control" id="sessionName">' +
                                            '</div>' +
                                            '<div class="form-group">' +
                                                '<label for="sessionRoom" class="col-form-label"><span style="color:red">*</span>Room:</label>' +
                                                '<input type="text" class="form-control" id="sessionRoom">' +
                                            '</div>' +
                                            '<div class="form-group">' +
                                                '<label for="sessionReviewer" class="col-form-label">Reviewer name: (separate by ;)</label>' +
                                                '<input type="text" class="form-control" id="sessionReviewer">' +
                                            '</div>' +
                                            '<div class="form-group">' +
                                                '<label for="sessionChair" class="col-form-label">Chair name: (separate by ;)</label>' +
                                                '<input type="text" class="form-control" id="sessionChair">' +
                                            '</div>' +
                                        '</form>' +
                                    '</div>' +
                                    '<div class="modal-footer">' +
                                        '<button type="button" class="btn btn-secondary" data-dismiss="modal" >Close</button>' +
                                        '<button type="submit" class="btn btn-primary" data-dismiss="modal" id="createSession">Create</button>' +
                                    '</div>' +
                                '</div>' +
                            '</div>' +
                        '</div>')
}

function createSession() {

    var eventID = $('#eventID').val();
    var sessionName = $('#sessionName').val();
    var sessionRoom = $('#sessionRoom').val();
    var sessionReviewer = $('#sessionReviewer').val();
    var sessionChair = $('#sessionChair').val();

    $.ajax({
        type: 'post',
        url: '/session/create',
        dataType: 'json',
        data: {
            eventID: eventID,
            sessionName:sessionName,
            sessionRoom:sessionRoom,
            sessionReviewer: sessionReviewer,
            sessionChair:sessionChair
        },
        cache: false,
        success: function () {
            getFirstSession(eventID);
        },
        error:function () {
            notificationMessage("danger", "error");
        }
    })
}

function editSessionForm() {
    var modal = $('#modal');
    modal.empty();
    $('#modal').append('<div class="modal fade" id="editSessionModal" tabindex="-1" role="dialog"' +
                            ' aria-labelledby="editSessionLabel" aria-hidden="true">' +
                            '<div class="modal-dialog" role="document">' +
                                '<div class="modal-content">' +
                                    '<div class="modal-header">' +
                                        '<h5 class="modal-title" id="editSessionLabel" >Edit Session</h5>' +
                                    '</div>' +
                                    '<div class="modal-body">' +
                                        '<form>' +
                                            '<div class="form-group">' +
                                                '<input type="hidden" class="form-control" id="sessionID">' +
                                            '</div>' +
                                            '<div class="form-group">' +
                                                '<label for="sessionName" class="col-form-label"><span style="color:red">*</span>Name:</label>' +
                                                '<input type="text" class="form-control" id="sessionName">' +
                                            '</div>' +
                                            '<div class="form-group">' +
                                                '<label for="sessionRoom" class="col-form-label"><span style="color:red">*</span>Room:</label>' +
                                                '<input type="text" class="form-control" id="sessionRoom">' +
                                            '</div>' +
                                            '<div class="form-group">' +
                                                '<label for="sessionReviewer" class="col-form-label">Reviewer name: (separate by ;)</label>' +
                                                '<input type="text" class="form-control" id="sessionReviewer">' +
                                            '</div>' +
                                            '<div class="form-group">' +
                                                '<label for="sessionChair" class="col-form-label">Chair name: (separate by ;)</label>' +
                                                '<input type="text" class="form-control" id="sessionChair">' +
                                            '</div>' +
                                        '</form>' +
                                    '</div>' +
                                    '<div class="modal-footer">' +
                                        '<button type="button" class="btn btn-secondary" data-dismiss="modal" >Close</button>' +
                                        '<button type="submit" class="btn btn-primary" data-dismiss="modal" id="editSession">Edit</button>' +
                                    '</div>' +
                                '</div>' +
                            '</div>' +
                        '</div>')
}

function getSessionDetailForEdit(sessionID) {
    $.ajax({
        type: 'post',
        url: '/session/get-session-by-id',
        dataType: 'json',
        data: {
            sessionID: sessionID
        },
        cache: false,
        success: function (data) {
            var object = data['data']['result'];
            $('#sessionID').attr('value', object['sessionID']);
            $('#sessionName').attr('value', object['sessionName']);
            $('#sessionRoom').attr('value', object['sessionRoom']);
            $('#sessionReviewer').attr('value', object['sessionReviewer']);
            $('#sessionChair').attr('value', object['sessionChair']);
        },
        error:function (data) {
            notificationMessage("danger", data['message']);
        }
    })
}

function editSession() {

    var sessionID = $('#sessionID').val();
    var sessionName = $('#sessionName').val();
    var sessionRoom = $('#sessionRoom').val();
    var sessionReviewer = $('#sessionReviewer').val();
    var sessionChair = $('#sessionChair').val();

    $.ajax({
        type: 'post',
        url: '/session/first-session',
        dataType: 'json',
        data: {
            sessionID: sessionID,
            sessionName: sessionName,
            sessionRoom: sessionRoom,
            sessionReviewer: sessionReviewer,
            sessionChair: sessionChair,
        },
        cache: false,
        success: function () {
            getSessionByID(sessionID);
        },
        error:function (data) {
            notificationMessage("danger", data['message']);
        }
    })
}

function getFirstSession(eventID) {
    $.ajax({
        type: 'post',
        url: '/session/first-session',
        dataType: 'json',
        data: {
            eventID: eventID
        },
        cache: false,
        success: function (data) {
            var selectedPaper = $('#selectedPaper');
            selectedPaper.empty();
            if (data['status'] == 403){
                selectedPaper.append('<div class="panel"> ' +
                                        '<div class="panel-heading">' +
                                            '<h3 class="panel-title">No session in this event</h3>' +
                                            '<div class="right">' +
                                                '<button type="button" class="new-session-form" id="' + eventID + '">' +
                                                    '<i class="lnr lnr-plus-circle"></i>' +
                                                    '<span> New</span>' +
                                                '</button>' +
                                            '</div>' +
                                        '</div>' +
                                        '<div class="panel-body"></div>' +
                                    '</div>')
            } else {
                var object = data['data']['result'];
                selectedPaper.append('<div class="panel">' +
                                        '<div class="panel-heading">' +
                                            '<h3 class="panel-title">' + object['sessionName'] +'</h3>' +
                                            '<p class="panel-subtitle">Room ' + object['sessionRoom'] + '</p>' +
                                            '<div class="right">' +
                                                '<button type="button" class="new-session-form" id="' + eventID + '">' +
                                                    '<i class="lnr lnr-plus-circle"></i>' +
                                                    '<span> New</span>' +
                                                '</button>' +
                                            '</div>' +
                                        '</div>' +
                                        '<div class="panel-body">' +
                                            '<div class="paper-list" style="overflow: auto; word-break: break-all;word-wrap: break-word;">' +
                                            '</div>' +
                                            '<div style="margin-bottom: 5%">' +
                                                '<div class="col-sm-4"></div>' +
                                                '<div class="col-sm-4"></div>' +
                                                '<div class="col-sm-4">' +
                                                    '<div class="right">' +
                                                        '<a href="javascript:void(0)" class="get-session-by-id nextSession" ' +
                                                        'style="float: right" id="' + object['nextSessionID'] + '"></a>' +
                                                    '</div>' +
                                                '</div>' +
                                            '</div>' +
                                        '</div>' +
                                    '</div>');
                getPapersInSession(object['sessionID']);
                if (!object['nextSessionID']){
                    $('.nextSession').remove();
                } else if (object['nextSessionID']){
                    $('.nextSession').append('<i class="lnr lnr-arrow-right-circle"></i>' +
                                                '<span> Next</span>');
                }
            }
        }
    });
}

function getSessionByID(sessionID) {

    $.ajax({
        type: 'post',
        url: '/session/get-session-by-id',
        dataType: 'json',
        data: {
            sessionID: sessionID
        },
        cache: false,
        success: function (data) {
            var selectedPaper = $('#selectedPaper');
            selectedPaper.empty();
            var object = data['data']['result'];
            selectedPaper.append('<div class="panel">' +
                                    '<div class="panel-heading">' +
                                        '<h3 class="panel-title">' + object['sessionName'] +'</h3>' +
                                        '<p class="panel-subtitle">Room ' + object['sessionRoom'] + '</p>' +
                                        '<div class="right">' +
                                            '<button type="button" class="edit-session-form" id="' + object['sessionID'] + '">' +
                                                '<i class="lnr lnr-pencil"></i>' +
                                                '<span> Edit</span>' +
                                            '</button>' +
                                            '<button type="button" class="new-session-form" id="' + object['eventID'] + '">' +
                                                '<i class="lnr lnr-plus-circle"></i>' +
                                                '<span> New</span>' +
                                            '</button>' +
                                        '</div>' +
                                    '</div>' +
                                    '<div class="panel-body">' +
                                        '<div class="paper-list" style="overflow: auto; word-break: break-all;word-wrap: break-word;"></div>' +
                                        '<div style="margin-bottom: 5%">' +
                                            '<div class="col-sm-4">' +
                                                '<div class="left">' +
                                                '<a href="javascript:void(0)" class="get-session-by-id previousSession" style="float: left" ' +
                                                'id="' + object['previousSessionID'] + '"></a>' +
                                                '</div>' +
                                            '</div>' +
                                            '<div class="col-sm-4"></div>' +
                                            '<div class="col-sm-4">' +
                                                '<div class="right">' +
                                                '<a href="javascript:void(0)" class="get-session-by-id nextSession" style="float: right" ' +
                                                'id="' + object['nextSessionID'] + '"></a>' +
                                                '</div>' +
                                            '</div>' +
                                        '</div>' +
                                    '</div>' +
                                '</div>');
            getPapersInSession(object['sessionID']);
            if (!object['previousSessionID']){
                $('.previousSession').remove();
            } else if (object['previousSessionID']){
                $('.previousSession').append('<i class="lnr lnr-arrow-left-circle"></i>' +
                                                '<span> Previous</span>');
            }
            if (!object['nextSessionID']){
                $('.nextSession').remove();
            } else if (object['nextSessionID']){
                $('.nextSession').append('<i class="lnr lnr-arrow-right-circle"></i>' +
                                            '<span> Next</span>');
            }
        }
    })
}

function getPapersInSession(sessionID) {
    $.ajax({
        type: 'post',
        url: '/session/paper-in-session',
        dataType: 'json',
        data: {
            sessionID: sessionID
        },
        cache: false,
        success: function (data) {
            var paperList = $('.paper-list');
            paperList.empty();
            var ul = $('<ul class="list-unstyled todo-list"></ul>');
            if (data['status'] == 407){
                ul.append('<li>' +
                            '<p>' +
                                '<span class="title">No paper found in this session</span>' +
                            '</p>' +
                        '</li>'
                )
            } else {
                $.each(data['data']['result'], function (index, object) {
                    ul.append('<li>' +
                                '<p>' +
                                    '<span class="title">' + object['paperTitle'] + '</span>' +
                                    '<a href="javascript:void(0)" class="submission-detail" ' +
                                    'id="' + object['submissionID'] + '" style="float: right">' +
                                    '<span class="lnr lnr-pointer-right"> Detail</span>' +
                                    '</a>' +
                                    '<a href="javascript:void(0)" class="remove-paper" ' +
                                    'id="' + object['paperFileID'] + '" style="float: right">' +
                                    '<span class="lnr lnr-trash"> Remove</span>' +
                                    '</a>' +
                                    '<span class="short-description">' + object['author'] + '</span>' +
                                '</p>' +
                            '</li>');
                });
            }
            ul.append('<li>' +
                        '<p>' +
                        '<span class="short-description">' +
                            '<a href="javascript:void(0)" class="show-available-papers" id="' + sessionID + '">' +
                                'Show available papers' +
                            '</a>' +
                        '</span>' +
                        '</p>' +
                        '</li>'
            );
            paperList.append(ul);
        }
    })
}

var conferenceDiv = $('#conference-div');

conferenceDiv.on("click", ".session-detail", function () {
    var eventID = $(this).attr('id');
    getFirstSession(eventID);
});

conferenceDiv.on("click", ".new-session-form", function () {
    var eventID = $(this).attr('id');
    newSessionForm(eventID);
    $('#createSessionForm').click();
});

conferenceDiv.on("click", "#createSession", function () {
    createSession();
});

conferenceDiv.on("click", ".edit-session-form", function () {
    var sessionID = $(this).attr('id');
    editSessionForm();
    getSessionDetailForEdit(sessionID);
    $('#editSessionForm').click();
});

conferenceDiv.on("click", "#editSession", function () {
    editSession();
});

conferenceDiv.on("click", ".get-session-by-id", function () {
    var sessionID = $(this).attr('id');
    getSessionByID(sessionID);
});

