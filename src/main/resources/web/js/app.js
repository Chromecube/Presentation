let numArr = "";


$(document).ready(function () {
    toggleF11Text(document.documentElement);
    refresh();
})

$(document).keyup(function (event) {
    let num = event.which - 48;

    if (event.which === 65) { //A
        previousImage();
    } else if (event.which === 68) { //D
        nextImage();
    } else if (event.which === 66) { //B
        $("#blackscreen").toggle();
    } else if (event.which === 76) { //L
        $("#logo").toggle();
    } else if (event.which === 122) { //F11
        toggleF11Text();
    } else if (event.which === 82) { //R
        refresh();
    } else if (event.which === 85) { //U
        inverseColorScheme();
    } else if (num >= 0 && num < 10) {
        console.log("Number: " + num);
        numArr += num;
    } else if (event.which === 13 && numArr.length > 0) { //Enter
        axios.get("/api/set/" + numArr).then(function (response) {
            update(response.data);
        });
        numArr = "";
    } else {
        console.log(event.which);
    }
});

function toggleF11Text() {
    if (window.fullScreen || window.fullscreen) {
        $("#f11").hide();
    } else {
        $("#f11").show();
    }

}

function refresh() {
    axios.get("/api/desc").then(function (response) {
        update(response.data);
    });
}

function previousImage() {
    console.log("Loading previous image");
    axios.get("/api/prev").then(function (response) {
        update(response.data);
    });
}

function nextImage() {
    console.log("Loading next image");
    axios.get("/api/next").then(function (response) {
        update(response.data);
    });
}

function update(restData) {
    if (restData.name.length === 0 || restData.number.length === 0) {
        $("#left").hide();
        $("#right").hide();
    } else {
        $("#left").show();
        $("#right").show();
    }
    $("#left").text(restData.name);
    $("#image").attr("src", restData.link);
    $("#right").text("Bild " + restData.number);
}

function inverseColorScheme() {
    document.body.getAttribute("data-theme") === "light" ? document.body.setAttribute("data-theme", "dark") : document.body.setAttribute("data-theme", "light");
}