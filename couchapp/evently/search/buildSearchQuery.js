function() {
    $.log("building search query");
    var classId = null;
    var subclassId = null;
    var invType = null;
    var slot = $(".search-slot").val();
    if ($(".search-mh").is(':visible')) {
	classId = "2";
	invType = $(".search-mh").val();
    }
    else if ($(".search-oh").is(':visible')) {
	invType = $(".search-oh").val();
	if (invType == "22,13,14,23")
	    classId = "2,4";
	else
	    classId = "2";
    } else if ($(".search-ranged").is(':visible')) {
	invType = "15,25,26,11"; // TODO: maybe optimise this for specific subclasses
	subclassId = $(".search-ranged").val();
	if (subclassId.search(/11/) >= 0) {
	    if (subclassId == "11") {
		classId = "4";
		invType = "28";
	    } else {
		classId = "2,4";
	    }
	} else {
	    classId = "2";
	    invType = "15,25,26";
	}
    } else if ($(".search-armor").is(':visible')) {
	classId = "4";
	invType = String(parseInt(slot)+1);
	subclassId = $(".search-armor").val();
    } else { // assume neck/back/ring/trinket
	subclassId = "0";
	classId = "4";
	switch (slot) {
	    case "1":
	    invType = "2";
	    break;
	    case "14":
	    invType = "16";
	    subclassId = "0,1";
	    break;
	    case "10":
	    invType = "11";
	    break;
	    case "12":
	    invType = "12";
	    break;
	}
    }
    var filters = {
	filter: [],
	remove: []
    };
    $(".stat").each(
	function(a,b) {
	    if ($(b).val() == "yes") {
		filters.filter.push($(b).attr("class").split(/\s+/)[1]);
	    } else if ($(b).val() == "no") {
		filters.remove.push($(b).attr("class").split(/\s+/)[1]);
	    }
	});
    var query = {
	name: $(this).find("input.search-input").val(),
	filters: filters,
	slot: slot,
	classId: classId,
	subclassId: subclassId,
	invType: invType,
	itemLevel: { from: $(this).find("input.ilvlf").val(),
		     to: $(this).find("input.ilvlt").val() }
    };
    $(this).trigger('search', query);
}