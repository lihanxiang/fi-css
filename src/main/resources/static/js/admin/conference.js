function getValidConferencesByAdmin(){
    $.ajax({
        type:'get',
        url:'/conference/admin/show',
        dataType:'json',
        cache:false,
        success:function (data) {
            var conferenceList = $('.conference-list');
            conferenceList.empty();
            var ul = $('<ul class="list-unstyled todo-list"></ul>');
            if (data['status'] == 400){
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
                        '</a>');
                    if (object['firstDay'] == object['lastDay']){
                        p.append('<span class="short-description">' + object['firstDay'] + '</span>');
                    } else {
                        p.append('<span class="short-description">From '+ object['firstDay'] + ' To ' + object['lastDay'] + '</span>');
                    }
                    li.append(p);
                    ul.append(li);
                });
            }
            conferenceList.append(ul);
        },
        error:function (data) {
            notificationMessage("waring", data['message'])
        }
    })
}

$('#get-empty-conference-form').click(function () {
    var form = $('#empty-conference-form');
    form.empty();
    form.append('<div class="panel">' +
                    '<div class="panel-heading">' +
                        '<h3 class="panel-title">Create a new conference</h3>' +
                        '<div class="right">' +
                            '<button type="button" class="conference-form-remove">' +
                                '<i class="lnr lnr-cross"></i>' +
                            '</button>' +
                        '</div>' +
                    '</div>' +
                    '<div class="panel-body">' +
                        '<div class="form-group">' +
                            '<label for="conferenceName" class="col-form-label">Title</label>' +
                            '<input type="text" class="form-control" id="conferenceName" name="conferenceName">' +
                        '</div>' +
                        '<div class="form-group">' +
                            '<label for="topics" class="col-form-label">Topics</label>' +
                            '<div class="alert alert-info alert-dismissible" role="alert">' +
                                '<button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
                                    '<span aria-hidden="true">&times</span>' +
                                '</button>' +
                                '<i class="fa fa-info-circle"></i> Topics of this conference, separated by ;'+
                            '</div>' +
                            '<input type="text" class="form-control" id="topics" name="topics">' +
                        '</div>' +
                        '<div class="form-group">' +
                            '<label for="agendaStartDate" class="col-form-label">Start</label>' +
                            '<input type="date" class="form-control" id="agendaStartDate" name="agendaStartDate">' +
                        '</div>' +
                        '<div class="form-group">' +
                            '<label for="agendaEndDate" class="col-form-label">End</label>' +
                            '<input type="date" class="form-control" id="agendaEndDate" name="agendaEndDate">' +
                        '</div><br>' +
                        '<button type="submit" style="float:right" class="btn btn-primary" id="create-conference">' +
                            'Submit' +
                        '</button>' +
                    '</div>' +
                '</div>');
});

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
                $('#submissionCount').append('<a href="javascript:void(0)" class="submission-detail" ' +
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
        success:function () {
            notificationMessage("success", "Create agenda success");
            getValidConferences();
            $('#empty-conference-form').empty();
        },
        error:function () {
            alert("error");
        }
    })
}

$('#conference').click(function () {
    getValidConferencesByAdmin();
});

var conferenceDiv = $('#conference-div');
var emptyConferenceForm = $('#empty-conference-form');

conferenceDiv.on("click", "#create-conference", function () {
    createConference();
});

conferenceDiv.on("click", ".conference-detail", function () {
    var conferenceID = $(this).attr('id');
    emptyConferenceForm.empty();
    getConferenceDetail(conferenceID);
});









