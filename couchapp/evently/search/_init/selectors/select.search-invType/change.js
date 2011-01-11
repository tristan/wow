function() {
    var val = $(this).val();
    $(".search-subclass").hide();
    switch (val) {
	case "main-hand":
	    $(".search-subclassID-weapons-mh").show();
	    break;
	case "off-hand":
	    $(".search-subclassID-weapons-oh").show();
	    break;
	case "ranged":
	    $(".search-subclassID-weapons-ranged").show();
	    break;
	case "ring":
	case "trinket":
	case "neck":
	case "back":
	    break;
	default:
	    $(".search-subclassID-armor").show();
	    break;
    }
    $(this).trigger('buildSearchQuery');
}