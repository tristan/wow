function(data, evt, itemid) {
    //$.log("called data", data, evt, itemid);
    if (data.rows.length > 0) {
	var item = data.rows[0].value;
	item.slot = $(this).children("td.slot").html();
	$.log(item.slot);
	return item;
    } else {
	$.log("didn't find: " + itemid);
	var ireq = {
	    id: itemid,
	    type: "item",
	    _id: "item-" + itemid
	};
	$$(this).app.db.saveDoc(ireq, {
				    success : function() {
					$.log("success");
				    },
				    error : function(e) {
					if (e == 409) {
					    $.log("item already requested");
					}
				    }
				});
	return {
	    name: false,
	    id: itemid
	};
    }
}