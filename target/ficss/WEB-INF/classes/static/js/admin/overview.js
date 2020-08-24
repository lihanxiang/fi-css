$('#overview').click(function () {
    $('.overview-data').empty();
    $('.conference-list').empty();
    $('#empty-conference-form').empty();
    $('#conference-detail').empty();
    $('#agenda-detail').empty();
    $('#selectedPaper').empty();
    $('#availablePaper').empty();
    $('#conference-detail').empty();
    $('#submission-list').empty();
    $('#candidate-search-result').empty();
    $('#candidate-submissions-list').empty();

    $('.overview-data').append('<div class="panel panel-headline">' +
                                '<div class="panel-heading">' +
                                    '<h3 class="panel-title">Overview</h3>' +
                                '</div>' +
                                '<div class="panel-body">' +
                                    '<div class="row">' +
                                        '<div class="col-md-4">' +
                                            '<div class="metric">' +
                                                '<p>' +
                                                    '<span class="number" id="conferenceCount"></span>' +
                                                    '<span class="title" id="conferenceCountTitle">Conferences</span>' +
                                                '</p>' +
                                            '</div>' +
                                        '</div>' +
                                        '<div class="col-md-4">' +
                                            '<div class="metric">' +
                                                '<p>' +
                                                    '<span class="number" id="submissionCount"></span>' +
                                                    '<span class="title" id="submissionCountTitle">Submissions</span>' +
                                                '</p>' +
                                            '</div>' +
                                        '</div>' +
                                        '<div class="col-md-4">' +
                                            '<div class="metric">' +
                                                '<p>' +
                                                    '<span class="number" id="candidateCount"></span>' +
                                                    '<span class="title" id="candidateCountTitle">Candidates</span>' +
                                                '</p>' +
                                            '</div>' +
                                        '</div>' +
                                    '</div>' +
                                '</div>' +
                            '</div>');
    getOverviewData();
});

function getOverviewData() {

    $.ajax({
        type: 'get',
        url: '/overview/get',
        dataType: 'json',
        cache: false,
        success: function (data) {
            var object = data['data']['result'];
            if (object['conferenceCount'] < 2){
                $('#conferenceCountTitle').empty();
                $('#conferenceCountTitle').append("Conference");
            }
            if (object['submissionCount'] < 2){
                $('#submissionCountTitle').empty();
                $('#submissionCountTitle').append("Submission");
            }
            if (object['candidateCount'] < 2){
                $('#candidateCountTitle').empty();
                $('#candidateCountTitle').append("Candidate");
            }
            $('#conferenceCount').append(object['conferenceCount']);
            $('#submissionCount').append(object['submissionCount']);
            $('#candidateCount').append(object['candidateCount']);
        }
    })
}