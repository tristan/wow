function(evt, key) {
    $.log("searching for:", key);
    var from = parseInt(key.itemLevel.from);
    var to = parseInt(key.itemLevel.to);
    if (isNaN(from))
	from = 0;
    if (isNaN(to))
	to = 400; // just above current max ilvl
    return {
	view: "items-by-X",
	startkey: [key.classId, key.subclassId,
		   key.invType, from],
	endkey: [key.classId, key.subclassId,
		 key.invType, to]
    };
}