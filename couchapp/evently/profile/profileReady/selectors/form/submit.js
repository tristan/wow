function() {
    $.log("sumbit");
  var form = $(this);
  var fdoc = form.serializeObject();
  $.ajax({
	     url: "http://us.wowarmory.com/character-sheet.xml&callback=?",
	     data: fdoc,
	     dataType: "jsonp",
	     beforeSend: function(xmlr) {
		 $.log("before send!");
		 $.log(xmlr);
	     },
	     sucess: function(data) {
		 fdoc.xml = data;
		 fdoc.profile = $$("#profile").profile;
		 $$(this).app.db.saveDoc(fdoc, {
					     success : function() {
						 form[0].reset();
						 form[1].reset();
					     }
					 });
	     }
	 });
  return false;
};
