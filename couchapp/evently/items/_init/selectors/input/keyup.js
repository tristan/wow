function(evt) {
    if (evt.keyCode == 13) {
	$(this).trigger('getItem', $(this).val());
    }
}