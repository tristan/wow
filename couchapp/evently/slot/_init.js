function(event) {
    // evently/slot/_init.js
    $.log("evently/slot/_init.js", event, $(this));
    var slot = $(this).attr("slot");
    var state = StateManager.getKey(slot);
    state = state_str_to_map(state);
    if (state.i != undefined) {
	$(this).trigger("item", state);
    } else {
	$(this).trigger("empty", 
			{slot: slotToName[parseInt(slot)]});
    }
}