$("input[type='submit']").click(
    function() {
	//alert("clicked!");
});

$("form").submit(
    function(evt) {
	evt.target.action = 
	    evt.target.domain.value + "/" +
	    evt.target.realm.value + "/" +
	    evt.target.character.value + "/";
	$("form").children().remove();
	evt.target.elements = [];
    }
);