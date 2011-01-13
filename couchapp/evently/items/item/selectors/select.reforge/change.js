function() {
    var slot = $(this).parent().parent().attr("slot");
    var from = "none";
    var to = "none"; // = $(".rft").val();
    var self = $(this).attr("class").split(/\s+/)[1];
    if (self == "rff") {
	from = $(this).val();
	to = $(this).parent().siblings(".reforge-to").children(".rft").val();
    } else {
	to = $(this).val();
	from = $(this).parent().siblings(".reforge-from").children(".rff").val();
    }
    if (from != "none" && to != "none") {
	StateManager.setReforge(slot, from, to);
    }
}