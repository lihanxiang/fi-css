$('.search-candidate').click(function () {
    searchCandidate();
});

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
            var i = 0;
            var searchResult = $('.search-result');
            searchResult.empty();
            var table = $('<table class="table table-striped"></table>');
            var thead = $('<thead>' +
                '<tr>' +
                '<th>Chinese Name</th><th>English Name</th><th>Email</th><th>Phone</th>' +
                '</tr>' +
                '</thead>');
            var tbody = $('<tbody></tbody>');
            $.each(data['data']['result'], function (index, object) {
                var tr = $('<tr></tr>');
                tr.append($('<td>' + object['ChineseName'] + '</td>' +
                    '<td>' + object['EnglishName'] + '</td>' +
                    '<td>' + object['email'] + '</td>'+
                    '<td>' + object['email'] + '</td>'));
                tbody.append(tr);
                i++;
            });
            table.append(thead);
            table.append(tbody);
            searchResult.append(table);
            var resultNum = $('.result-num');
            resultNum.empty();
            resultNum.append('Total: ' + i);
        },
        error:function () {

        }
    })
}