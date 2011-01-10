function(evt, key) {
    $.log("searching for:" + key);
    return {
	view: "items-by-name",
	startkey: key,
	endkey: key + "\u9999"
    };
}