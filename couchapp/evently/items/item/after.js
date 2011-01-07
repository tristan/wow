function(data) {
    $.log("called after");
    if (data.rows.length == 0) {
	var itemid = $$(this).itemid;
	var element = this;
	setTimeout(function() {
		       $.log("checking for item " + itemid + " again!");
		       $(element).trigger("getItem", itemid);
		   }, 5000);
    } else {
	// set styles
	var item = data.rows[0].value;
	$(this).addClass("item-quality-" + item.quality);
    }
}