function(doc) {
    if (doc.type == "character") {
	emit([doc.domain, doc.realm, doc.character], doc);
    }
}