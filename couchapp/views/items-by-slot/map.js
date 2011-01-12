function(doc) {
    if (doc.type == "item") {
	var slot = "0";
	switch(parseInt(doc.invType)) {
	    case 1:
	    case 2:
	    case 3:
	    case 4:
	    case 6:
	    case 7:
	    case 8:
	    case 9:
	    case 10:
	    case 11:
	    case 19:
	    slot = String(parseInt(doc.invType)-1);
	    break;
	    case 12: // trinkets
	    slot = "12";
	    break;
	    case 16: // back
	    slot = "14";   
	    break;
	    case 5: // chest
	    case 20: // robe
	    slot = "4";
	    break;
	    case 17: // two hand
	    case 21: // main hand
	    slot = "15";
	    break;
	    case 14: // shield
	    case 22: // off-hand
	    case 23: // held-in-off
	    slot = "16";
	    break;
	    case 15:
	    case 25:
	    case 26:
	    case 28: // relic
	    slot = "17";
	    break;
	    case 13: // one-hand "special!"
	    slot = "one-hand";
	    break;
	}
	if (slot == "one-hand") { // for one-hand
	    emit(["15", parseInt(doc.itemLevel), "13", doc.classId, 
		  doc.subclassId], doc);
	    emit(["16", parseInt(doc.itemLevel), "13", doc.classId, 
		  doc.subclassId], doc);
	} else {
	    emit([slot, parseInt(doc.itemLevel), doc.invType, doc.classId, 
		  doc.subclassId], doc);
	}
    }
}