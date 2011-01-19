function(result,event,request) {
    $.log("evently/slot/item/after.js", result, event, request, $(this));
    if (result.rows.length > 0) {
	var item = result.rows[0].value;
	// save state
	StateManager.setItem($(this).attr("slot"),
			     item,
			     request.re);
	// add reforge values for each stat
	for (key in reforgeIds) {
	    if (item[key] != null) {
		$(this).find(".rff")
		    .children('option[value="' + key + '"]')
		    .append(" (" + parseInt(item[key] * 0.4) + ")");
	    }
	}
	// set reforge
	if (request.re != undefined) {
	    var rf = reforgeIdsReverse[parseInt(request.re)];
	    if (rf != null && rf.length == 2) {
		$(this).find(".rff").val(rf[0]);
		$(this).find(".rft").val(rf[1]);
	    }
	}
	// set gems
	for (k in request) {
	    var g = k.match(/g\d/);
	    if (g != null) {
		$("." + k).trigger("set", request[k]);
	    }
	}
    }
}

