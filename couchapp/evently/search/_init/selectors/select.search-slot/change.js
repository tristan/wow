function(evt, gemcolor) {
    // search/_init/selectors/select.search-slot/change.js
    var val = $(this).val();
    $(".search-subclass").hide();
    $(".ilvls-for-items").show();
    $(".ilvls-for-gems").hide();
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
	case "18":
	    $(".search-gem").show();
	    $(".ilvls-for-items").hide();
	    $(".ilvls-for-gems").show();
	    $.log(gemcolor);
	    switch (gemcolor) {
		case "Red":
		$(".search-gem").val("0,3,5");
		break;
		case "Blue":
		$(".search-gem").val("1,3,4");
		break;
		case "Yellow":
		$(".search-gem").val("2,4,5");
		break;
		case "Meta":
		$(".search-gem").val("6");
		break;
		case "Prismatic":
		$(".search-gem").val("0,1,2,3,4,5,6,8,10");
		break;
		case "Cogwheel":
		$(".search-gem").val("10");
		break;
	    }
	    break;
	default:
	    $(".search-armor").show();
	    break;
    }
    $(this).trigger('buildSearchQuery');
}