function(evt) {
    // items/_init/selectors/a/click.js
    $(".item-search").show();
    var slot = $(this).parent().parent().attr("slot");
    if (slot == "11") {
	slot = "10";
    } else if (slot == "13") {
	slot = "12";
    }
    $(".search-slot").val(slot).trigger('change');
}