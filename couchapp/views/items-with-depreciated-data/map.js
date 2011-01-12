function(doc) {
    if (doc.type == "item" && doc.slot != undefined) {
	emit([doc.id, "has slot field still"], doc);	
    } else if (doc.type == "item" && isNaN(parseInt(doc.invType))) {
	emit([doc.id, "invalid invType"], doc);
    } else if (doc.type == "item" && doc.invType == undefined) {
	emit([doc.id, "null invtype"], doc);
    }
}