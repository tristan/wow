function(response,event,request) {
    $.log("evently/slot/item/selectors/div.socket/set/data.js",
	  response, event, request, $(this));
    if (response.rows.length > 0) {
	return response.rows[0].value;
    } else {
	return null;
    }
}