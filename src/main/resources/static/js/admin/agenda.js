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
                                            '<button type="button" class="new-event-form" id="' + object['agendaID'] + '">' +
                                                '<i class="lnr lnr-plus-circle"></i>' +
                                                '<span> New</span>' +
                                            '</button><br>' +
                                            '<button type="button" class="edit-agenda-form" id="' + object['agendaID'] + '">' +
                                            '<i class="lnr lnr-pencil"></i>' +
                                            '<span> Edit </span>' +
                                            '</button><br>' +
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
                '<button type="button" class="new-event-form" id="' + object['agendaID'] + '">' +
                '<i class="lnr lnr-plus-circle"></i>' +
                '<span> New</span>' +
                '</button><br>' +
                '<button type="button" class="edit-agenda-form" id="' + object['agendaID'] + '">' +
                '<i class="lnr lnr-pencil"></i>' +
                '<span> Edit </span>' +
                '</button><br>' +
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

function editAgendaForm() {
    var modal = $('#modal');
    modal.empty();
    $('#modal').append('<div class="modal fade" id="editAgendaModal" tabindex="-1" role="dialog"' +
        ' aria-labelledby="editAgendaLabel" aria-hidden="true">' +
        '<div class="modal-dialog" role="document">' +
        '<div class="modal-content">' +
        '<div class="modal-header">' +
        '<h5 class="modal-title" id="editAgendaLabel" >Edit Agenda</h5>' +
        '</div>' +
        '<div class="modal-body">' +
        '<form>' +
        '<div class="form-group">' +
        '<input type="hidden" class="form-control" id="agendaID">' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="agendaName" class="col-form-label"><span style="color:red">*</span>Name:</label>' +
        '<input type="text" class="form-control" id="agendaName">' +
        '</div>' +
        '</form>' +
        '</div>' +
        '<div class="modal-footer">' +
        '<button type="button" class="btn btn-secondary" data-dismiss="modal" >Close</button>' +
        '<button type="submit" class="btn btn-primary" data-dismiss="modal" id="editAgenda">Edit</button>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>')
}

function getAgendaDetailForEdit(agendaID) {
    $.ajax({
        type: 'post',
        url: '/agenda/get-by-id',
        dataType: 'json',
        data: {
            agendaID: agendaID
        },
        cache: false,
        success: function (data) {
            var object = data['data']['result'];
            $('#agendaID').attr('value', object['agendaID']);
            $('#agendaName').attr('value', object['agendaName']);
        }
    })
}

function editAgenda() {

    var agendaID = $('#agendaID').val();
    var agendaName = $('#agendaName').val();

    $.ajax({
        type: 'post',
        url: '/agenda/edit',
        dataType: 'json',
        data: {
            agendaID: agendaID,
            agendaName: agendaName,
        },
        cache: false,
        success: function () {
            getAgendaByID(agendaID);
            $('#edit-agenda-success').click();
        }
    })
}


var conferenceDiv = $('#conference-div');

conferenceDiv.on("click", ".first-day-in-conference", function () {
    $('#submission-list').empty();
    var conferenceID = $(this).attr('id');
    getFirstDayInConference(conferenceID);
});

conferenceDiv.on("click", ".get-agenda-by-id", function () {
    var agendaID = $(this).attr('id');
    getAgendaByID(agendaID);
});

conferenceDiv.on("click", ".download-agenda", function () {
    var agendaID = $(this).attr('id');
    $('#downloadAgenda').attr('href', '/agenda/download/' + agendaID);
    document.getElementById('downloadAgenda').click();
});

conferenceDiv.on("click", ".edit-agenda-form", function () {
    var agendaID = $(this).attr('id');
    editAgendaForm();
    getAgendaDetailForEdit(agendaID);
    $('#editAgendaForm').click();
});

conferenceDiv.on("click", "#editAgenda", function () {
    editAgenda();
});