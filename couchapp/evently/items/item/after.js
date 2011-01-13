function(data, evt, req) {
    //$.log("called after", data, evt, itemid);
    var itemid = req;
    if (typeof(req) == "object") {
	itemid = req.id;
    }
    if (data.rows.length == 0) {
	var element = this;
	setTimeout(function() {
		       $.log("checking for item " + itemid + " again!");
		       $(element).trigger("getItem", itemid);
		   }, 5000);
    } else {
	var item = data.rows[0].value;
	// display reforge values
	for (key in reforgeIds) {
	    if (item[key] != null) {
		$(this).find(".rff")
		    .children('option[value="' + key + '"]')
		    .append(" (" + parseInt(item[key] * 0.4) + ")");
	    }
	}

	if (typeof(req) == "string") { // this is the first time it's set
	    //$(this).addClass("item-quality-" + item.quality);
	    var slot = parseInt($(this).attr("slot"));
	    StateManager.addItem(slot, item);
	} else { // otherwise it's being set by the loader
	    // set reforges
	    if (typeof(req) == "object") {
		if (req.re != null) {
		    var rf = reforgeIdsReverse[parseInt(req.re)];
		    if (rf != null && rf.length == 2) {
			$(this).find(".rff").val(rf[0]);
			$(this).find(".rft").val(rf[1]);
		    }
		}
	    }
	}
    }
}