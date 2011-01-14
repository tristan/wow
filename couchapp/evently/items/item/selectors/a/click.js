function(evt) {
    // items/item/selectors/a/click.js
    $(".item-search").show();
    var slot;
    if ($(this).attr("class") == "socket") {
	slot = "18";
	$(this).parent().siblings().removeClass("selected");
	$(this).parent().addClass("selected");
    } else {
	slot = $(this).parent().parent().attr("slot");
	$(this).parent().parent().siblings().removeClass("selected");
	$(this).parent().parent().addClass("selected");
    }
    if (slot == "11") {
	slot = "10";
    } else if (slot == "13") {
	slot = "12";
    }
    $(".search-slot").val(slot).trigger('change', $(this).attr("color"));
}