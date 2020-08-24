$(document).ready(function () {
    getValidConferences();
});

$('#conference').click(function () {
    getValidConferences();
    $('#submission-form').css("display", "none");
    $('#my-submissions-list').empty();
});

function getValidConferences(){

    $.ajax({
        type:'get',
        url:'/conference/candidate/show',
        dataType:'json',
        cache:false,
        success:function (data) {
            var conferenceList = $('.conference-list');
            conferenceList.empty();
            conferenceList.append('<div class="panel">' +
                '<div class="panel-heading">' +
                '<h3 class="panel-title">Conference</h3>' +
                '<div class="right">' +
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
                        '<span> Detail </span>' +
                        '</a>');
                    if (object['submissionID'] != null){
                        p.append('<a href="javascript:void(0)" class="submission-detail" ' +
                                    'id="' + object['submissionID'] + '" style="float: right; margin-right: 5%">' +
                                    '<i class="lnr lnr lnr-checkmark-circle"></i>' +
                                    '<span> My submission </span>' +
                                    '</a>');
                    } else {
                        p.append('<a href="javascript:void(0)" class="new-submission-button" ' +
                                'id="' + object['conferenceID'] + '" style="float: right; margin-right: 5%">' +
                                '<i class="lnr lnr lnr-pencil"></i>' +
                                '<span> New submission </span>' +
                                '</a>');
                    }
                    if (object['firstDay'] === object['lastDay']){
                        p.append('<span class="short-description">' + object['firstDay'] + '</span>');
                    } else {
                        p.append('<span class="short-description">From '+ object['firstDay'] + ' To ' + object['lastDay'] + '</span>');
                    }
                    li.append(p);
                    ul.append(li);
                });
            }
        }
    })
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
        }
    })
}



