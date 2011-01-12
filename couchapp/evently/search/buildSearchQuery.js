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
    }
    if ($(".search-subclassID-armor").is(':visible')) {
	subclassId = $(".search-subclassID-armor").val();
    }
    var query = {
	name: $(this).find("input.search-input").val(),
	slot: slot,
	classId: classId,
	subclassId: subclassId,
	invType: invType,
	itemLevel: { from: $(this).find("input.ilvlf").val(),
		     to: $(this).find("input.ilvlt").val() }
    };
    $(this).trigger('search', query);
}