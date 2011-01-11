function(evt,items) {
    $.log("addSearchResults");
    $.log(evt, items);
    $(this).trigger('results', [items]);
}