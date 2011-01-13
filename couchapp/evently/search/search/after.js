function(data, evt, query) {
    $(this).find(".item-search-result").remove();
    var items = [];
    for (var i = 0; i < data.rows.length; i++) {
	var item = data.rows[i].value;
	var include = true;
	if (!(item.name.toLowerCase().search(query.name.toLowerCase()) >= 0))
	    include = false;
	for (var j = 0; j < query.filters.filter.length; j++) {
	    if (item[query.filters.filter[j]] == undefined) {
		include = false;
	    }
	}
	for (var j = 0; j < query.filters.remove.length; j++) {
	    if (item[query.filters.remove[j]] != undefined) {
		include = false;
	    }
	}
	if (include == true) {
	    items.push(item);
	}
    }
    $(this).trigger('addSearchResults', [items]);
}