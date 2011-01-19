function(evt) {
    // search/_init/selectors/input.search-input/keyup.js
    var search = $(this).val();
    $(this).trigger('buildSearchQuery');
}