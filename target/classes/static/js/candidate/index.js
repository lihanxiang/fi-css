$('.create-submission').click(function () {
    createSubmission();
});

function createSubmission() {

    var formData = new FormData($('#form'));

    $.ajax({
        type:'post',
        url:'/submission/create',
        dataType:'json',
        data:formData,
        cache:false,
        async:false,
        success:function (data) {
            alert("success");
        },
        error:function () {
            alert("error")
        }
    })
}