function(event) {
    $.log("evently/extra-stats/_init/selectors/input/change.js", event, $(this));
    $(".set-stats").trigger("update");
}