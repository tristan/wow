function(event) {
    $.log("evently/character/_init/selectors/input[name=load]/click.js",
	  $("div.load>select[name=domain]").val(),
	  $("div.load>input[name=realm]").val(),
	  $("div.load>input[name=character]").val());
    var domain = $("div.load>select[name=domain]").val();
    var realm = $("div.load>input[name=realm]").val();
    realm = realm.toLowerCase().replace(/'/, "");
    var character = $("div.load>input[name=character]").val();
    character = character.toLowerCase();
//    var c = $$(this).app.db.view('wow/characters', 
//		    {"include_docs": "true",
//		     "key":[domain, realm, character]});
//    $.log(c);
    $(this).trigger("load", [domain, realm, character]);
}