function(response,event,domain,realm,character) {
    var key = [domain,realm,character];
    $.log("evently/character/load/after.js", response, event, key, $(this));
    if (response.rows.length > 0) {
	var character = response.rows[0].value;
	if (character.requested) {
	    // we have to wait a bit longer
	    $.log("still waiting for character");
	    setTimeout(function() {
		$(this).trigger("load", [domain, realm, character]);
	    }, 1000);
	} else {
	    // we can continue loading!
	    for (k in character.inventory) {
		$.log(k, character.inventory[k], $("tr.slot[slot=" + k + "]"));
		$("tr.slot[slot=" + k + "]").trigger("item", state_str_to_map(character.inventory[k]));
	    }
	}
    } else {
	// there is no character in the db at all, we need to make the request
	var ch = {
	    type: "character",
	    requested: true,
	    domain: domain,
	    realm: realm,
	    character: character,
	    _id: domain + "-" + realm + "-" + character
	};
	$$(this).app.db.saveDoc(ch,
			       {
				   success: function() {
				       $.log("wrote character request");
				       setTimeout(function() {
					   $(this).trigger("load", [domain, realm, character]);
				       }, 1000);
				   }});
    }
}