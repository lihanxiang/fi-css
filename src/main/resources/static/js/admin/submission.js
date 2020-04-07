function getSubmissionListInConference(conferenceID) {
    $.ajax({
        type: 'post',
        url: '/submission/submission-detail-in-conference',
        dataType: 'json',
        data: {
            conferenceID: conferenceID,
        },
        cache:false,
        success(data){
            $('#session-detail').empty();
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
                '<th>Last modified</th>' +
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
                tr.append($('<td>' + object['title'] + '</td>' +
                    '<td>' + object['submitter'] + '</td>' +
                    '<td>' + object['lastModified'] + '</td>'));
                submissionListDetail.append(tr);
            });
        }
    });
}

var conferenceDiv = $('#conference-div');


conferenceDiv.on("click", ".submission-detail", function () {
    var conferenceID = $(this).attr('id');
    getSubmissionListInConference(conferenceID);
});
