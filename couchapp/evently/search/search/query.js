function(evt, key) {
    var valid_ilvls = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 99, 100, 101, 102, 103, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 117, 120, 123, 125, 126, 127, 128, 130, 132, 133, 134, 135, 136, 138, 141, 142, 143, 144, 145, 146, 150, 151, 152, 154, 155, 156, 158, 159, 162, 163, 164, 166, 167, 170, 171, 174, 175, 178, 179, 180, 182, 183, 187, 190, 200, 206, 210, 213, 219, 226, 232, 239, 245, 251, 258, 259, 264, 268, 270, 271, 272, 277, 278, 283, 284, 285, 288, 289, 292, 295, 296, 298, 300, 305, 306, 308, 312, 316, 317, 318, 325, 333, 339, 346, 350, 352, 353, 359, 365, 372, 379, 435, 813];
    $.log("searching for:", key);
    var from = parseInt(key.itemLevel.from);
    var to = parseInt(key.itemLevel.to);
    if (isNaN(from))
	from = 0;
    if (isNaN(to))
	to = 400; // just above current max ilvl
    var fromindex =-1;
    var toindex = -1;
    if (from > to) {
	to = from;
    }
    for (var i = 0; i < valid_ilvls.length; i++) {
	if (fromindex == -1 && valid_ilvls[i] >= from)
	    fromindex = i;
	if (valid_ilvls[i] > to) {
	    toindex = i;
	    break;
	}
    }
    valid_ilvls = valid_ilvls.slice(fromindex, toindex);
    var invTypes = key.invType.split(',');
    var classIds = key.classId.split(',');
    var subclassIds = ["0","1","2","3","4","5","6","7","8","9", // TODO: this...
		       "10","11","12","13","14","15","16","17","18","19","20"];
    var keys = [];
    for (var i = 0; i < valid_ilvls.length; i++) {
	for (var j = 0; j < invTypes.length; j++) {
	    for (var k = 0; k < classIds.length; k++) {
		for (var l = 0; l < subclassIds.length; l++) {
		    keys.push(
			[key.slot, valid_ilvls[i], invTypes[j], 
			 classIds[k], subclassIds[l]]
		    );
		}
	    }
	}
    }
    $.log(keys);
    return {
	view: "items-by-slot",
	keys: keys
    };
}