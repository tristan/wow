function(event) {
    $.log("evently/slot/item/selectors/div.socket/click.js", event, $(this));
    var itemslot = $(this).parent().parent().attr("slot");
    var socket = $(this).attr("socket");
    var colour = $(this).attr("colour");
    $(".item-search").trigger("showGemSearch", 
			      {slot: itemslot,
			       colour: colour,
			       socket: socket});
}