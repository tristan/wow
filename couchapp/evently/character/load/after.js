function(response,event,domain,realm,character) {
    var key = [domain,realm,character];
    $.log("evently/character/load/after.js", response, event, key, $(this));
    var redo = true;
    if (response.rows.length > 0) {
	var character = response.rows[0].value;
	if (!character.requested) {
	}
    }
}