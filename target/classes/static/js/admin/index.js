


$('.search-candidate').click(function () {
    searchCandidate();
});

function searchCandidate() {

    var cnName = $('#cnName').val();
    var enName = $('#enName').val();
    var email = $('#email').val();
    var phone = $('#phone').val();

    if (!cnName){
        cnName = "ignore";
    }
    if (!enName){
        enName = "ignore";
    }
    if (!email){
        email = "ignore";
    }
    if (!phone){
        phone = "ignore";
    }

    $.ajax({
        type:'post',
        url:'/admin/search-candidate',
        dataType:'json',
        cache:false,
        data:{
            cnName:cnName,
            enName:enName,
            email:email,
            phone:phone,
        },
        success:function (data) {
            var searchResult = $('.search-result');
            var resultNum = $('.result-num');
            resultNum.empty();
            //resultNum.append($('<i class="lnr lnr-database"></i>'));
            if (data['status'] == 100) {
                resultNum.append('Candidates found: 0');
            } else {
                var i = 0;
                searchResult.empty();
                var table = $('<table class="table"></table>');
                var thead = $('<thead>' +
                    '<tr>' +
                    '<th>Chinese Name</th><th>English Name</th><th>Email</th><th>Phone</th>' +
                    '</tr>' +
                    '</thead>');
                var tbody = $('<tbody></tbody>');
                $.each(data['data']['result'], function (index, object) {
                    var tr = $('<tr></tr>');
                    tr.append($('<td>' + object['ChineseName'] + '</td>' +
                        '<td>' + object['EnglishName'] + '</td>' +
                        '<td>' + object['email'] + '</td>'+
                        '<td>' + object['email'] + '</td>'));
                    tbody.append(tr);
                    i++;
                });
                table.append(thead);
                table.append(tbody);
                searchResult.append(table);
                resultNum.append('Candidates found: ' + i);
            }
        },
        error:function () {

        }
    })
}

$('#agenda').click(function () {
    $('#overview').css("display", "none");
    $('#search-candidate-form').css("display", "none");
    showAgenda();
});

function showAgenda(){
    $.ajax({
        type: 'get',
        url: '/admin/agenda/show',
        dataType: 'json',
        cache: false,
        success:function (data) {
            var agendaDetail = $('.agenda-list');
            agendaDetail.empty();
            var ul = $('<ul class="list-unstyled todo-list"></ul>');
            $.each(data['data']['result'], function (index, object) {
                var li = $('<li></li>');
                li.append($('<p>' +
                    '<span class="title">' + object['agendaName'] + '</span>' +
                    '<a href="javascript:void(0)" class="agenda-detail-button" '+
                    'id="' + object['agendaID'] + '" style="float: right">' +
                    '<i class="lnr lnr-pointer-right"></i>' +
                    '<span> Detail</span>' +
                    '</a>' +
                    '<span class="short-description">' + object['agendaDate'] + '</span>' +
                    '</p>'));
                ul.append(li);
            });
            agendaDetail.append(ul);
        },
        error: function () {

        }
    })
}

$('.agenda-list').on('click', '.agenda-detail-button', function () {
    var agendaID = $(this).attr('id');
    showAgendaDetail(agendaID);
});

function showAgendaDetail(agendaID){

    $.ajax({
        type:'post',
        url:'/admin/agenda/detail',
        dataType:'json',
        data:{
            agendaID:agendaID
        },
        cache:false,
        success:function (data) {
            var agendaDetail = $('.agenda-detail');
            agendaDetail.empty();
            var ul = $('<ul class="list-unstyled todo-list"></ul>');
            if (data['data'] != null){
                $.each(data['data']['event'], function (index, object) {
                    var eventID = object['eventID'];
                    var li = $('<li></li>');
                    var p = $('<p>');
                    p.append($('<span class="title">' + object['eventName'] + '</span>' +
                        '<span class="short-description">'
                        + '&nbsp;' + object['eventRoom'] + ' ' + object['eventStartTime'] +
                        ' - ' + object['eventEndTime'] +
                        '</span>'));
                    if (object['status'] == 1){
                        $.each(data['data']['session'], function (index, object) {
                            if (object['eventID'] == eventID){
                                p.append('<span class="short-description">' + '&nbsp;' +object['sessionName'] + '</span>');
                            }
                        })
                    }
                    li.append(p);
                    ul.append(li);
                });
                $('#add-event-to-agenda').append(
                    $('<button type="button" data-toggle="modal" data-target="#createAgendaModal">' +
                        '<i class="lnr lnr-plus-circle"></i>\n' +
                        '<span> New</span>' +
                        '</button>'));
                agendaDetail.append(ul);
            } else {
                agendaDetail.append($('<h4 class="panel-title">No Detail Found</h4>'));
            }
        },
        error:function () {
            notificationMessage("danger", "No event found");
        }
    })
}

$('.create-agenda').click(function () {
    createAgenda();
});

function createAgenda(){

    var agendaName = $('#agendaName').val();
    var agendaDate = $('#agendaDate').val();

    if (!agendaName){
        notificationMessage("danger", "title is required");
        return;
    }
    if (!agendaDate){
        notificationMessage("danger", "date is required");
        return;
    }

    $.ajax({
        type:'post',
        url:'/admin/agenda/create',
        dataType:'json',
        data:{
            agendaName:agendaName,
            agendaDate:agendaDate,
        },
        cache:false,
        success:function (data) {
            notificationMessage("success", "Create agenda success");
            var agendaDetail = $('.agenda-detail');
            agendaDetail.empty();
            var ul = $('<ul class="list-unstyled todo-list"></ul>');
            $.each(data['data']['result'], function (index, object) {
                var li = $('<li></li>');
                li.append($('<p>' +
                                '<a href="javascript:void(0)">' +
                                    '<span class="title">' + object['agendaName'] + '</span>' +
                                '</a>' +
                                '<div id="right">' +
                                    '<button type="button" class="btn-toggle-collapse">' +
                                        '<i class="lnr lnr-pointer-right"></i>' +
                                    '<span> Detail</span>' +
                                    '</button>' +
                                '</div>' +
                                '<span class="short-description">' + object['agendaDate'] + '</span>' +
                            '</p>'));
                ul.append(li);
            });
            agendaDetail.append(ul);
            $('#modal-close').click();
        },
        error:function () {
            alert("error");
        }
    })
}

function notificationMessage(status, message) {
    var button;
    if (status == "success"){
        button = document.getElementById("btn-success");
        button.setAttribute("data-message", message);
        button.click();
    } else if (status == "warning"){
        button = document.getElementById("btn-warning");
        button.setAttribute("data-message", message);
        button.click();
    } else if (status == "danger"){
        button = document.getElementById("btn-danger");
        button.setAttribute("data-message", message);
        button.click();
    }
}
$('#candidate').click(function () {
    $('#overview-div').empty();
    $('#conference-div').empty();
    $('#search-candidate-form').css("display", "block")
});
