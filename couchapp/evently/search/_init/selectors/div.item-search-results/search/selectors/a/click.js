function(event) {
    $.log("evently/search/_init/selectors/div.item-search-results/search/selectors/a/click.js",
	  event, $(this));
    var type = $(".search-slot").val();
    var slot = $(".item-search").find("input[type=hidden][name=slot]").val();
    var id = $(this).attr("itemid");
    if (type == "18") { // gems
	var socket = $(".item-search").find("input[type=hidden][name=socket]").val();
	$("tr.slot[slot=" + slot + "]").find("div[socket=" + socket + "]")
	    .trigger("set", id);
    } else {
	$("tr.slot[slot=" + slot + "]").trigger(
	    "item", {i: id});
    }
    $(".item-search").hide();
}