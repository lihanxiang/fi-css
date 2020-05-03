function submissionForm() {
    var modal = $('#modal');
    modal.empty();
    $('#modal').append('<div class="modal fade" id="submissionDetailModal" tabindex="-1" role="dialog"' +
        ' aria-labelledby="submissionDetailLabel" aria-hidden="true">' +
        '<div class="modal-dialog" role="document">' +
        '<div class="modal-content">' +
        '<div class="modal-header">' +
        '<h3 class="modal-title" id="submissionDetailLabel" >Submission Detail</h3>' +
        '</div>' +
        '<div class="modal-body">' +
        '<form>' +
        '<div class="form-group">' +
        '<input type="hidden" class="form-control" id="submissionID">' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="title" class="col-form-label">Title:</label>' +
        '<input type="text" class="form-control" id="title" disabled>' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="author" class="col-form-label">Author:</label>' +
        '<input type="text" class="form-control" id="author" disabled>' +
        '</div>' +
        '<div class="form-group" style="word-wrap:break-word; word-break:break-all;">' +
        '<label for="abstractText" class="col-form-label">Abstract:</label>' +
        '<input type="text" class="form-control" id="abstractText" disabled>' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="keyword" class="col-form-label">Keyword:</label>' +
        '<input type="text" class="form-control" id="keyword" disabled>' +
        '</div>' +
        '<div class="form-group">' +
        '<h5>Topics</h5>' +
        '<div class="form-check" id="topic" disabled></div>' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="email" class="col-form-label">Email:</label>' +
        '<input type="email" class="form-control" id="email" disabled>' +
        '</div>' +
        '<div class="form-group">' +
        '<p>Paper: </p>' +
        '<a id="paper"></a>' +
        '</div>' +
        '<div class="form-group">' +
        '<p>Slide: </p>' +
        '<a id="slide"></a>' +
        '</div>' +
        '<div class="form-group">' +
        '<p id="commitTime"></p>' +
        '</div>' +
        '<div class="form-group">' +
        '<p id="lastModified"></p>' +
        '</div>' +
        '</form>' +
        '</div>' +
        '<div class="modal-footer">' +
        '<button type="button" class="btn btn-secondary" data-dismiss="modal" >Close</button>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>')
}

function submissionDetail(submissionID) {
    $.ajax({
        type: 'post',
        url: '/submission/detail',
        dataType: 'json',
        data: {
            submissionID: submissionID,
        },
        cache: false,
        success(data) {
            var object = data['data']['result'];
            $('#submissionID').attr('value', object['submissionID']);
            $('#title').attr('value', object['title']);
            $('#author').attr('value', object['author']);
            $('#abstractText').attr('value', object['abstractText']);
            $('#keyword').attr('value', object['keyword']);
            getTopicsInSubmission(object['conferenceID'], object['topic']);
            $('#email').attr('value', object['email']);
            $('#paper').append(object['paperTitle']);
            $('#paper').attr('href', "/paper/download/" + object['paperFileID']);
            $('#slide').append(object['slideTitle']);
            $('#slide').attr('href', "/slide/download/" + object['slideFileID']);
            $('#commitTime').append("Commit: " + object['commitTime']);
            $('#lastModified').append("Last Modified: " + object['lastModified']);
        }
    })
}

function getTopicsInSubmission(conferenceID, topics) {
    var topic = $('#topic');
    topic.empty();

    $.ajax({
        type:'post',
        url:'/candidate/topic',
        dataType:'json',
        cache:false,
        data:{
            conferenceID:conferenceID
        },
        success:function (data) {
            $.each(data['data']['result'], function (index, object) {
                topic.append('<label class="fancy-checkbox">' +
                                '<input type="checkbox" value="' + object['topicName'] + '" name="topic" >' +
                                '<span>' + object['topicName'] + '</span>' +
                            '</label>')

            });
            var topicBox = $("input:checkbox[name='topic']");
            var topicArray = topics.split(';');
            for (var i = 0; i < topicBox.length; i++){
                for (var j = 0; j < topicArray.length; j++){
                    if (topicBox[i].value == topicArray[j]){
                        topicBox[i].checked = true;
                        break;
                    }
                }
            }
        }
    })
}

function getTopicsInConference(conferenceID){

    var topic = $('#topic');
    topic.empty();

    $.ajax({
        type:'post',
        url:'/candidate/topic',
        dataType:'json',
        cache:false,
        data:{
            conferenceID:conferenceID
        },
        success:function (data) {
            $.each(data['data']['result'], function (index, object) {
                if (index == 0){
                    topic.append('<label class="fancy-checkbox">' +
                        '<input type="checkbox" class="input" value="' + object['topicName'] + '" name="topic" checked="checked">' +
                        '<span>' + object['topicName'] + '</span>' +
                        '</label>')
                } else {
                    topic.append('<label class="fancy-checkbox">' +
                        '<input type="checkbox" class="input" value="' + object['topicName'] + '" name="topic" >' +
                        '<span>' + object['topicName'] + '</span>' +
                        '</label>')

                }
            });
        }
    })
}

function getEmptySubmissionForm(){
    $('.input').val("");
    $('.fileinput-remove-button').click();
    $('#submission-form').css("display", "block");
    $('#submission-warning').click();
}

$('.create-submission').click(function () {
    createSubmission();
});

function createSubmission() {

    var formData = new FormData();

    var conferenceID = $('#conference-id').val();
    var title = $('#title').val();
    var author = $('#author').val();
    var abstractText = $('#abstractText').val();
    var keyword = $('#keyword').val();
    var topic = "";
    $("input[name='topic']:checked").each(function () {
        topic += ($(this).val() + ";")
    });
    var paper = $('#paper')[0].files[0];
    var slide = $('#slide')[0].files[0];

    if (title === ""){
        $('#title-cannot-be-empty').click();
        //$('#conference').click();
        return;
    } else if (author === "") {
        $('#author-cannot-be-empty').click();
        return;
    } else if (abstractText === "") {
        $('#abstract-cannot-be-empty').click();
        return;
    } else if (keyword === "") {
        $('#keyword-cannot-be-empty').click();
        return;
    } else if (topic === "") {
        $('#topic-cannot-be-empty').click();
        return;
    } else if (paper === undefined) {
        $('#paper-cannot-be-empty').click();
        return;
    } else if (slide === undefined) {
        $('#abstract-cannot-be-empty').click();
        return;
    }

    formData.append("conferenceID", conferenceID);
    formData.append("title", title);
    formData.append("author", author);
    formData.append("abstractText", abstractText);
    formData.append("keyword", keyword);
    formData.append("topic", topic);
    formData.append("paper", paper);
    formData.append("slide", slide);

    $.ajax({
        type:'post',
        url:'/submission/create',
        dataType:'json',
        data:formData,
        cache:false,
        async:false,
        contentType: false,
        processData: false,
        success:function (data) {
            if (data['status'] == 502){
                notificationMessage("danger",data['message']);
            } else {
                notificationMessage("success", "Success");
                $('#conference').click();
            }
        },
        error:function () {
            notificationMessage("danger", "error");
        }
    })
}

function mySubmission() {
    $.ajax({
        type: 'post',
        url: '/submission/my',
        dataType: 'json',
        cache: false,
        data: {},
        success: function (data) {
            var mySubmissionList = $('#my-submissions-list');
            mySubmissionList.empty();
            mySubmissionList.append('<div class="panel">' +
                '<div class="panel-heading">' +
                '<h3 class="panel-title">My Submissions</h3>' +
                '</div>' +
                '<div class="panel-body no-padding candidate-submissions-detail">' +

                '</div>' +
                '<div class="panel-footer">' +
                '<div class="row">' +
                '<div class="col-md-6">' +
                '<span class="panel-note submission-count">' +
                '</span>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>');
            var submissionCount = $('.submission-count');
            submissionCount.empty();
            if (data['status'] == 404) {
                submissionCount.append('Submission found: 0');
            } else {
                var i = 0;
                var searchResultDetail = $('.candidate-submissions-detail');
                searchResultDetail.empty();
                var table = $('<table class="table"></table>');
                var thead = $('<thead>' +
                    '<tr>' +
                    '<th style="text-align: center">Submission Title</th>' +
                    '<th style="text-align: center">Commit Time</th>' +
                    '<th style="text-align: center">Last Modified</th>' +
                    '</tr>' +
                    '</thead>');
                var tbody = $('<tbody></tbody>');
                $.each(data['data']['result'], function (index, object) {
                    var tr = $('<tr></tr>');
                    tr.append($('<td style="text-align: center">' +
                        '<a href="javascript:void(0)" class="submission-detail" id="' + object['submissionID'] + '">'
                        + object['title'] + '</a></td>' +
                        '<td style="text-align: center">' + object['commitTime'] + '</td>' +
                        '<td style="text-align: center">' + object['lastModified'] + '</td>'));
                    tbody.append(tr);
                    i++;
                });
                table.append(thead);
                table.append(tbody);
                searchResultDetail.append(table);
                if (i == 1){
                    submissionCount.append('Submission found: ' + i);
                } else {
                    submissionCount.append('Submissions found: ' + i);
                }
            }
        }
    })
}

$('#submission').click(function () {
    $('#submission-form').css("display", "none");
    $('.conference-list').empty();
    $('#conference-detail').empty();
    $('#agenda-detail').empty();
    mySubmission()
});

$('#my-submission').on("click", ".submission-detail", function () {
    var submissionID = $(this).attr('id');
    submissionForm();
    submissionDetail(submissionID);
    $('#submissionDetail').click();
});

$('#index').on('click', '.new-submission-button', function () {
    var conferenceID = $(this).attr('id');
    $('.conference-list').empty();
    $('#conference-detail').empty();
    $('#agenda-detail').empty();
    getEmptySubmissionForm();
    getTopicsInConference(conferenceID);
    $('#conference-id').attr("value", conferenceID);
});

$('#index').on('click', '.submission-detail', function () {
    var submissionID = $(this).attr('id');
    submissionForm();
    submissionDetail(submissionID);
    $('#submissionDetail').click();
});

$('#index').on("click", ".conference-detail", function () {
    var conferenceID = $(this).attr('id');
    $('#submission-form').css("display", "none");
    getConferenceDetail(conferenceID);
});

function notificationMessage(status, message) {
    var button;
    if (status == "success"){
        button = $('.btn-success');
        button.attr("data-message", message);
        button.click();
    } else if (status == "warning"){
        button = $('.btn-warning');
        button.attr("data-message", message);
        button.click();
    } else if (status == "danger"){
        button = $('.btn-danger');
        button.attr("data-message", message);
        button.click();
    }
}


