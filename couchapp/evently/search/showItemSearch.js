function(event,slot) {
    $.log("evently/search/show.js", event, slot, $(this));
    $(this).show();
    $(this).find("input[type=hidden][name=slot]").val(slot);
    if (slot == "11") {
	slot = "10";
    } else if (slot == "13") {
	slot = "12";
    }
    $(this).find("select.search-slot").val(slot);
    $(this).find("select:first").trigger("change");
}