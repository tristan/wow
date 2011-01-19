function(response,event,request) {
    $.log("evently/search/_init/selectors/div.item-search-results/search/data.js", 
	  response, event, request, $(this));
    var data = {rows: []};
    for (i in response.rows) {
	var item = response.rows[i].value;
	var include = true;
	if (!(item.name.toLowerCase().search(request.name.toLowerCase()) >= 0))
	    include = false;
	for (var j = 0; j < request.filters.filter.length; j++) {
	    if (item[request.filters.filter[j]] == undefined) {
		include = false;
	    }
	}
	for (var j = 0; j < request.filters.remove.length; j++) {
	    if (item[request.filters.remove[j]] != undefined) {
		include = false;
	    }
	}
	if (include == true) {
	    data.rows.push(item);
	}
    }
    return data;
}
