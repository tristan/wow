function() {
  $.log($(this).attr('href'));
  $(this).trigger('topping', $(this).attr('href'));
}
