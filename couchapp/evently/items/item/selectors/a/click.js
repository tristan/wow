function(evt) {
    // items/item/selectors/a/click.js
    $(".item-search").show();
    var slot = $(this).parent().parent().attr("slot");
    $(this).parent().parent().siblings().removeClass("selected");
    $(this).parent().parent().addClass("selected");
    if (slot == "11") {
	slot = "10";
    } else if (slot == "13") {
	slot = "12";
    }
    $(".search-slot").val(slot).trigger('change');
}