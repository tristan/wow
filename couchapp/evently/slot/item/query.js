function(event,item) {
    $.log("evently/slot/item/query.js", event, item, $(this));
    return {
	"view": "items",
	"key": item.i
    };
}