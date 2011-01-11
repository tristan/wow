function(evt) {
    // search/_init/selectors/input.search-itemlevel/keyup.js
    var search = $(this).val();
    $(this).trigger('buildSearchQuery');
}