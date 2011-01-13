function(evt, item) {
    //$.log("called query", id);
    var id;
    if (typeof(item) == "object") {
	id = item.id;
    } else {
	id = item;
    }
    return {
	view: "items",
	key: id
    };
}