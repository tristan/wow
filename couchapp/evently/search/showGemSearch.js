function(event,slot) {
    $.log("evently/search/showGemSearch.js", event, slot, $(this));
    $(this).show();
    $(this).find("input[type=hidden][name=slot]").val(slot.slot);
    $(this).find("input[type=hidden][name=socket]").val(slot.socket);
    $(this).find("select.search-slot").val("18"); // 18 for gems!
    if (slot.colour == "Red") {
	$(this).find("select.search-gem").val("0,3,5");
    } else if (slot.colour == "Blue") {
	$(this).find("select.search-gem").val("1,3,4");
    } else if (slot.colour == "Yellow") {
	$(this).find("select.search-gem").val("2,4,5");
    } else if (slot.colour == "Meta") {
	$(this).find("select.search-gem").val("6");
    } else if (slot.colour == "Cogwheel") {
	$(this).find("select.search-gem").val("10");
    } else {
	$(this).find("select.search-gem").val("0,1,2,3,4,5,6,8,10");
    }
    $(this).find("select.search-slot").trigger("change");
}