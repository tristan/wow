function merge_stats(m1, m2) {
    var r = $.extend(true, {}, m1);
    for (k in m2) {
	if (typeof(m2[k]) == "number") {
	    if (r[k] != undefined) {
		r[k] += m2[k];
	    } else {
		r[k] = m2[k];
	    }
	}
    }
    return r;
}

function overall_item_stats(item, reforge, gems, enchant) {
    if (item == null) {
	return {};
    }
    gems = gems || [];
    item.sockets = item.sockets || [];
    item.socketBonus = item.socketBonus || {};

    var result = $.extend(true, {}, item);
    if (reforge != null) {	
	var re = reforgeIdsReverse[reforge];
	if (re != null) {
	    var re_val = parseInt(item[re[0]] * 0.4);
	    $.log(re_val, re[0], result[re[0]], re[1], result[re[1]]);
	    result[re[0]] = (result[re[0]] || 0) + re_val;
	    result[re[1]] = (result[re[1]] || 0) + re_val;
	}
    }
    var socket_match = true;
    for (var i = 0; i < item.sockets.length; i++) {
	var colour = item.sockets[i];
	if (gems[i] == undefined) {
	    socket_match = false;
	} else {
	    if (!((colour == "Blue" &&
		(gems[i].subclassId == "1" ||
		 gems[i].subclassId == "3" ||
		 gems[i].subclassId == "4"))
		|| (colour == "Yellow" &&
		    (gems[i].subclassId == "2" ||
		     gems[i].subclassId == "4" ||
		     gems[i].subclassId == "5"))
		|| (colour == "Red" &&
		    (gems[i].subclassId == "0" ||
		     gems[i].subclassId == "3" ||
		     gems[i].subclassId == "5"))
		|| (colour == "Prismatic")
		|| (colour == "Meta" && gem[i].subclassId == "6")
		|| (colour == "Cogwheel" && gem[i].subclassId == "10")
		// TODO: hydraulic simple
		 ))
	    { // if none of the above return true, the socket isn't matched
		socket_match = false;
	    }
	    
	    result = merge_stats(result, gems[i]);
	}
    }
    if (socket_match) {
	result = merge_stats(result, item.socketBonus);
    }
    // TODO: enchant
    return result;
}