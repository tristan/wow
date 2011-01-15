function() {
    // searchResults/results/selectors/a/click.js
    //$.log("item: " + $(this).attr("href"));
    //$.log("slot: " + $("tr.slot.selected").attr("slot"));
    $("tr.slot.selected")
	.trigger('getItem', $(this).attr("itemid"));
    $("div.socket-outer.selected")
	.trigger('setSocket', $(this).attr("itemid"));
    // TODO: these may be better futher down the chain
    $("tr.slot.selected").removeClass('selected');
    $(".item-search").hide();
}