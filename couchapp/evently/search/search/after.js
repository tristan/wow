function(data, evt) {
    $.log("after search");
    $(this).find(".item-search-result").remove();
    var items = [];
    for (var i = 0; i < data.rows.length; i++) {
	var item = data.rows[i].value;
	items.push(item);
    }
    $(this).trigger('addSearchResults', [items]);
}