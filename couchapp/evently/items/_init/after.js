function(data) {
    var id = $(this).attr("id");
    switch(id) {
	case "main-hand":
	$(this).children(".slot").html("Main Hand");
	break;
	case "off-hand":
	$(this).children(".slot").html("Off Hand");
	break;
	case "ranged":
	$(this).children(".slot").html("Ranged/Relic");
	break;
	case "head":
	$(this).children(".slot").html("Head");
	break;
	case "neck":
	$(this).children(".slot").html("Neck");
	break;
	case "sholders":
	$(this).children(".slot").html("Sholders");
	break;
	case "back":
	$(this).children(".slot").html("Back");
	break;
	case "chest":
	$(this).children(".slot").html("Chest");
	break;
	case "wrists":
	$(this).children(".slot").html("Wrists");
	break;
	case "hands":
	$(this).children(".slot").html("Hands");
	break;
	case "waist":
	$(this).children(".slot").html("Waist");
	break;
	case "legs":
	$(this).children(".slot").html("Legs");
	break;
	case "feet":
	$(this).children(".slot").html("Feet");
	break;
	case "ring1":
	case "ring2":
	$(this).children(".slot").html("Ring");
	break;
	case "trinket1":
	case "trinket2":
	$(this).children(".slot").html("Trinket");
	break;
    }
}