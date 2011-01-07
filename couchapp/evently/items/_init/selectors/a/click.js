function(evt) {
    var itemid = prompt("Enter item id");
    if (itemid) {
	$(this).trigger('getItem', itemid);
    }
}