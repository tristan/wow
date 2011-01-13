function(e, itemid,a) {
    $.log("getItem", itemid, a);
    $(this).trigger('item', itemid);
}