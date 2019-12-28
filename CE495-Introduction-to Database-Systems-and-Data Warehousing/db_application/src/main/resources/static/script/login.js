function login() {
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;

    $.ajax({
        url: '/login/' + username + '/' + password,
        datatype: 'json',
        type: 'post',
        async: true,
        success: function (data) {
            window.location.href = "http://localhost:19495/studentMenu.html?" + username;
        },
        error: function () {
            alert("Incorrect username or password");
        }
    });
}

