function(event) {
    $.log("evently/slot/empty/selectors/td.item>a/click.js", event, $(this));
    $(".item-search").trigger("showItemSearch", $(this).parent().parent().attr("slot"));
}
