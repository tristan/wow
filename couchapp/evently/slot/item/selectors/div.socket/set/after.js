function(response,event,request) {
    $.log("evently/slot/item/selectors/div.socket/set/after.js",
	  response, event, request, $(this));
    if (response.rows.length > 0) {
	var gem = response.rows[0].value;
	var slot = $(this).parent().parent().attr("slot");
	var socket = $(this).attr("socket");
	var gems = {};
	gems["g"+socket] = gem;
	StateManager.setGems(slot, gems);
    }
}