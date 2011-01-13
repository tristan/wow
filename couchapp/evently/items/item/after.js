function(data, evt, itemid) {
    //$.log("called after", data, evt, itemid);
    if (data.rows.length == 0) {
	var element = this;
	setTimeout(function() {
		       $.log("checking for item " + itemid + " again!");
		       $(element).trigger("getItem", itemid);
		   }, 5000);
    } else {
	// set styles
	//var item = data.rows[0].value;
	//$(this).addClass("item-quality-" + item.quality);
    }
}