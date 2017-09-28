$(function () {


    $("#addPhone").click(function () {
        sendContact("add");
    });

    $("#removePhone").click(function () {
        if (confirm("Are you sure you want to remove info?")) {
            sendContact("remove");}
    });

    $("#testPhone").click(function(){
        sendContact("test");
    });
    $("#ServiceOn").click(function(){

    })
    $("#ServiceOff").click(function(){

    })

    $("#displayOn").click(function(){
        //document.getElementById("displayOn").g
        sendDisplay(1);
    })

    $("#displayOff").click(function(){
        sendDisplay(0);
    })
});

function sendDisplay(val){
    $.post("Device.Display","display="+val);
}

function sendContact(action) {
    var info = "btn=" + action + "&";
    info += "p=" + document.getElementById("contact-modal-phone").value + "&";
    info += "c=" + document.getElementById("contact-modal-carrier").value;
    if(checkNum(parseInt(document.getElementById("contact-modal-phone").value))){
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
    }//end if
    else{
        alert("Invalid number, must enter a numeric value");
    }
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
    var high =parseInt(document.getElementByID("highTemp").value);
    if(checkNum(high) && high >-10 && high <=62){
        console.log("high threshold temp acceptable.")
    }
    else{
        console.log("high threshold temp failed");
        alert("Invalid High Threshold");
    }
}//end checkHighBounds
function checkLowBounds(){
    var low = parseInt(document.getElementByID("lowTemp").value);
    if(checkNum(low) && low >=-10 && low <62){
        console.log("Temp threshold temp acceptable.")
    }
    else{
        console.log("Low threshold temp failed");
        alert("Invalid Low Threshold");
    }
}//end checkLowBounds
function checkNum(value){
    if(value==="/[0-9]+/"){
        return true;
    }
    else{
        return false;
    }
}//end checkNum