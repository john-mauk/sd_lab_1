$(function () {


    $("#addPhone").click(function () {
        sendContact("add");
    });

    $("#removePhone").click(function () {
        if (confirm("Are you sure you want to remove info?")) {
            sendContact("remove");
            var phone = document.getElementById("contact-modal-phone").value.toString;
            var carrier = document.getElementByID("contact-modal-carrier").value;

            if (carrier !== "--Select Carrier--" && phone === "/[0-9]/" && (phone.length === 10)) {
                document.getElementById("removeButton").disabled = false;
            }
            else{
                document.getElementByID("removeButton").disabled = true;
            }
        }
    });

    $("#testPhone").click(function(){
        sendContact("test");
        var phone = document.getElementById("contact-modal-phone").value.toString;
        var carrier = document.getElementByID("contact-modal-carrier").value;

        if (carrier !== "--Select Carrier--" && phone === "/[0-9]/" && (phone.length === 10)) {
            document.getElementById("testPhone").disabled = false;
        }
        else{
            document.getElementByID("addPhone").disabled = true;
        }
    })
    $("#displayOn").click(function(){

    })
    $("#displayOff").click(function(){

    })
    $("#ServiceOn").click(function(){

    })
    $("#ServiceOff").click(function(){

    })
});

function sendContact(action) {
    var info = "btn=" + action + "&";
    info += "p=" + document.getElementById("contact-modal-phone").value + "&";
    info += "c=" + document.getElementById("contact-modal-carrier").value;

    $.post("Contact.AddRemoveTest", info,
        function (msg) {
            console.log(msg);
            if (msg === "added") {
                console.log("added");
            } else if (msg === "removed") {
                console.log("removed");
            } else if (msg === "tested") {
                console.log("tested");
            } else {
                console.log("error");
            }
        }
    );
    console.log("posted it");
}
function sendHigh(){
    var tempH = "temp=" + document.getElementById("highTemp").value;
    $.post("Temp.High",tempH, function(msg){
        console.log(msg);
    });
}//end sendHigh
function sendLow(){
    var tempL = "temp=" + document.getElementById("lowTemp").value;
    $.post("Temp.Low",tempL,function(msg){
       console.log(msg);
    });
}//end sendLow
function checkHighBounds(){
    high = document.getElementByID("highTemp").value;
    if(high = "/[0-9]/" && parseInt(high) >-10 && parseInt(high) <=62){
        console.log("high threshold temp acceptable.")
    }
    else{
        console.log("high threshold temp failed");
    }
}//end checkHighBounds
function checkLowBounds(){
    low = document.getElementByID("lowTemp").value;
    if(low = "/[0-9]/" && parseInt(low) >=-10 && parseInt(high) <62){
        console.log("Temp threshold temp acceptable.")
    }
    else{
        console.log("Low threshold temp failed");
    }
}//end checkLowBounds
