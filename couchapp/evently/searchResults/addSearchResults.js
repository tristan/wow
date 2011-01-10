function(evt,items) {
    $.log("addSearchResults");
    $(this).trigger('results', [items]);
}