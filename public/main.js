function getFiles(data) {
    for (var i in data) {
        var elem = $("<a>");
        elem.attr("href", "files/" + data[i].realFilename);
        elem.text(data[i].comments);
        $("#list").append(data[i].id + ": ")
        $("#list").append(elem);
        $("#list").append($("<br>"));
    }
}

$.get("/files", getFiles);
