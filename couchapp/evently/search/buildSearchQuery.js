function() {
    $.log("building search query");
    var classId = "2";
    var subclassId = "0";
    var invType = "0";
    switch ($(this).find("select.search-invType").val()) {
	case "main-hand":
	case "off-hand":
	case "ranged":
	break;
	case "head":
	classId = "4";
	invType = "1";
	break;
	case "neck":
	classId = "4";
	invType = "2";
	break;
	case "sholders":
	classId = "4";
	invType = "3";
	break;
	case "back":
	case "chest":
	classId = "4";
	invType = "5";
	break;
	case "wrists":
	classId = "4";
	invType = "9";
	break;
	case "hands":
	classId = "4";
	invType = "10";
	break;
	case "waist":
	classId = "4";
	invType = "6";
	break;
	case "legs":
	classId = "4";
	invType = "7";
	break;
	case "feet":
	classId = "4";
	invType = "8";
	break;
	case "ring":
	classId = "4";
	invType = "11";
	break;
	case "trinket":
	classId = "4";
	invType = "12";
	break;
    }
    if ($(".search-subclassID-armor").is(':visible')) {
	subclassId = $(".search-subclassID-armor").val();
    }
    var query = {
	name: $(this).find("input.search-input").val(),
	classId: classId,
	subclassId: subclassId,
	invType: invType,
	itemLevel: { from: $(this).find("input.ilvlf").val(),
		     to: $(this).find("input.ilvlt").val() }
    };
    $(this).trigger('search', query);
}