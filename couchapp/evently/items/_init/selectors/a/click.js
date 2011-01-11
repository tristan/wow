function(evt) {
    // items/_init/selectors/a/click.js
    $(".item-search").show();
    var id = $(this).parent().parent().attr("id");
    if (id == "ring1" || id == "ring2") {
	id = "ring";
    } else if (id == "trinket1" || id == "trinket2") {
	id = "trinket";
    }
    $(".search-invType").val(id).trigger('change');
}