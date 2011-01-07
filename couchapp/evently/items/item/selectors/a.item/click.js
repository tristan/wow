function(evt) {
    var itemid = prompt("Enter item id");
    if (itemid) {
	$(this).parent().parent().parent().removeClass(
	    "item-quality-1 item-quality-2 item-quality-3 item-quality-4 item-quality-5 item-quality-6 item-quality-7 item-quality-8 item-quality-9"
	);
	$(this).trigger('getItem', itemid);
    }
}