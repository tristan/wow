function(response,event,request) {
    $.log("evently/slot/item/data.js", response, event, request, $(this));
    var slot = slotToName[parseInt($(this).attr("slot"))];
    if (response.rows.length > 0) {
	var item = response.rows[0].value;
	item.slot = slot;
	var sockets = [];
	if (item.sockets != null) {
	    for (var i = 0; i < item.sockets.length; i++) {
		sockets.push({index: i,
			      colour: item.sockets[i]});
	    }
	}
	item.sockets = sockets;
	return item;
    } else {
	$(this).trigger("empty", {slot: slot});
	return false;
    }
}