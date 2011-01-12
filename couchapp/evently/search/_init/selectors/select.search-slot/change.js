function() {
    // search/_init/selectors/select.search-slot/change.js
    var val = $(this).val();
    $(".search-subclass").hide();
    switch (val) {
	case "15":
	    $(".search-mh").show();
	    break;
	case "16":
	    $(".search-oh").show();
	    break;
	case "17":
	    $(".search-ranged").show();
	    break;
	case "10":
	case "12":
	case "1":
	case "14":
	    break;
	default:
	    $(".search-subclassID-armor").show();
	    break;
    }
    $(this).trigger('buildSearchQuery');
}