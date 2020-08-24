function getSubmissionInfoListInConference(conferenceID) {
    $.ajax({
        type: 'post',
        url: '/submission/submission-info-in-conference',
        dataType: 'json',
        data: {
            conferenceID: conferenceID,
        },
        cache:false,
        success(data){
            var submissionList = $('#submission-list');
            submissionList.empty();
            submissionList.append('<div class="panel">' +
                '<div class="panel-heading">' +
                '<h3 class="panel-title">Submissions</h3>' +
                '</div>' +
                '<div class="panel-body">' +
                '<table class="table">' +
                '<thead>' +
                '<tr>' +
                '<th>Title of paper</th>' +
                '<th>Submitter by</th>' +
                '<th>Commit time</th>' +
                '</tr>' +
                '</thead>' +
                '<tbody class="submission-list-detail">' +
                '</tbody>' +
                '</table>' +
                '</div>' +
                '</div>');
            var submissionListDetail = $('.submission-list-detail');
            $.each(data['data']['result'], function (index, object) {
                var tr = $('<tr></tr>');
                tr.append($('<td><a href="javascript:void(0)" class="submission-detail" id="' + object['submissionID'] + '">' 
                    + object['title'] + '</a></td>' +
                    '<td>' + object['submitter'] + '</td>' +
                    '<td>' + object['commitTime'] + '</td>'));
                submissionListDetail.append(tr);
            });
        }
    });
}

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
        url:'/submission/topic',
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

var conferenceDiv = $('#conference-div');

conferenceDiv.on("click", ".submission-detail", function () {
    var submissionID = $(this).attr('id');
    submissionForm();
    submissionDetail(submissionID);
    $('#submissionDetail').click();
});

conferenceDiv.on("click", ".submission-info-list", function () {
    $('#agenda-detail').empty();
    $('#selectedPaper').empty();
    $('#availablePaper').empty();
    var conferenceID = $(this).attr('id');
    getSubmissionInfoListInConference(conferenceID);
});
