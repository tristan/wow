function(event,domain,realm,character) {
    var key = [domain,realm,character];
    $.log("evently/character/load/query.js", event, key, $(this));
    return {
	"view": "characters",
	"key": key
    };
}