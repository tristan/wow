function(event,request) {
    $.log("evently/slot/item/selectors/div.socket/set/query.js", event, request, $(this));
    return {
	"view": "items",
	"key": request
    };
}