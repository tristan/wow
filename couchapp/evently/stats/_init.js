function(event) {
    // evently/slot/_init.js
    $.log("evently/stats/_init.js", event, $(this));
    $(this).trigger("update");
}