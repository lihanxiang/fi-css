$('.conference-list').on('click', '.new-submission-button', function () {
    var conferenceID = $(this).attr('id');
    $('#index').empty();
    getEmptySubmissionForm();
    getTopicsInConference(conferenceID);
    $('#conference-id').attr("value", conferenceID);
});

$('.conference-list').on('click', '.my-submission-button', function () {
    var submissionID = $(this).attr('id');
    $('#index').empty();
    getEmptySubmissionForm();
    getMySubmission(submissionID);
});

function getMySubmission(submissionID){

    $.ajax({
        type:'post',
        url:'/submission/detail',
        dataType:'json',
        data:{
            submissionID:submissionID
        },
        cache:false,
        success:function (data) {
            var object = data['data']['result'];
            $('#title').attr('value', object['title']);
            $('#abstractText').append(object['abstractText']);
            $('#keyword').attr('value', object['keyword']);
            getMyTopicsInSubmission(object['conferenceID'], object['topic']);
            $('#paper').fileinput({
                theme: 'fas',
                showUpload: false,
                showCaption: false,
                fileType: "any",
                previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
                overwriteInitial: false,
                initialPreviewAsData: true,
                initialPreview: [
                    "C:\\Users\\94545\\Desktop\\Papers\\11111111111\\lihanxiang.pdf"
                ]
            });
            $('#slide').fileinput({
                'theme': 'explorer-fas',
                overwriteInitial: false,
                initialPreviewAsData: true,
                initialPreview:object['slideFilePath'],
                initialPreviewConfig: {url: "{$url}"},
            });
        },
        error:function () {
            notificationMessage("danger","Sorry, loading you submission detail failed")
        }
    })
}

function getMyTopicsInSubmission(conferenceID, topics) {
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
        },
        error:function () {

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
                        '<input type="checkbox" value="' + object['topicName'] + '" name="topic" checked="checked">' +
                        '<span>' + object['topicName'] + '</span>' +
                        '</label>')
                } else {
                    topic.append('<label class="fancy-checkbox">' +
                        '<input type="checkbox" value="' + object['topicName'] + '" name="topic" >' +
                        '<span>' + object['topicName'] + '</span>' +
                        '</label>')

                }
            });
        },
        error:function () {

        }
    })
}

function getEmptySubmissionForm(){
    $('#submission-form').css("display", "block");
    /*$('#submission-form').append('<div class="panel">' +
                                    '<div class="panel-heading">' +
                                        '<h2 class="panel-title">new Submission</h2>' +
                                    '</div>' +
                                    '<div class="panel-body">' +
                                        '<div class="col-md-4">' +
                                            '<div class="form-group" style="display: none">' +
                                                '<input type="hidden" name="conferenceID" id="conference-id">' +
                                            '</div>' +
                                            '<div class="form-group">' +
                                                '<label for="title">Title</label><br>' +
                                                '<div class="alert alert-info alert-dismissible" role="alert">' +
                                                '<button type="button" class="close" data-dismiss="alert" ' +
                                                    'aria-label="Close">' +
                                                    '<span aria-hidden="true">&times</span>' +
                                                '</button>' +
                                                '<i class="fa fa-info-circle"></i> Title of you Paper' +
                                            '</div>' +
                                            '<input type="text" class="form-control" id="title" placeholder="Enter"><br>' +
                                        '</div>' +
                                        '<div class="form-group">' +
                                            '<label for="abstractText">Abstract</label>' +
                                            '<div class="alert alert-info alert-dismissible" role="alert">' +
                                                '<button type="button" class="close" data-dismiss="alert" ' +
                                                    'aria-label="Close">' +
                                                    '<span aria-hidden="true">&times</span>' +
                                                '</button>' +
                                                '<i class="fa fa-info-circle"></i> Abstract of your paper' +
                                            '</div>' +
                                            '<textarea class="form-control" id="abstractText" style="resize: none"' +
                                            '  rows="8"></textarea><br>' +
                                        '</div>' +
                                        '<div class="form-group">' +
                                            '<label for="keyword">Keyword</label>' +
                                            '<div class="alert alert-info alert-dismissible" role="alert">' +
                                                '<button type="button" class="close" data-dismiss="alert" ' +
                                                'aria-label="Close">' +
                                                '<span aria-hidden="true">&times</span>' +
                                            '</button>' +
                                                '<i class="fa fa-info-circle"></i> Specify <b>at least three</b> keywords,' +
                                                'which are separated by semicolons.' +
                                            '</div>' +
                                            '<input type="text" class="form-control" id="keyword" placeholder="Enter"><br>' +
                                        '</div>' +
                                        '<div class="form-group">' +
                                            '<h5>Topics</h5>' +
                                            '<div class="alert alert-info alert-dismissible" role="alert">' +
                                                '<button type="button" class="close" data-dismiss="alert" ' +
                                                'aria-label="Close">' +
                                                '<span aria-hidden="true">&times</span>' +
                                            '</button>' +
                                                '<i class="fa fa-info-circle"></i> Select <b>at least one</b> topic relevant to' +
                                                'your submission from the following list' +
                                            '</div>' +
                                            '<div class="form-check" id="topic"></div>' +
                                        '</div><br>' +
                                    '</div>' +
                                    '<div class="col-md-4">' +
                                        '<div class="form-group">' +
                                            '<h5>Paper</h5>' +
                                            '<h6>The paper MUST be in PDF and the file name MUST be your English name, e.g.,' +
                                            'TaimanChan.pdf</h6><br>' +
                                            '<div class="file-loading">' +
                                                '<input id="paper" class="file" type="file" data-theme="fas"' +
                                                '   name="paper" data-browse-on-zone-click="true">' +
                                            '</div>' +
                                        '</div><br>' +
                                        '<div class="form-group">' +
                                            '<h5>Slide</h5>' +
                                            '<h6>The slides file MUST be in PPT or PDF and the file name MUST be your' +
                                            'English name, e.g., TaimanChan.pptx.</h6><br>' +
                                            '<div class="file-loading">' +
                                                '<input id="slide" class="file" type="file" data-theme="fas"' +
                                                '   name="slide" data-browse-on-zone-click="true">' +
                                            '</div>' +
                                        '</div><br>' +
                                        '<button type="submit" style="float:right" ' +
                                            'class="btn btn-primary create-submission" >' +
                                            'Submit' +
                                        '</button>' +
                                        '</div>' +
                                            '<div id="toastr" style="display: none">' +
                                            '<button type="button" id="btn-success" class="btn btn-success btn-toastr" ' +
                                                'data-context="success" data-message="" data-position="top-right">Success</button>' +
                                            '<button type="button" id="btn-warning" class="btn btn-warning btn-toastr" ' +
                                                'data-context="warning" data-message="" data-position="top-right">Warning</button>' +
                                            '<button type="button" id="btn-danger" class="btn btn-danger btn-toastr" ' +
                                                    'data-context="error" data-message="" data-position="top-right">Danger</button>' +
                                        '</div>');

     */
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


    if (title == ""){
        notificationMessage("danger","Title can not be empty");
        return;
    } else if (abstractText == ""){
        notificationMessage("danger","Abstract can not be empty");
        return;
    } else if (keyword == ""){
        notificationMessage("danger","Keyword can not be empty");
        return;
    } else if (topic == ""){
        notificationMessage("danger","Topic can not be empty");
        return;
    } else if (paper == undefined){
        notificationMessage("danger","Paper file can not be empty");
        return;
    } else if (slide == undefined){
        notificationMessage("danger","Slide file can not be empty");
        return;
    }

    formData.append("conferenceID", conferenceID);
    formData.append("title",title);
    formData.append("author",author);
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
                notificationMessage("success", "Submit application successfully. Thank you");
                $('#submission-form').empty();
                window.location.reload()
            }
        },
        error:function (data) {
            notificationMessage("danger", data['message'])
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


