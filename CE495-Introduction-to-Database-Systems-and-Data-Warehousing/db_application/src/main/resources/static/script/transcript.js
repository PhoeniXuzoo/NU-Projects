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
    document.getElementById("tableTitle").innerText = username + "'s transcript";
}

function getTranscriptById() {
    $.ajax({
        url: '/transcript/' + username,
        datatype: 'json',
        type: 'get',
        async: true,
        success: function (data) {
            var gradeNum = data.length;
            for (var i = 0; i !== gradeNum; ++i) {
                var tr = document.createElement('tr');
                var FirstTh = document.createElement('th');
                FirstTh.innerText = data[i]['uoSCode'];
                var SecondTh = document.createElement('th');
                SecondTh.innerText = data[i]['uoSName'];
                var ThirdTh = document.createElement('th');
                ThirdTh.innerText = data[i]['semester'];
                var FourthTh = document.createElement('th');
                FourthTh.innerText = data[i]['year'];
                var FifthTh = document.createElement('th');
                if (data[i]['grade'] === null)
                    FifthTh.innerText = "No data";
                else FifthTh.innerText = data[i]['grade'];
                tr.insertAdjacentElement('beforeend', FirstTh);
                tr.insertAdjacentElement('beforeend', SecondTh);
                tr.insertAdjacentElement('beforeend', ThirdTh);
                tr.insertAdjacentElement('beforeend', FourthTh);
                tr.insertAdjacentElement('beforeend', FifthTh);
                document.getElementById("transcriptTable").insertAdjacentElement('beforeend', tr);
            }
        },
        error: function () {
            alert("Server is busy, Please try later.");
        }
    });
}

function courseDetails() {
    var uoSCode = document.getElementById("UoSCode").value;
    var courseSemester = document.getElementById("courseSemester").value;
    var courseYear = document.getElementById("courseYear").value;

    if (courseSemester === "Q1" || courseSemester === "Q2") {

        $.ajax({
            url: '/transcript/courseDetails/' + username + '/' + uoSCode + '/' + courseSemester + '/' + courseYear,
            datatype: 'json',
            type: 'get',
            async: true,
            success: function (data) {
                if (data["uoSCode"] !== null) {
                    document.getElementById("USCode").innerText = data["uoSCode"];
                    document.getElementById("Title").innerText = data["uosname"];
                    document.getElementById("Year").innerText = data["year"];
                    document.getElementById("Quarter").innerText = data["semester"];

                    if (data["enrollment"] === null)
                        document.getElementById("Nofes").innerText = "No data";
                    else
                        document.getElementById("Nofes").innerText = data["enrollment"];

                    if (data["maxEnrollment"] === null)
                        document.getElementById("maxEn").innerText = "No data";
                    else
                        document.getElementById("maxEn").innerText = data["maxEnrollment"];

                    document.getElementById("Lecturer").innerText = data["lecturerName"];

                    if (data["grade"] === null)
                        document.getElementById("grade").innerText = "No data";
                    else
                        document.getElementById("grade").innerText = data["grade"];

                    $("#courseDetailsModal").modal('show');
                } else {
                    alert("Please input leagal input.");
                }
            },
                error: function () {
                alert("Please input correct data");
            }
        });
    }
    else {
        alert ("Please input correct semester");
    }
}

changeHref();
getTranscriptById();