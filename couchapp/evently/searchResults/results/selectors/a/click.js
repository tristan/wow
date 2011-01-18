function() {
    // searchResults/results/selectors/a/click.js
    //$.log("item: " + $(this).attr("href"));
    //$.log("slot: " + $("tr.slot.selected").attr("slot"));
    var selected = $("tr.slot.selected");
    if (selected.length != 0) {
	$.log(selected);
	selected.trigger('getItem', $(this).attr("itemid"));
	$("tr.slot.selected").removeClass('selected');
    } else {
	selected = $("div.socket-outer.selected");
	if (selected.length != 0) {
	    $.log(selected);
	    var slot = selected.parent().parent().attr("slot");
	    var socket = null;
	    selected.parent().children().each(
		function(index, elem) {
		    $.log(index, $(elem).attr("class").match(/selected/));
		    if ($(elem).attr("class").match(/selected/))
			socket = String(index);
		});
	    if (socket != null) {
		selected.trigger('setSocket', {slot: slot, socket: socket, id: $(this).attr("itemid")});
		$("div.socket-outer.selected").removeClass('selected');
	    }
	}
    }
    // TODO: these may be better futher down the chain
    $(".item-search").hide();
}