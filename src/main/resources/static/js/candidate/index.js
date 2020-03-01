$('.create-submission').click(function () {
    createSubmission();
});

function createSubmission() {

    var formData = new FormData();

    var title = $('#title').val();
    var abstractText = $('#abstractText').val();
    var keyword = $('#keyword').val();
    var topic = "";
    $("input[name='topic']:checked").each(function () {
        topic += ($(this).val() + ";")
    });
    var paper = $('#paper')[0].files[0];
    var slide = $('#slide')[0].files[0];


    if (title == ""){
        notificationMessage("danger", "Title can not be empty");
        return;
    } else if (abstractText == ""){
        notificationMessage("danger", "Abstract can not be empty");
        return;
    } else if (keyword == ""){
        notificationMessage("danger", "Keyword can not be empty");
        return;
    } else if (topic == ""){
        notificationMessage("danger", "Topic can not be empty");
        return;
    } else if (paper == undefined){
        notificationMessage("danger", "Paper file can not be empty");
        return;
    } else if (slide == undefined){
        notificationMessage("danger", "Slide file can not be empty");
        return;
    }

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
        success:function () {
            notificationMessage("success", "Submit application successfully. Thank you")
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



