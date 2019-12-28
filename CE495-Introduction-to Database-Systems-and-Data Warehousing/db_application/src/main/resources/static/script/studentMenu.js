var username = window.location.search.substr(1);

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

function getCurrentCourseById() {
    $.ajax({
        url: "/currentCourse/" + username,
        datatype: 'json',
        type: 'get',
        async: true,
        success: function (data) {
            var courseNum = data.length;
            for (var i = 0; i !== courseNum; ++i) {
                var firstTH = document.createElement('th');
                firstTH.innerText = data[i]["uoSCode"];
                var secondTH = document.createElement('th');
                secondTH.innerText = data[i]["uoSName"];
                var thirdTH = document.createElement('th');
                thirdTH.innerText = data[i]["semester"];
                var fourthTH = document.createElement('th');
                fourthTH.innerText = data[i]["year"];
                var tr = document.createElement('tr');
                tr.insertAdjacentElement("beforeend", firstTH);
                tr.insertAdjacentElement("beforeend", secondTH);
                tr.insertAdjacentElement("beforeend", thirdTH);
                tr.insertAdjacentElement("beforeend", fourthTH);
                document.getElementById("currentCourseTable").insertAdjacentElement("beforeend", tr);
            }
        },
        error: function (data) {
            alert("Server is busy.");
        }
    });
}

getCurrentCourseById();
changeHref();