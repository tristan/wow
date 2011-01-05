function(data) {
    $.log(data);
    if (data.rows.length > 0) {
	var item = data.rows[0].value;
	return item;
    } else {
	setTimeout(function() {
		       $.log("checking item again!");
		       //$.log($(evt.target).children("input").val());
		       //$(evt).trigger("getItem", evt.target);
		   }, 1000);
	return {
	    id: false
	};
    }
}