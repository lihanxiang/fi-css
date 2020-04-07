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
                                        '<p class="panel-subtitle">' + object['agendaDate'] +  '</p>' +
                                        '<div class="right">' +
                                            '<button type="button" class="new-event-form" id="' + object['agendaID'] + '">' +
                                                '<i class="lnr lnr-plus-circle"></i>' +
                                                '<span> New</span>' +
                                            '</button>' +
                                        '</div>' +
                                    '</div>' +
                                    '<div class="panel-body">' +
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
                '<p class="panel-subtitle">' + object['agendaDate'] +  '</p>' +
                '<div class="right">' +
                '<button type="button" class="new-event-form" id="' + object['agendaID'] + '">' +
                '<i class="lnr lnr-plus-circle"></i>' +
                '<span> New</span>' +
                '</button>' +
                '</div>' +
                '</div>' +
                '<div class="panel-body">' +
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

var conferenceDiv = $('#conference-div');

conferenceDiv.on("click", ".first-day-in-conference", function () {
    var conferenceID = $(this).attr('id');
    getFirstDayInConference(conferenceID);
});

conferenceDiv.on("click", ".get-agenda-by-id", function () {
    var agendaID = $(this).attr('id');
    getAgendaByID(agendaID);
});