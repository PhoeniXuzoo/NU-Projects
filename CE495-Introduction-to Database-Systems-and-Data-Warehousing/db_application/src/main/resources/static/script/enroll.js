var username = window.location.search.substr(1);
var courseOfferingList = [];

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

function getCourseOffering() {
    $.ajax({
        url: "/enroll/courseOffering/" + username,
        datatype: "json",
        type: 'get',
        async: true,
        success: function (data) {
            courseOfferingList = data;
            var courseNum = data.length;
            for (var i = 0; i !== courseNum; ++i) {
                var firstTH = document.createElement('th');
                firstTH.innerText = data[i]["uoSCode"];
                var secondTH = document.createElement('th');
                secondTH.innerText = data[i]["uoSName"];
                var thirdTH = document.createElement('th');
                thirdTH.innerText = data[i]["semester"];
                thirdTH.setAttribute("id", i + "thSemester");
                var fourthTH = document.createElement('th');
                fourthTH.innerText = data[i]["year"];
                fourthTH.setAttribute("id", i + "thYear");

                var fifthTH = document.createElement('th');
                fifthTH.setAttribute("id", i + "thEnrollment");
                if (data[i]["enrollment"] === null)
                    fifthTH.innerText = "No data";
                else
                    fifthTH.innerText = data[i]["enrollment"];

                var sixthTH = document.createElement('th');
                if (data[i]["maxEnrollment"] === null)
                    sixthTH.innerText = "No data";
                else
                    sixthTH.innerText = data[i]["maxEnrollment"];

                var seventhTH = document.createElement('button');
                seventhTH.setAttribute("class", "btn btn-primary btn-xs");
                seventhTH.setAttribute("id", String(i));
                seventhTH.setAttribute("onclick", "enroll(" + i + ")");
                seventhTH.innerText = "enroll";

                var tr = document.createElement('tr');
                tr.insertAdjacentElement("beforeend", firstTH);
                tr.insertAdjacentElement("beforeend", secondTH);
                tr.insertAdjacentElement("beforeend", thirdTH);
                tr.insertAdjacentElement("beforeend", fourthTH);
                tr.insertAdjacentElement("beforeend", fifthTH);
                tr.insertAdjacentElement("beforeend", sixthTH);
                tr.insertAdjacentElement("beforeend", seventhTH);

                document.getElementById("courseOfferingTable").insertAdjacentElement("beforeend", tr);
            }
        }
    });
}

function enroll(i) {
    var courseNum = courseOfferingList[i]["uoSCode"];

    $.ajax({
        url: "/enroll/enroll/" + username + "/" + courseNum + "/" + document.getElementById(i + "thSemester").innerText + "/" + document.getElementById(i + "thYear").innerText,
        datatype: "json",
        type: 'post',
        async: true,
        success: function (data) {
            if (data[0] === "suc") {
                alert("success to enroll the course.");
                var newEnrollment = Number(document.getElementById(i + "thEnrollment").innerText) + 1;
                document.getElementById(i + "thEnrollment").innerText = String(newEnrollment);
            }
            else if (data[0] === "in") {
                alert("This course is already in your transcript.");
            }
            else if (data[0] === "full") {
                alert("Fail: Max Enrollment.");
            }
            else {
                alert("you have to enroll these courses first: " + JSON.stringify(data));
            }
        }
    });
}


getCourseOffering();
changeHref();