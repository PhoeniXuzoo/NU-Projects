var username = window.location.search.substr(1);
var address = "";
var name = "";

function changeHref() {
    var href = document.getElementById("goDetails").href + "?" + username;
    document.getElementById("goDetails").setAttribute("href", href);
    href = document.getElementById("goWithdraw").href + "?" + username;
    document.getElementById("goWithdraw").setAttribute("href", href);
    href = document.getElementById("goEnroll").href + "?" + username;
    document.getElementById("goEnroll").setAttribute("href", href);
    href = document.getElementById("goTranscript").href + "?" + username;
    document.getElementById("goTranscript").setAttribute("href", href);
    href = document.getElementById("goStudentMenu").href + "?" + username;
    document.getElementById("goStudentMenu").setAttribute("href", href);
}

function findById() {
    $.ajax({
        url: '/personalDetails/' + username,
        datatype: 'json',
        type: 'get',
        async: true,
        success: function (data) {
            //alert(JSON.stringify(data));
            document.getElementById("name").innerText = data['name'];
            document.getElementById("ID").innerText = data['id'];
            if (data['address'] == null)
                document.getElementById("address").innerText = "No Address Info";
            else
                document.getElementById("address").innerText = data['address'];
        },
        error: function () {
            alert("Server is busy, Please try later.");
        }
    });
}

function submitNewAdress() {
    var newAddress = document.getElementById("newAddress").value;

    if (newAddress === "") newAddress = null;

    $.ajax({
        url: '/personalDetails/updateAddress/' + username,
        datatype: 'json',
        type: 'post',
        contentType: "application/json",
        async: true,
        data: JSON.stringify({
            newAddress: newAddress
        }),
        success: function (data) {
            document.getElementById("address").innerText = newAddress;
            document.getElementById("newAddress").value = "";
            $('#ChangeAddressModal').modal('hide');
        },
        error: function () {
            alert("Server is busy, Please try later.");
        }
    });
}

function subbitNewPassword() {
    var newPassword = document.getElementById("newPassword").value;
    var repeatPassword = document.getElementById("repeatPassword").value;

    /*if (newPassword === "") {
        alert("New password cannot be empty.");
        return ;
    }*/

    if (newPassword !== repeatPassword) {
        alert("Repeat new password does not match.");
    }
    else {
        $.ajax({
            url: '/personalDetails/updatePassword/' + username + '/' + newPassword,
            datatype: 'json',
            type: 'post',
            async: true,
            success: function (data) {
                document.getElementById("newPassword").value = "";
                document.getElementById("repeatPassword").value = "";
                $('#ChangePasswordModal').modal('hide');
                alert("Success.");
            },
            error: function () {
                alert("Fail to change password");
            }
        });
    }
}

changeHref();
findById();