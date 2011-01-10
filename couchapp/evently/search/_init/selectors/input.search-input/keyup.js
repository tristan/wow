function(evt) {
    var search = $(this).val();
    if (search.length > 3) {
	$(this).trigger('search', search);
    }
}