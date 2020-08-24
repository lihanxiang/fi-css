$('.test').click(function () {
    candidates();
});

function candidates() {
    $.ajax({
        type:'get',
        url:'/user/candidates',
        dataType:'json',
        data:{

        },
        success:function (data) {
            var candidateContent = $('.candidateContent');
            candidateContent.empty();
            var candidate = $('<div></div>');
            $.each(data['data']['result'], function (index, object) {
                candidate.append($('<p>email ' + object['email'] + '</p>'));
                candidate.append($('<p>Chinese Name ' + object['ChineseName'] + '</p>'));
                candidate.append($('<p>English Name ' + object['EnglishName'] + '</p>'));
            });
            candidateContent.append(candidate);
        },

        error : function() {

        }
    })
}