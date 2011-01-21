function(doc, req) {
    if (doc.type == "item" && doc.name == undefined) {
	return true;
    } else if (doc.type == "character" && doc.requested) {
	return true;
    } else {
	return false;
    }
}