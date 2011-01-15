function(data,elem,id) {
    // items/gem/after.js
    $.log("gems>after");
    var icon = $("<img>");
    icon
	.attr("src", 
	      "/wow/icons%2F18/" +
	      data.rows[0].value.icon +
	      ".jpg")
	.attr("class", "icon");
    $("div.socket-outer.selected")
	.find(".icon")
	.remove();
    $("div.socket-outer.selected")
	.find("a.socket")
	.prepend(icon);
    $("div.socket-outer.selected").removeClass("selected");
}