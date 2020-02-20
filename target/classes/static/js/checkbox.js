function submit() {
    var checkOne = false;
    var checkBox = $('input[name = "topic"]');

    for (var i = 0; i < checkBox.length; i++) {
        if(checkBox[i].prop('checked')){
            checkOne = true;
        }
    }

    if (!checkOne) {
        alert("You must select a topic related to your paper");
    }
}