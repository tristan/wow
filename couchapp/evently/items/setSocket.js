function(evt, itemid) {
    $.log("gem:", itemid);
    $(this).find("div.socket-outer.selected")
	.trigger('gem', itemid);
}