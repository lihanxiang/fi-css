$(document).ready(function () {
    initializePage();
});

function initializePage() {
    getValidConferences();
    getSubmissionSnapshot();
}

function getValidConferences(){

    $.ajax({
        type:'get',
        url:'/conference/candidate/show',
        dataType:'json',
        cache:false,
        success:function (data) {
            var conferenceList = $('.conference-list');
            conferenceList.empty();
            var ul = $('<ul class="list-unstyled todo-list"></ul>');
            if (data['status'] == 400){
                ul.append('<li><p><span class="title">No Conference found</span></p></li>');
            } else {
                $.each(data['data']['result'], function (index, object) {
                    var li = $('<li></li>');
                    var p = $('<p></p>');
                    p.append('<span class="title">' + object['conferenceName'] + '</span>');
                    if (object['submissionID'] != null){
                        p.append('<a href="javascript:void(0)" class="my-submission-button" ' +
                            'id="' + object['submissionID'] + '" style="float: right">' +
                            '<i class="lnr lnr lnr-checkmark-circle"></i>' +
                            '<span> My submission</span>' +
                            '</a>');
                    } else {
                        p.append('<a href="javascript:void(0)" class="new-submission-button" ' +
                                    'id="' + object['conferenceID'] + '" style="float: right">' +
                                        '<i class="lnr lnr-upload"></i>' +
                                        '<span> Submit my paper</span>' +
                                    '</a>');
                    }

                    if (object['firstDay'] == object['lastDay']){
                        p.append('<span class="short-description">' + object['firstDay'] + '</span>');
                    } else {
                        p.append('<span class="short-description">From '+ object['firstDay'] + ' To ' + object['lastDay'] + '</span>');
                    }
                    li.append(p);
                    ul.append(li);
                });
            }
            conferenceList.append(ul);
        },
        error:function (data) {
            notificationMessage("waring", data['message'])
        }
    })
}

function getSubmissionSnapshot(){

    $.ajax({
        type:'get',
        url:'/candidate/submission-snapshot',
        dataType:'json',
        cache:false,
        success:function (data) {
            var submissionSnapshot = $('.submission-snapshot');
            submissionSnapshot.empty();
            var ul = $('<ul class="list-unstyled todo-list "></ul>');
            if (data['status'] == 404){
                ul.append('<li><p><span class="title">No Submission found</span></p></li>');
            } else {
                $.each(data['data']['result'], function (index, object) {
                    var li = $('<li></li>');
                    var p = $('<p></p>');
                    p.append('<span class="title">' + object['title'] + '</span>');
                    p.append('<span class="short-description">' + 'Last Modified: ' + object['lastModified'] + '</span>');
                    li.append(p);
                    ul.append(li);
                });
            }
            submissionSnapshot.append(ul);
        },
        error:function () {
            notificationMessage("waring", "No submission yet")
        }
    })
}


/*$('.create-submission').click(function () {
    createSubmission();
});


function createSubmission() {

    var formData = new FormData();

    var conferenceID = $('#conference-id').val();
    var title = $('#title').val();
    var abstractText = $('#abstractText').val();
    var keyword = $('#keyword').val();
    var topic = "";
    $("input[name='topic']:checked").each(function () {
        topic += ($(this).val() + ";")
    });
    var paper = $('#paper')[0].files[0];
    var slide = $('#slide')[0].files[0];


    if (!title) {
        notificationMessage("danger", "Title can not be empty");
        return;
    }
    if (!abstractText){
        notificationMessage("danger", "Abstract can not be empty");
        return;
    }
    if (!keyword){
        notificationMessage("danger", "Keyword can not be empty");
        return;
    }
    if (!topic){
        notificationMessage("danger", "Topic can not be empty");
        return;
    }
    if (paper == undefined){
        notificationMessage("danger", "Paper file can not be empty");
        return;
    }
    if (slide == undefined){
        notificationMessage("danger", "Slide file can not be empty");
        return;
    }

    formData.append("conferenceID", conferenceID);
    formData.append("title",title);
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
                notificationMessage("danger", data['message']);
            } else {
                notificationMessage("success", "Submit application successfully. Thank you");
                $('#submission-form').empty();
                $('#container').load('index');
            }
        },
        error:function () {
            alert("error");
        }
    })
}*/


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



