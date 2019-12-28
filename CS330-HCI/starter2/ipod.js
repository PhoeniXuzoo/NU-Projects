// Create your global variables below:
var tracklist = ["ori, Lost in the Storm", "Naru, Embracing the Light", "Calling Out", "The Blinded Forest", "Inspiriting", "First Step", "Finding Sein", "Up the Spirit Caverns Walls", "The Spirit Tree", "Kuro's Tale I - Her Rage"];
var volLevels = [];
var currentVol;
var ithSong = 6;            /*now, it is ith song on player*/
var currentPlayTime;
var isPlay = false;         /*is it playing?*/
var interval;               /*handler of setInterval, use it to stop and reactive*/

function playBackFn() {     /*callback function which helps to control playback or pause*/
    if (currentPlayTime >= 0 && currentPlayTime <= 180) {
        currentPlayTime = document.getElementById("time-elapsed").value;
        document.getElementById("player-time").innerText = secondsToMs(++currentPlayTime);
        document.getElementById("time-elapsed").value = currentPlayTime;
    }
    if (currentPlayTime >= 181) nextSong();
}

function init() {
	// Your code goes here
     volLevels = new Array(6);
     for (i = 0; i < volLevels.length; ++i) {
         volLevels[i] = document.getElementById("vl" + i);
     }
     for (i = 0; i < 3; ++i) {
         volLevels[i].style.backgroundColor = "#9f5cc4";
     }
     currentVol = 2;
     currentPlayTime = 0;
}

function volUp() {
	// Your code goes here
    if (currentVol < 5)
        volLevels[++currentVol].style.backgroundColor = "#9f5cc4";
}

function volDown() {
	// Your code goes here
    if (currentVol > -1)
        volLevels[currentVol--].style.backgroundColor = "#FFFFFF";
}

/*click time-bar to modify time*/
function modifyTime() {
    currentPlayTime = document.getElementById("time-elapsed").value;
    document.getElementById("player-time").innerText = secondsToMs(currentPlayTime);
}

function switchPlay() {
	// Your code goes here
    if (!isPlay) {
        isPlay = true;
        document.getElementById("switch").innerHTML = "pause";
        interval = setInterval(playBackFn, 1000);
    }
    else {
        isPlay = false;
        document.getElementById("switch").innerHTML = "play_arrow";
        clearInterval(interval);
    }
}

function nextSong() {
	// Your code goes here
    currentPlayTime = 0;
    document.getElementById("player-time").innerText = secondsToMs(currentPlayTime);
    document.getElementById("time-elapsed").value = currentPlayTime;
    ithSong = ++ithSong > 9 ? 0 : ithSong;
    document.getElementById("player-song-name").innerText = tracklist[ithSong];
}

function prevSong() {
	// Your code goes here
    currentPlayTime = 0;
    document.getElementById("player-time").innerText = secondsToMs(currentPlayTime);
    document.getElementById("time-elapsed").value = currentPlayTime;
    ithSong = --ithSong < 0 ? 9 : ithSong;
    document.getElementById("player-song-name").innerText = tracklist[ithSong];
}

function secondsToMs(d) {
    d = Number(d);

    var min = Math.floor(d / 60);
    var sec = Math.floor(d % 60);

    return `0${min}`.slice(-1) + ":" + `00${sec}`.slice(-2);
}

init();