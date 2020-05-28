$('#candidate').click(function () {
    $('.overview-data').empty();
    $('.conference-list').empty();
    $('#empty-conference-form').empty();
    $('#conference-detail').empty();
    $('#agenda-detail').empty();
    $('#selectedPaper').empty();
    $('#availablePaper').empty();
    $('#conference-detail').empty();
    $('#submission-list').empty();
    searchCandidateForm();
    $('#candidateSearchForm').click();
});

$('#conference-div').on('click', '#searchCandidate', function () {
    searchCandidate();
});

$('#candidate-div').on('click', '.show-candidate-submissions', function () {
    var userID = $(this).attr('id');
    getCandidateSubmissions(userID);
});

$('#candidate-div').on("click", ".submission-detail", function () {
    var submissionID = $(this).attr('id');
    submissionForm();
    submissionDetail(submissionID);
    $('#submissionDetail').click();
});

function searchCandidateForm() {
    var modal = $('#modal');
    modal.empty();
    $('#modal').append('<div class="modal fade" id="candidateSearchModal" tabindex="-1" role="dialog"' +
        ' aria-labelledby="candidateSearchLabel" aria-hidden="true">' +
        '<div class="modal-dialog" role="document">' +
        '<div class="modal-content">' +
        '<div class="modal-header">' +
        '<h3 class="modal-title" id="candidateSearchLabel">Search Candidates</h3>' +
        '</div>' +
        '<div class="modal-body">' +
        '<form>' +
        '<div class="form-group">' +
        '<label for="email" class="col-form-label">Email</label>' +
        '<input type="text" class="form-control" id="email">' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="cnName" class="col-form-label">Chinese Name</label>' +
        '<input type="text" class="form-control" id="cnName">' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="enName" class="col-form-label">English Name</label>' +
        '<input type="text" class="form-control" id="enName">' +
        '</div>' +
        '<div class="form-group">' +
        '<label for="phone" class="col-form-label">Phone Number</label>' +
        '<input type="text" class="form-control" id="phone">' +
        '</div>' +
        '<div class="modal-footer">' +
        '<button type="button" class="btn btn-secondary" data-dismiss="modal" >Close</button>' +
        '<button type="submit" class="btn btn-primary" data-dismiss="modal" id="searchCandidate">Search</button>' +
        '</div>' +
        '</div>' +
        '</div>' +
        '</div>')
}

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
            var searchResult = $('#candidate-search-result');
            searchResult.empty();
            searchResult.append('<div class="panel">' +
                '<div class="panel-heading">' +
                '<h3 class="panel-title">Search Results</h3>' +
                '</div>' +
                '<div class="panel-body no-padding search-result-detail">' +
                '<!-- Insert search results -->' +
                '</div>' +
                '<div class="panel-footer">' +
                '<div class="row">' +
                '<div class="col-md-6">' +
                '<span class="panel-note candidate-count">' +
                '</span>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '</div>');
            var candidateCount = $('.candidate-count');
            candidateCount.empty();
            if (data['status'] == 406) {
                candidateCount.append('Candidate found: 0');
            } else {
                var i = 0;
                var searchResultDetail = $('.search-result-detail');
                searchResultDetail.empty();
                var table = $('<table class="table"></table>');
                var thead;
                var object = data['data']['result'];
                if (object['submissionCount'] == 1){
                    thead = $('<thead>' +
                        '<tr>' +
                        '<th>Chinese Name</th><th>English Name</th><th>Email</th><th>Phone</th><th>Submission</th>' +
                        '</tr>' +
                        '</thead>');
                } else {
                    thead = $('<thead>' +
                        '<tr>' +
                        '<th style="text-align: center">Chinese Name</th>' +
                        '<th style="text-align: center">English Name</th>' +
                        '<th style="text-align: center">Email</th>' +
                        '<th style="text-align: center">Phone</th>' +
                        '<th style="text-align: center">Submission</th>' +
                        '</tr>' +
                        '</thead>');
                }
                var tbody = $('<tbody></tbody>');
                $.each(data['data']['result'], function (index, object) {
                    var tr = $('<tr></tr>');
                    tr.append($('<td style="text-align: center">' + object['ChineseName'] + '</td>' +
                        '<td style="text-align: center">' + object['EnglishName'] + '</td>' +
                        '<td style="text-align: center">' + object['email'] + '</td>'+
                        '<td style="text-align: center">' + object['phone'] + '</td>'+
                        '<td style="text-align: center"><a href="javascript:void(0)" class="show-candidate-submissions" id="' + object['userID'] + '">' + object['submissionCount'] + '</a></td>'));
                    tbody.append(tr);
                    i++;
                });
                table.append(thead);
                table.append(tbody);
                searchResultDetail.append(table);
                if (i == 1){
                    candidateCount.append('Candidate found: ' + i);
                } else {
                    candidateCount.append('Candidates found: ' + i);
                }
            }
        }
    })
}

function getCandidateSubmissions(userID) {
    $.ajax({
        type: 'post',
        url: '/submission/candidate',
        dataType: 'json',
        cache: false,
        data: {
            userID: userID,
        },
        success: function (data) {
            var candidateSubmissionList = $('#candidate-submissions-list');
            candidateSubmissionList.empty();
            candidateSubmissionList.append('<div class="panel">' +
                                            '<div class="panel-heading">' +
                                            '<h3 class="panel-title">Submissions</h3>' +
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
                    '</tr>' +
                    '</thead>');
                var tbody = $('<tbody></tbody>');
                $.each(data['data']['result'], function (index, object) {
                    var tr = $('<tr></tr>');
                    tr.append($('<td style="text-align: center">' +
                        '<a href="javascript:void(0)" class="submission-detail" id="' + object['submissionID'] + '">'
                        + object['title'] + '</a></td>' +
                        '<td style="text-align: center">' + object['commitTime'] + '</td>'));
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