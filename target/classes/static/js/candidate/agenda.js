function getFirstDayInConference(conferenceID) {

    $.ajax({
        type:'post',
        url:'/agenda/first-day-in-conference',
        dataType:'json',
        data:{
            conferenceID:conferenceID
        },
        cache:false,
        success:function (data) {
            var agendaDetail = $('#agenda-detail');
            agendaDetail.empty();
            var object = data['data']['result'];
            agendaDetail.append('<div class="panel"> ' +
                '<div class="panel-heading">' +
                '<h3 class="panel-title">' + object['agendaName'] +'</h3>' +
                '<div class="right">' +
                '<button type="button" class="download-agenda" id="' + object['agendaID'] + '">' +
                '<i class="lnr lnr-download"></i>' +
                '<span> Download </span>' +
                '</button>' +
                '</div>' +
                '</div>' +
                '<div class="panel-body">' +
                '<p>' + object['agendaDate'] + '</p>' +
                '<div class="event-list" style="overflow: auto; word-break: break-all;word-wrap: break-word;">' +
                '</div>' +
                '<div style="margin-bottom: 5%">' +
                '<div class="col-sm-4"></div>' +
                '<div class="col-sm-4"></div>' +
                '<div class="col-sm-4">' +
                '<div class="right">' +
                '<a href="javascript:void(0)" class="get-agenda-by-id nextDay" ' +
                'style="float: right"  id="' + object['nextDayAgendaID'] + '"></a>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>');
            getEventsInAgenda(object['agendaID']);
            if (!object['nextDayAgendaID']){
                $('.nextDay').remove();
            } else if (object['nextDayAgendaID']){
                $('.nextDay').append('<i class="lnr lnr-arrow-right-circle"></i>' +
                    '<span> Next</span>');
            }
        }
    })
}

function getAgendaByID(agendaID){
    $.ajax({
        type:'post',
        url:'/agenda/get-by-id',
        dataType:'json',
        data:{
            agendaID:agendaID
        },
        cache:false,
        success:function (data) {
            var agendaDetail = $('#agenda-detail');
            agendaDetail.empty();
            var object = data['data']['result'];
            agendaDetail.append('<div class="panel"> ' +
                '<div class="panel-heading">' +
                '<h3 class="panel-title">' + object['agendaName'] +'</h3>' +
                '<div class="right">' +
                '<button type="button" class="download-agenda" id="' + object['agendaID'] + '">' +
                '<i class="lnr lnr-download"></i>' +
                '<span> Download </span>' +
                '</button>' +
                '</div>' +
                '</div>' +
                '<div class="panel-body">' +
                '<p>' + object['agendaDate'] + '</p>' +
                '<div class="event-list" style="overflow: auto; word-break: break-all;word-wrap: break-word;"></div>' +
                '<div style="margin-bottom: 5%">' +
                '<div class="col-sm-4">' +
                '<div class="left">' +
                '<a href="javascript:void(0)" class="get-agenda-by-id previousDay" style="float: left" ' +
                'id="' + object['previousDayAgendaID'] + '"></a>' +
                '</div>' +
                '</div>' +
                '<div class="col-sm-4"></div>' +
                '<div class="col-sm-4">' +
                '<div class="right">' +
                '<a href="javascript:void(0)" class="get-agenda-by-id nextDay" style="float: right" ' +
                'id="' + object['nextDayAgendaID'] + '"></a>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>');
            getEventsInAgenda(object['agendaID']);
            if (!object['previousDayAgendaID']){
                $('.previousDay').remove();
            } else if (object['previousDayAgendaID']){
                $('.previousDay').append('<i class="lnr lnr-arrow-left-circle"></i>' +
                    '<span class=""> Previous</span>');
            }
            if (!object['nextDayAgendaID']){
                $('.nextDay').remove();
            } else if (object['nextDayAgendaID']){
                $('.nextDay').append('<i class="lnr lnr-arrow-right-circle"></i>' +
                    '<span class=""> Next</span>');
            }
        }
    })
}

function getEventsInAgenda(agendaID) {

    $.ajax({
        type: 'post',
        url: '/event/events-in-agenda',
        dataType: 'json',
        data: {
            agendaID: agendaID
        },
        cache:false,
        success:function (data) {
            var eventList = $('.event-list');
            eventList.empty();
            var ul = $('<ul class="list-unstyled todo-list"></ul>');
            if (data['status'] == 402) {
                ul.append('<li>' +
                    '<p>' +
                    '<span class="title">No event found</span>' +
                    '</p>' +
                    '</li>'
                )
            } else {
                $.each(data['data']['result'], function (index, object) {
                    if (agendaID == object['agendaID']){
                        var title = '<span class="title">' + object['eventName'] + '</span>';
                        if (object['status'] == 1){
                            ul.append('<li>' +
                                '<p>' +
                                '<span class="title">' + object['eventName'] + '</span>' +
                                '<span class="short-description">' + object['eventStartTime'] + ' - ' + object['eventEndTime'] + '</span>' +
                                '</p>' +
                                '</li>');
                        } else {
                            ul.append('<li>' +
                                '<p class="agenda-detail">' +
                                '<span class="title">' + object['eventName'] + '</span>' +
                                '<span class="short-description">' + object['eventStartTime'] +
                                ' - ' + object['eventEndTime'] + '</span>' +
                                '<span class="date">Room ' + object['room'] + '</span>' +
                                '</p>' +
                                '</li>');
                        }
                    }
                });
            }
            eventList.append(ul);
        }
    })
}


$('#conference-detail').on("click", ".first-day-in-conference", function () {
    var conferenceID = $(this).attr('id');
    getFirstDayInConference(conferenceID);
});

$('#index').on("click", ".get-agenda-by-id", function () {
    var agendaID = $(this).attr('id');
    getAgendaByID(agendaID);
});

$('#index').on("click", ".download-agenda", function () {
    var agendaID = $(this).attr('id');
    $('#downloadAgenda').attr('href', '/agenda/download/' + agendaID);
    document.getElementById('downloadAgenda').click();
});