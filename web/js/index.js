$(function () {

    $("lowTemp").text(function(){

    });
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

    });
    $("#ServiceOff").click(function(){

    });

    $("#displayOn").click(function(){
        //document.getElementById("displayOn").g
        sendDisplay(1);
    });

    $("#displayOff").click(function(){
        sendDisplay(0);
    });
    $("#updateHigh").click(function(){
        borderColor = "red";
        if(checkHighBounds()){
            borderColor ="green";
        }
    });
    $("#updateLow").click(function(){
        borderColor = "red";
        if(checkLowBounds()){
            borderColor ="green";
        }
    });
});

function sendDisplay(val){
    $.post("Device.Display","display="+val);
}

function sendContact(action) {
    var info = "btn=" + action + "&";
    info += "p=" + document.getElementById("contact-modal-phone").value + "&";
    info += "c=" + document.getElementById("contact-modal-carrier").value;
    console.log("Phone number: "+ parseInt(document.getElementById("contact-modal-phone").value));
    if(checkNum((document.getElementById("contact-modal-phone").value))){
        console.log("Made it!");
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
        console.log("error");
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
    var high =parseInt(document.getElementById("highTemp").value);
    console.log(checkNum(high.toString()));
    console.log(high);
    if(checkNum(high.toString()) && high >-10 && high <=62){
        console.log("high threshold temp acceptable.");
        return true;
    }
    else{
        console.log("high threshold temp failed");
        alert("Invalid High Threshold");
        return false;
    }
}//end checkHighBounds
function checkLowBounds(){
    var low =parseInt(document.getElementById("lowTemp").value);
    console.log(checkNum(low.toString()));
    console.log(low);
    if(checkNum(low.toString()) && low >=-10 && low <62){
        console.log("Temp threshold temp acceptable.");
        return true;
    }
    else{
        console.log("Low threshold temp failed");
        alert("Invalid Low Threshold");
        return false;
    }
}//end checkLowBounds
function checkNum(value){
    console.log("value= " + value);
    if(/[0-9]/.test(value)){
        console.log("true");
        return true;
    }
    else{
        console.log("false");
        return false;
    }
}//end checkNum