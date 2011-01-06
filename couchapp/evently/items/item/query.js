function(evt) {
    $.log("called query");
    return {
	view: "items",
	key: $$(this).itemid
    };
}