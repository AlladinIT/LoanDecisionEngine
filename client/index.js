
function sendJSON(){

    document.getElementById("approved").innerHTML = "";
    document.getElementById("explanation").innerHTML = "";

    let personalCode = document.querySelector('#personalCode');
    let inputSum = document.querySelector('#inputSum');
    let loanPeriod = document.querySelector('#loanPeriod');

    let xhr = new XMLHttpRequest();
    let url = "http://localhost:8080/api";

    xhr.open("POST", url);

    xhr.setRequestHeader("Content-Type", "application/json");

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {

            var json = JSON.parse(xhr.responseText);

            if (json.approved === false || json.approved == null){
                document.getElementById("explanation").style.color = "red";
                document.getElementById("approved").style.color = "red";
                document.getElementById("approved").innerHTML = "Your request was not approved!";
            }
            else{
                document.getElementById("explanation").style.color = "green";
                document.getElementById("approved").style.color = "green";
                document.getElementById("approved").innerHTML = "Your request was approved!";

            }

            //document.getElementById("outputSum").innerHTML = json.outputSum;
            //document.getElementById("suitablePeriod").innerHTML = json.suitablePeriod;
            //document.getElementById("approved").innerHTML = json.approved;
            document.getElementById("explanation").innerHTML = json.explanation;


        }
        console.log(xhr.status);
    };

    var data = JSON.stringify({ "personalCode": personalCode.value, "inputSum": inputSum.value, "loanPeriod": loanPeriod.value});

    xhr.send(data);
}



