function(event,slot) {
    $.log("evently/search/show.js", event, slot, $(this));
    $(this).show();
    $(this).find("input[type=hidden][name=slot]").val(slot);
    $(this).find("select.search-slot").val(slot);
    $(this).find("select:first").trigger("change");
}