function(data, evt, query) {
    $(this).find(".item-search-result").remove();
    var items = [];
    for (var i = 0; i < data.rows.length; i++) {
	var item = data.rows[i].value;
	if (item.name.toLowerCase().search(query.name.toLowerCase()) >= 0)
	    items.push(item);
    }
    $(this).trigger('addSearchResults', [items]);
}