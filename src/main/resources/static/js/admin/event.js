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
                        if (object['status'] == 1){
                            ul.append('<li>' +
                                '<p>' +
                                '<span class="title">' + object['eventName'] + '</span>' +
                                '<span class="short-description">' + object['eventStartTime'] + ' - ' + object['eventEndTime'] + '</span>' +
                                '<span class="short-description">' + object['description'] + '</span>' +
                                '<a href="javascript:void(0)" class="remove-event" ' +
                                'id="' + object['eventID'] + '" style="float: right">' +
                                '<span class="lnr lnr-trash"> Remove</span>' +
                                '</a>' +
                                '<a href="javascript:void(0)" class="edit-event-form" ' +
                                'id="' + object['eventID'] + '" style="float: right; margin-right: 5%">' +
                                '<span class="lnr lnr-pencil"> Edit</span>' +
                                '</a>' +
                                '<a href="javascript:void(0)" class="session-detail" ' +
                                'id="' + object['eventID'] + '" style="float: right; margin-right: 5%">' +
                                '<span class="lnr lnr-pointer-right"> Detail</span>' +
                                '</a>' +
                                '</p>' +
                                '</li>');
                        } else {
                            ul.append('<li>' +
                                '<p class="agenda-detail">' +
                                '<span class="title">' + object['eventName'] + '</span>' +
                                '<span class="short-description">' + object['eventStartTime'] +
                                ' - ' + object['eventEndTime'] + '</span>' +
                                '<span class="short-description">' + object['description'] + '</span>' +
                                '<span class="date">Room ' + object['room'] + '</span>' +
                                '<a href="javascript:void(0)" class="remove-event" ' +
                                'id="' + object['eventID'] + '" style="float: right">' +
                                '<span class="lnr lnr-trash"> Remove</span>' +
                                '</a>' +
                                '<a href="javascript:void(0)" class="edit-event-form" ' +
                                'id="' + object['eventID'] + '" style="float: right; margin-right: 5%">' +
                                '<span class="lnr lnr-pencil"> Edit</span>' +
                                '</a>' +
                                '</p>' +
                                '</li>');
                        }
                    }
                });
            }
            eventList.append(ul);
        },
        error:function () {
            notificationMessage("danger", "error");
        }
    })
}

function getEditEventForm() {
    var modal = $('#modal');
    modal.empty();
    $('#modal').append('<div class="modal fade" id="editEventModal" tabindex="-1" role="dialog"' +
        ' aria-labelledby="editEventLabel" aria-hidden="true">' +
        '<div class="modal-dialog" role="document">' +
        '<div class="modal-content">' +
        '<div class="modal-header">' +
        '<h5 class="modal-title" id="editEventLabel" >Edit event</h5>' +
        '</div>' +
        '<div class="modal-body">' +
        '<form>' +
        '<div class="form-group">' +
        '<input type="hidden" class="form-control" id="eventID">' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="eventName" class="col-form-label"><span style="color:red">*</span>Name:</label>' +
        '<input type="text" class="form-control" id="eventName">' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="eventStartTime" class="col-form-label">' +
        '<span style="color:red">*</span>' +
        'Start: (Format: HH:mm, like 14:00)' +
        '</label>' +
        '<input type="text" class="form-control" id="eventStartTime">' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="eventEndTime" class="col-form-label">' +
        '<span style="color:red">*</span>' +
        'End: (Format: HH:mm, like 16:00)' +
        '</label>' +
        '<input type="text" class="form-control" id="eventEndTime">' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="room" class="col-form-label">Room: (If this event has session(s), don\'t fill in this field)</label>' +
        '<input type="text" class="form-control" id="room">' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="description" class="col-form-label">Description:</label>' +
        '<input type="text" class="form-control" id="description">' +
        '</div>' +
        '</form>' +
        '</div>' +
        '<div class="modal-footer">' +
        '<button type="button" class="btn btn-secondary" data-dismiss="modal" >Close</button>' +
        '<button type="submit" class="btn btn-primary" data-dismiss="modal" id="editEvent">Edit</button>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>');
}

function getEventDetailForEdit(eventID) {
    $.ajax({
        type: 'post',
        url: '/event/get-events-for-edit',
        dataType: 'json',
        data: {
            eventID: eventID
        },
        cache: false,
        success: function (data) {
            var object = data['data']['result'];
            $('#eventID').attr('value', object['eventID']);
            $('#eventName').attr('value', object['eventName']);
            $('#eventStartTime').attr('value', object['eventStartTime']);
            $('#eventEndTime').attr('value', object['eventEndTime']);
            $('#room').attr('value', object['room']);
            $('#description').attr('value', object['description']);
        },
        error:function () {
            notificationMessage("danger", "error");
        }
    })
}

function editEvent() {
    var eventID = $('#eventID').val();
    var eventName = $('#eventName').val();
    var eventStartTime = $('#eventStartTime').val();
    var eventEndTime = $('#eventEndTime').val();
    var room = $('#room').val();
    var description = $('#description').val();

    $.ajax({
        type: 'post',
        url: '/event/edit',
        dataType: 'json',
        data: {
            eventID: eventID,
            eventName: eventName,
            eventStartTime: eventStartTime,
            eventEndTime: eventEndTime,
            room: room,
            description: description,
        },
        cache: false,
        success: function (data) {
            getAgendaByID(data['data']['result']['agendaID']);
        },
        error:function () {
            notificationMessage("danger", "error");
        }
    })
}

function removeEvent(eventID) {
    $.ajax({
        type: 'post',
        url: '/event/remove',
        dataType: 'json',
        data: {
            eventID:eventID,
        },
        success:function (data) {
            getAgendaByID(data['data']['result']['agendaID']);
        },
        error:function () {
            notificationMessage("danger", "error");
        }
    })
}

function createEvent() {

    var agendaID = $('#agendaID').val();
    var eventName = $('#eventName').val();
    var eventStartTime = $('#eventStartTime').val();
    var eventEndTime = $('#eventEndTime').val();
    var room = $('#room').val();
    var description = $('#description').val();

    $.ajax({
        type:'post',
        url:'/event/create',
        dataType: 'json',
        data: {
            agendaID: agendaID,
            eventName: eventName,
            eventStartTime: eventStartTime,
            eventEndTime: eventEndTime,
            room: room,
            description: description,
        },
        cache:false,
        success:function (data) {
            if (data['status'] == 302){
                notificationMessage("danger", "event with same time already exists");
            }
            getEventsInAgenda(agendaID);
            $('#createEventModal').empty();
        }
    })
}

function newEventForm() {

    var modal = $('#modal');
    modal.empty();
    $('#modal').append('<div class="modal fade" id="createEventModal" tabindex="-1" role="dialog"' +
        ' aria-labelledby="createEventLabel" aria-hidden="true">' +
        '<div class="modal-dialog" role="document">' +
        '<div class="modal-content">' +
        '<div class="modal-header">' +
        '<h5 class="modal-title" id="createEventLabel" >New Event</h5>' +
        '</div>' +
        '<div class="modal-body">' +
        '<form>' +
        '<div class="form-group">' +
        '<input type="hidden" class="form-control" id="agendaID">' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="eventName" class="col-form-label"><span style="color:red">*</span>Name:</label>' +
        '<input type="text" class="form-control" id="eventName">' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="eventStartTime" class="col-form-label">' +
        '<span style="color:red">*</span>' +
        'Start: (Format: HH:mm, like 14:00)' +
        '</label>' +
        '<input type="text" class="form-control" id="eventStartTime">' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="eventEndTime" class="col-form-label">' +
        '<span style="color:red">*</span>' +
        'End: (Format: HH:mm, like 16:00)' +
        '</label>' +
        '<input type="text" class="form-control" id="eventEndTime">' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="room" class="col-form-label">Room: (If this event has session(s), don\'t fill in this field)</label>' +
        '<input type="text" class="form-control" id="room">' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="description" class="col-form-label">Description:</label>' +
        '<input type="text" class="form-control" id="description">' +
        '</div>' +
        '</form>' +
        '</div>' +
        '<div class="modal-footer">' +
        '<button type="button" class="btn btn-secondary" data-dismiss="modal" >Close</button>' +
        '<button type="submit" class="btn btn-primary" data-dismiss="modal" id="createEvent">Create</button>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>')
}

var conferenceDiv = $('#conference-div');

conferenceDiv.on("click", ".new-event-form", function () {
    newEventForm();
    var agendaID = $(this).attr('id');
    $('#agendaID').attr("value", agendaID);
    $('#emptyEventForm').click();
});

conferenceDiv.on("click", "#createEvent", function () {
    createEvent();
});

conferenceDiv.on("click", ".edit-event-form", function () {
    getEditEventForm();
    var eventID = $(this).attr('id');
    getEventDetailForEdit(eventID);
    $('#editEventForm').click();
});

conferenceDiv.on("click", "#editEvent", function () {
    editEvent();
});

conferenceDiv.on("click", ".remove-event", function () {
    var eventID = $(this).attr('id');
    removeEvent(eventID);
});