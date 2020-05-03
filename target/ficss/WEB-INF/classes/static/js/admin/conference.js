function getValidConferencesByAdmin(){
    $.ajax({
        type:'get',
        url:'/conference/admin/show',
        dataType:'json',
        cache:false,
        success:function (data) {
            var conferenceList = $('.conference-list');
            conferenceList.empty();
            conferenceList.append('<div class="panel">' +
                '<div class="panel-heading">' +
                '<h3 class="panel-title">Conference</h3>' +
                '<div class="right">' +
                '<button type="button" id="new-conference-form">' +
                '<i class="lnr lnr-plus-circle"></i>' +
                '<span> New</span>' +
                '</button>' +
                '</div>' +
                '</div>' +
                '<div class="panel-body ">' +
                '<ul class="list-unstyled todo-list conference-detail-list"></ul>' +
                '</div>' +
                '</div>');
            var ul = $('.conference-detail-list');
            if (data['status'] === 400){
                ul.append('<li><p><span class="title">No Conference found</span></p></li>');
            } else {
                $.each(data['data']['result'], function (index, object) {
                    var li = $('<li></li>');
                    var p = $('<p></p>');
                    p.append('<span class="title">' + object['conferenceName'] + '</span>');
                    p.append('<a href="javascript:void(0)" class="conference-detail" ' +
                        'id="' + object['conferenceID'] + '" style="float: right">' +
                        '<i class="lnr lnr-pointer-right"></i>' +
                        '<span> Detail</span>' +
                        '</a>' +
                        '<a href="javascript:void(0)" class="download-candidate-form" ' +
                        'id="' + object['conferenceID'] + '" style="float: right; margin-right: 5%">' +
                        '<i class="lnr lnr-download"></i>' +
                        '<span> Candidate Form</span>' +
                        '</a>');
                    if (object['firstDay'] === object['lastDay']){
                        p.append('<span class="short-description">' + object['firstDay'] + '</span>');
                    } else {
                        p.append('<span class="short-description">From '+ object['firstDay'] + ' To ' + object['lastDay'] + '</span>');
                    }
                    li.append(p);
                    ul.append(li);
                });
            }
        },
        error:function (data) {
            notificationMessage("waring", data['message'])
        }
    })
}

function newConferenceForm(){
    var modal = $('#modal');
    modal.empty();
    $('#modal').append('<div class="modal fade" id="createConferenceModal" tabindex="-1" role="dialog"' +
        ' aria-labelledby="createConferenceLabel" aria-hidden="true">' +
        '<div class="modal-dialog" role="document">' +
        '<div class="modal-content">' +
        '<div class="modal-header">' +
        '<h3 class="modal-title" id="createConferenceLabel">Create a new conference</h3>' +
        '</div>' +
        '<div class="modal-body">' +
        '<form>' +
        '<div class="form-group">' +
        '<label for="conferenceName" class="col-form-label">Name:</label>' +
        '<input type="text" class="form-control" id="conferenceName" required>' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="topics" class="col-form-label">Topics(separated by ;): </label>' +
        '<input type="text" class="form-control" id="topics" required>' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="agendaStartDate" class="col-form-label">Start</label>' +
        '<input type="date" class="form-control" id="agendaStartDate" name="agendaStartDate" required>' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="agendaEndDate" class="col-form-label">End</label>' +
        '<input type="date" class="form-control" id="agendaEndDate" name="agendaEndDate" required>' +
        '</div>' +
        '<div class="modal-footer">' +
        '<button type="button" class="btn btn-secondary" data-dismiss="modal" >Close</button>' +
        '<button type="submit" class="btn btn-primary" data-dismiss="modal" id="createConference">Create</button>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>')
}

function getConferenceDetail(conferenceID) {
    $.ajax({
        type:'post',
        url:'/conference/detail',
        dataType:'json',
        data: {
            conferenceID:conferenceID
        },
        cache:false,
        success(data){
            var conferenceDetail = $('#conference-detail');
            conferenceDetail.empty();
            var object = data['data']['result'];
            conferenceDetail.append('<div class="panel">' +
                                        '<div class="panel-heading">' +
                                            '<h3 class="panel-title">' + object['conferenceName'] +'</h3>' +
                                        '</div>' +
                                        '<div class="panel-body">' +
                                            '<ul class="list-unstyled todo-list">' +
                                                '<li>' +
                                                    '<p id="agendaCount">' +
                                                    '</p>' +
                                                '</li>' +
                                                '<li>' +
                                                    '<p id="submissionCount">' +
                                                        '<span class="title">Submission: ' + object['submissionCount'] + '</span>' +
                                                    '</p>' +
                                                '</li>' +
                                            '</ul>' +
                                        '</div>' +
                                    '</div>');
            var agendaCount = object['agendaCount'];
            if (agendaCount === 1){
                $('#agendaCount').append('<span class="title">Duration: ' + agendaCount + ' day</span>');
            } else {
                $('#agendaCount').append('<span class="title">Duration: ' + agendaCount + ' days</span>');
            }
            if (agendaCount > 0){
                $('#agendaCount').append('<a href="javascript:void(0)" class="first-day-in-conference" ' +
                                                'id="' + conferenceID + '" style="float: right">' +
                                                '<i class="lnr lnr-pointer-right"></i>' +
                                                '<span> Detail</span>' +
                                            '</a>');
            }
            if (object['submissionCount'] > 0){
                $('#submissionCount').append('<a href="javascript:void(0)" class="submission-info-list" ' +
                                                    'id="' + conferenceID + '" style="float: right">' +
                                                    '<i class="lnr lnr-pointer-right"></i>' +
                                                    '<span> Detail</span>' +
                                                '</a>');
            }
        },
        error(){
            notificationMessage("danger", "Internal error");
        }
    })
}

function createConference() {
    var conferenceName = $('#conferenceName').val();
    var agendaStartDate = $('#agendaStartDate').val();
    var agendaEndDate = $('#agendaEndDate').val();
    var topics = $('#topics').val();

    if (!conferenceName){
        notificationMessage("danger", "title is required");
        return;
    }
    if (!agendaStartDate){
        notificationMessage("danger", "start date is required");
        return;
    }
    if (!agendaEndDate){
        notificationMessage("danger", "end date is required");
        return;
    }
    if (!topics){
        notificationMessage("danger", "At least one topic is required");
        return;
    }

    $.ajax({
        type:'post',
        url:'/conference/create',
        dataType:'json',
        data:{
            conferenceName:conferenceName,
            agendaStartDate:agendaStartDate,
            agendaEndDate:agendaEndDate,
            topics:topics
        },
        cache:false,
        success:function (data) {
            if (data['status'] == 300){
                notificationMessage("danger", data['message']);
            } else {
                notificationMessage("success", "Success");
                getValidConferencesByAdmin();
            }
        },
        error:function (data) {
            notificationMessage("danger", data['message']);
        }
    })
}

$('#conference').click(function () {
    $('.overview-data').empty();
    $('.conference-list').empty();
    $('#empty-conference-form').empty();
    $('#conference-detail').empty();
    $('#agenda-detail').empty();
    $('#selectedPaper').empty();
    $('#availablePaper').empty();
    $('#submission-list').empty();
    $('#candidate-search-result').empty();
    $('#candidate-submissions-list').empty();
    getValidConferencesByAdmin();
});

var conferenceDiv = $('#conference-div');

conferenceDiv.on("click", "#createConference", function () {
    createConference();
    $('#conference').click();
});

conferenceDiv.on('click', '#new-conference-form', function () {
    newConferenceForm();
    $('#emptyConferenceForm').click();
});

conferenceDiv.on("click", ".conference-detail", function () {
    var conferenceID = $(this).attr('id');
    getConferenceDetail(conferenceID);
});

conferenceDiv.on("click", ".download-candidate-form", function () {
    var conferenceID = $(this).attr('id');
    $('#downloadCandidateForm').attr('href', '/admin/download-candidate-form/' + conferenceID);
    document.getElementById('downloadCandidateForm').click();
});








