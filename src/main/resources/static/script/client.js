// message Html template
var messageTemplate = null;

var messageSpace = null;

var frontMessageId = null;

// test message data
const testMessage = {
    avatar: "../default_avatar.png",
    username: "darius.couchard",
    channel: "#bullshit",
    content: "Hello :)",
    messageId: 1,
};

// sets the web socket to receive messages
var socket = null;

const socketAddress = "ws://localhost:2700/websocket/message-socket";

// Page initialization
window.addEventListener('DOMContentLoaded', event => {
    // sets reference to message space in the document
    messageSpace = document.getElementById("messages");

    // prepares the message template document and adds a test one.
    fetch('message.html')
        .then(
            data => data.text())
        .then( function(html) {
            messageTemplate = new DOMParser().parseFromString(html, "text/html");
        });

    // prepares the websocket to receive events.
    try {
        socket = new WebSocket(socketAddress);
    } catch (exception) {
        // alert("Couldn't initialize websocket with server: "+exception);
        console.error(exception);
    }

    socket.onerror = function(error) {
        console.error(error);
    };

    socket.onopen = function(event) {
        console.log("Opened connection to server " + socketAddress);

        this.onclose = function (event) {
            console.log("WebSocket closed. Can't receive message anymore.");
        };

        this.onmessage = function (event) {
            console.log("Received message: "+event.data);
            addMessage(event.data);
        }

    };

    // Changes the time on the bottom left clock
    window.setInterval(function() {
        var time = new Date();
        document.getElementById("clock").innerText =
            addZeroBefore(time.getHours())+":"+
            addZeroBefore(time.getMinutes())+":"+
            addZeroBefore(time.getSeconds());
    }, 1000);

});


// stolen from https://stackoverflow.com/questions/18889548/javascript-change-gethours-to-2-digit
function addZeroBefore(n) {
    return (n < 10 ? '0' : '') + n;
}

// adds a message in the html document
function addMessage(data) {
    // removes the old front message
    if (frontMessageId != null) {
        var oldFrontMessage = document.getElementById(frontMessageId);
        console.log(oldFrontMessage);
        oldFrontMessage.parentNode.removeChild(oldFrontMessage);
        frontMessageId = null;
    }

    const message = JSON.parse(data);
    let copy = messageTemplate.cloneNode(true);

    copy.getElementById("message-id").setAttribute("id", message.messageId);
    copy.getElementById("avatar").setAttribute('src', message.avatar);
    copy.getElementById("channel").innerHTML = "#" + message.channel;
    copy.getElementById("username").innerHTML = message.username;
    console.log(message.content);

    copy.getElementById("content-message").innerHTML = message.content;

    frontMessageId = message.messageId;
    messageSpace.innerHTML += copy.documentElement.innerHTML;
}

function adjustFontSize(content) {
    let innerText = content.innerHTML.repeat(220);
    // content.getElementsByClassName("content-message").fontSize = 20;
    content.fontSize = "100px";
}

// Message data class
class MessageData {
    constructor(avatar, username, channel, content) {
        this.avatar = avatar;
        this.username = username;
        this.channel = channel;
        this.content = content; // HTML
    }
}