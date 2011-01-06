function(data, evt) {
    $.log("called data");
    if (data.rows.length > 0) {
	var item = data.rows[0].value;
	return item;
    } else {
	var itemid = $$(this).itemid;
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