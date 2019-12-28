var username = window.location.search.substr(1);
var currentEnrollList;

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

function getcurrentEnroll() {
    $.ajax ({
        url: "/withdraw/currentEnroll/" + username,
        datatype: "json",
        type: 'get',
        async: true,
        success: function (data) {
            currentEnrollList = data;
            var courseNum = data.length;
            for(var i = 0; i !== courseNum; ++i) {
                var firstTH = document.createElement('th');
                firstTH.setAttribute("id", i + "UoSCode");
                firstTH.innerText = data[i]["uoSCode"];
                var secondTH = document.createElement('th');
                secondTH.setAttribute("id", i + "UoSName");
                secondTH.innerText = data[i]["uoSName"];
                var thirdTH = document.createElement('th');
                thirdTH.setAttribute("id", i + "Semester");
                thirdTH.innerText = data[i]["semester"];
                var fourth = document.createElement('th');
                fourth.setAttribute("id", i + "Year");
                fourth.innerText = data[i]["year"];
                var fifthTH = document.createElement('button');
                fifthTH.setAttribute("class", "btn btn-primary btn-xs");
                fifthTH.setAttribute("onclick", "withdraw(" + i + ")");
                fifthTH.innerText = "withdraw";

                var tr = document.createElement('tr');
                tr.setAttribute("id", i + "Tr");
                tr.insertAdjacentElement("beforeend", firstTH);
                tr.insertAdjacentElement("beforeend", secondTH);
                tr.insertAdjacentElement("beforeend", thirdTH);
                tr.insertAdjacentElement("beforeend", fourth);
                tr.insertAdjacentElement("beforeend", fifthTH);
                document.getElementById("withdrawTable").insertAdjacentElement("beforeend", tr);
            }
        },
        error: function (data) {
            alert("System is busy, please try again later.");
        }
    })
}

function withdraw(i) {
    var courseCode = currentEnrollList[i]["uoSCode"];
    var courseQuarter = currentEnrollList[i]["semester"];
    var courseYear = currentEnrollList[i]["year"];

    //alert(courseCode + courseQuarter + courseYear);

    $.ajax({
        url: "/withdraw/withdrawCourse/" + username + "/" + courseCode + "/" + courseYear + "/" + courseQuarter,
        datatype: "json",
        type: "POST",
        async: true,
        success: function (data) {
            if (data[0] === "suc") {
                a = document.getElementById("withdrawTable");
                b = document.getElementById(i + "UoSCode").parentNode;
                a.removeChild(b);
            }
            else alert("Fail to withdraw.");

            if (data.length === 2) {
                alert(data[1]);
            }
        },
        error: function (data) {
            alert("System is busy. Please try again later.");
        }
    });
}

changeHref();
getcurrentEnroll();