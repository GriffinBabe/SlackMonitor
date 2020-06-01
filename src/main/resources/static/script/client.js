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