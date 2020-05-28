var conferenceDiv = $('#conference-div');

conferenceDiv.on("click", ".show-available-papers", function () {
    var sessionID = $(this).attr('id');
    showAvailablePapers(sessionID);
});

conferenceDiv.on("click", ".add-paper-to-session", function () {
    var IDs = $(this).attr('id').split(';');
    var sessionID = IDs[0];
    var paperFileID = IDs[1];
    addPaperToSession(sessionID, paperFileID);
});

conferenceDiv.on("click", ".remove-paper", function () {
    var IDs = $(this).attr('id').split(';');
    var sessionID = IDs[0];
    var paperFileID = IDs[1];
    deletePaperFromSession(sessionID, paperFileID);
});


function showAvailablePapers(sessionID) {

    $.ajax({
        type: 'post',
        url: '/paper/available-papers',
        dataType: 'json',
        data: {
            sessionID: sessionID,
        },
        cache: false,
        success: function (data) {
            var availablePaper = $('#availablePaper');
            availablePaper.empty();
            availablePaper.append('<div class="panel"> ' +
                                    '<div class="panel-heading">' +
                                    '<h3 class="panel-title">Available Papers</h3>' +
                                    '</div>' +
                                    '<div class="panel-body">' +
                                    '<div class="available-paper-detail" style="overflow: auto; word-break: break-all;word-wrap: break-word;">' +
                                    '</div>' +
                                    '</div>' +
                                    '</div>');
            var ul = $('<ul class="list-unstyled todo-list"></ul>');
            if (data['status'] == 408){
                ul.append('<li>' +
                            '<p>' +
                                '<span class="title">' + data['message'] + '</span>' +
                            '</p>' +
                            '</li>'
                )
            } else {
                $.each(data['data']['result'], function (index, object) {
                    ul.append('<li>' +
                                '<p>' +
                                '<span class="title">' +
                                    '<a href="javascript:void(0)" class="submission-detail" ' +
                                        'id="' + object['submissionID'] + '">' + object['paperTitle'] +
                                    '</a>' +
                                '</span>' +
                                '<span class="short-description">' + object['author'] + '</span>' +
                                '<a href="javascript:void(0)" class="add-paper-to-session" ' +
                                'id="' + sessionID + ';' + object['paperFileID'] + '" style="float: right">' +
                                '<span class="lnr lnr-pushpin"> Add to Session</span>' +
                                '</a>' +
                                '</p>' +
                                '</li>');
                });
            }
            $('.available-paper-detail').append(ul);
        }
    })
}

function addPaperToSession(sessionID, paperFileID) {

    $.ajax({
        type: 'post',
        url: '/paper/add-paper-to-session',
        dataType: 'json',
        data: {
            sessionID: sessionID,
            paperFileID: paperFileID
        },
        cache: false,
        success: function () {
            getPapersInSession(sessionID);
            showAvailablePapers(sessionID);
        }
    })
}

function deletePaperFromSession(sessionID, paperFileID) {

    $.ajax({
        type: 'post',
        url: '/paper/delete-paper-from-session',
        dataType: 'json',
        data: {
            paperFileID: paperFileID
        },
        cache: false,
        success: function () {
            getPapersInSession(sessionID);
            showAvailablePapers(sessionID);
        }
    })
}


