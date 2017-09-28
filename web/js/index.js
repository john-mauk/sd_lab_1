$(function () {

    $("#addPhone").click(function () {
        sendInfo("add");
    });

    $("#removePhone").click(function () {
        if (confirm("Are you sure you want to remove info?")) {
            sendInfo("remove");
        }
    });

    $("#testPhone").click(function(){
        sendInfo("test");
    })
});

function sendInfo(action) {
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
    var tempH = document.getElementById("highText").value;
    $.post("Temp.High",tempH, function(msg){
        console.log(msg);
        if(msg.length >0){
        }
        else{
            console.log("Error: No value")
        }

    });



}//end sendHigh


