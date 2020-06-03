// message Html template
var messageTemplate = null;

// reference to the <div> containing all the messages
var messageSpace = document.getElementById("message-space");

// sets the web socket to receive messages
var socket = null;

// test message data
const testMessage = {
    avatar: "../default_avatar.png",
    avatar: "darius.couchard",
    channel: "#bullshit",
    content: "Hello :)"
};

const socketAddress = "ws://localhost:2700/websocket/message-socket";

try {
    socket = new WebSocket(socketAddress);
} catch (exception) {
    // alert("Couldn't initialize websocket with server: "+exception);
    console.error(exception);
}

socket.onerror = function(error) {
    console.error(error);
    // alert("Error while loading websocket.");
}

socket.onopen = function(event) {
    console.log("Opened connection to server " + socketAddress);

    this.onclose = function (event) {
        console.log("WebSocket closed. Can't receive message anymore.");
        // alert("WebSocket closed. Can't receive message anymore.");
    }

    this.onmessage = function (event) {
        console.log("Received message: "+event.data);
        addMessage(event.data);
    }

}

// prepares the message template document and adds a test one.
fetch('message.html')
    .then(
        data => data.text())
    .then( function(html) {
        messageTemplate = new DOMParser().parseFromString(html, "text/xml");
        addMessage(testMessage);
});

// Changes the time on the bottom left clock
window.setInterval(function() {
    var time = new Date();
    document.getElementById("clock").innerText =
        addZeroBefore(time.getHours())+":"+
        addZeroBefore(time.getMinutes())+":"+
        addZeroBefore(time.getSeconds());
}, 1000);

// stolen from https://stackoverflow.com/questions/18889548/javascript-change-gethours-to-2-digit
function addZeroBefore(n) {
    return (n < 10 ? '0' : '') + n;
}

// adds a message in the html document
function addMessage(data) {
    let copy = messageTemplate.cloneNode(true);

    copy.getElementById("avatar").src = data.avatar;
    copy.getElementById("channel").innerText = data.channel;
    copy.getElementById("username").innerText = data.username;
    copy.getElementById("content-message").innerHTML = data.content;

    console.log(copy);
    console.log(messageSpace)
    messageSpace.innerHTML += copy.innerHTML;
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