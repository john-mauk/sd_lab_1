var celsius = true;

$(function () {

    setTexts();
    toggleDegreeShow();

    $(".grafanaContainer").mouseup(function(){
        console.log("Im here bitches!");
        var iframe;
        if(celsius===true){
            iframe = document.getElementById("graphC");
        }else {
            iframe = document.getElementById("graphF");
        }
        iframe.src = iframe.src;
    });

    $("#tempSimToggle").click(function(){
        $.post("Temp.Simulator");
    });

    $("#highThresholdOverlay").click(function(){
        document.getElementById("highTemp").style.visibility="visible";
        document.getElementById("updateHigh").style.visibility="visible";

    });

    $("#lowThresholdOverlay").click(function(){
        document.getElementById("lowTemp").style.visibility = "visible";
        document.getElementById("updateLow").style.visibility = "visible";
    });

    $("#updateHigh").click(function(){
        if(checkHighBounds()){
            document.getElementById("highTemp").style.visibility = "hidden";
            document.getElementById("updateHigh").style.visibility = "hidden";
            sendHigh();
        }
    });

    $("#updateLow").click(function(){
        if(checkLowBounds()){
            document.getElementById("lowTemp").style.visibility = "hidden";
            document.getElementById("updateLow").style.visibility = "hidden";
            sendLow();
        }
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

    $("#serviceToggle").click(function(){
        $.post("Temp.CheckingService");
    });

    $("#serviceOn").click(function(){
        sendService(1);
    });

    $("#serviceOff").click(function(){
        sendService(0);
    });

    $("#displayToggle").click(function(){
        console.log("toggling display");
        $.post("Device.Display");
    });




    $("#tempC").click(function(){
        celsius = true;
        toggleDegreeShow();
    });

    $("#tempF").click(function(){
        celsius = false;
        toggleDegreeShow();
    });

    $(".textMessage").click(function(){
        sendTexts(this.value,document.getElementById(this.value).value);
    });
});



function reloadiFrames(){
    var list = document.getElementsByClassName("gpanel");
    for(var i=0; i<list.length; i++){
        list[i].src = list[i].src;
    }
}

function toggleDegreeShow(){
    var grafanaC = document.getElementById("graphC");
    var grafanaF = document.getElementById("graphF");
    if(celsius===true){
        $(".degreesC").show();
        $(".degreesF").hide();
        grafanaC.style.height = grafanaF.style.height;
        grafanaC.style.width = grafanaF.style.width;
        grafanaC.style.zIndex = 2;
        grafanaC.style.position = "relative";
        grafanaF.style.zIndex = 0;
        grafanaC.style.visibility = "visible";
        grafanaF.style.visibility = "hidden";
        reloadiFrames();
    }else{
        $(".degreesC").hide();
        $(".degreesF").show();
        grafanaF.style.height = grafanaC.style.height;
        grafanaF.style.width = grafanaC.style.width;
        grafanaF.style.visibility = "visible";
        grafanaF.style.zIndex = 2;
        grafanaC.style.visibility = "hidden";
        grafanaC.style.zIndex = 0;
        grafanaC.style.position = "absolute";
        grafanaC.style.width = "200px";
        grafanaC.style.height = "200px";
        reloadiFrames();
    }

}


function convertToCelsius(temp){
    if(!celsius){
        return Math.round(temp-32)*5/9;
    }else{
        return temp;
    }
}

function sendService(val){
    $.post("Temp.CheckingService","service="+val);
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
    var value = (celsius===true)?document.getElementById("highTemp").value:convertToCelsius(document.getElementById("highTemp").value);
    var tempH = "temp=" + value.toString();
    $.post("Temp.High",tempH);
}//end sendHigh
function sendLow(){
    var value = (celsius===true)?document.getElementById("lowTemp").value:convertToCelsius(document.getElementById("lowTemp").value);
    var tempL = "temp=" + value.toString();
    $.post("Temp.Low",tempL);
}//end sendLow
function checkHighBounds(){
    var high =parseInt(document.getElementById("highTemp").value);
    console.log(checkNum(high.toString()));
    console.log(high);
    if(checkNum(high.toString()) && convertToCelsius(high) >-10 && convertToCelsius(high) <=62){
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
    if(checkNum(low.toString()) && convertToCelsius(low) >=-10 && convertToCelsius(low) <62){
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

function sendTexts(field,text){
    var update = "field="+field+"&text="+text;
    console.log(update);
    $.post("Contact.TextContent",update);
}

function getTexts(field){
    $.get("Contact.TextContent","field="+field,function(msg){
        console.log(msg);
        document.getElementById(field).value = msg;
    });
}

function setTexts(){
    var array = document.getElementsByClassName("textMessage");
    for(var i=0; i<array.length; i++){
        getTexts(array[i].value);
    }
}