// message Html template
var messageTemplate = null;

// reference to the <div> containing all the messages
var messageSpace = document.getElementById("message-space");

// sets the web socket to receive messages
var socket = null;

const socket_address = "ws://localhost:2700/websocket/message-socket";

try {
    socket = new WebSocket(socket_address);
} catch (exception) {
    alert("Couldn't initialize websocket with server: "+exception);
    console.error(exception);
}

socket.onerror = function(error) {
    console.error(error);
    alert("Error while loading websocket.");
}

socket.onopen = function(event) {
    console.log("Opened connection to server " + socket_address);

    this.onclose = function (event) {
        console.log("WebSocket closed. Can't receive message anymore.");
        alert("WebSocket closed. Can't receive message anymore.");
    }

    this.onmessage = function (event) {
        console.log("Received message: "+event.data);
    }

}

// loads the message html code sets it to messageTemplate
document.addEventListener('readystatechange', () => {
    fetch('message.html')
        .then(
            data => data.text())
        .then(
            html => messageTemplate = html
        );
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
    let copy = Object.assign({}, messageTemplate);
    messageTemplate.getElementById("avatar").src = data.avatar;
    messageTemplate.getElementById("channel").innerText = data.channel;
    messageTemplate.getElementById("username").innerText = data.username;
    messageTemplate.getElementById("content-message").innerHTML = data.content;

    messageSpace.innerHTML += messageTemplate;
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