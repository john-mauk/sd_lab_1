$(function () {

    $("#contact-modal-close").click(function () {
        closeContactModal();
    });

    $("#contact-add-remove").click(function () {
        viewContactModal();
    });

    $("#addContactButton").click(function () {
        sendInfo("add");
    });

    $("#removeContactButton").click(function () {
        if (confirm("Are you sure you want to remove info?")) {
            sendInfo("remove");
        }
    });
});

function viewContactModal() {
    document.getElementById("contact-modal").style.display = "block";
}

function closeContactModal() {
    document.getElementById("contact-modal").style.display = "none";
}

function sendInfo(action) {
    var info = "btn=" + action + "&";
    info += "p=" + document.getElementById("contact-modal-phone").value + "&";
    info += "c=" + document.getElementById("contact-modal-carrier").value;

    $.post("Contact.Add", info,
        function (msg) {
            console.log(msg);
            if (msg === "added") {
                console.log("added");
            } else if (msg === "removed") {
                console.log("removed");
            } else {
                console.log("error");
            }
            closeContactModal();
        }
    );

    console.log("posted it");


}


