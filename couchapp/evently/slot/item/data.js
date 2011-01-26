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
	// socket bonuses
	if (item.socketBonus != null) {
	    var bonus = {};
	    for (k in item.socketBonus) {
		var stat = k.match(/^bonus(\w+)$/) || [];
		if (stat.length > 1) {
		    stat = stat[1];
		    var stat1 = stat.match(/^(\w+)Rating$/) || [];
		    if (stat1.length > 1) {
			bonus.socketBonusStat = stat1[1] + " Rating";
		    } else {
			bonus.socketBonusStat = stat;
		    }
		}
		bonus.socketBonusValue = item.socketBonus[k];
	    }
	    item.socketBonus = bonus;
	}
	item.sockets = sockets;
	return item;
    } else {
	$(this).trigger("empty", {slot: slot});
	return false;
    }
}