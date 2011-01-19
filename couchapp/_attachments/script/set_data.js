function merge_stats(m1, m2) {
    var r = m1;
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
    item.sockets = item.sockets || [];
    var result = item;
    var re = reforgeIdsReverse[reforge];
    if (re != null) {
	var re_val = parseInt(item[re[0]] * 0.4);
	result[re[0]] -= re_val;
	result[re[1]] += re_val;
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
		|| (color == "Yellow" &&
		    (gems[i].subclassId == "2" ||
		     gems[i].subclassId == "4" ||
		     gems[i].subclassId == "5"))
		|| (color == "Red" &&
		    (gems[i].subclassId == "0" ||
		     gems[i].subclassId == "3" ||
		     gems[i].subclassId == "5"))
		|| (color == "Prismatic")
		|| (color == "Meta" && gem[i].subclassId == "6")
		|| (color == "Cogwheel" && gem[i].subclassId == "10")
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

SetData = {
    __set__: {
	0: null,
	1: null,
	3: null,
	4: null,
	5: null,
	6: null,
	7: null,
	8: null,
	9: null,
	10: null,
	11: null,
	12: null,
	13: null,
	14: null,
	15: null,
	16: null,
	17: null
    },
    setItem: function(slot, item, reforge, gems, enchant) {
	$.log("setItem", slot, item, reforge, gems, enchant);
	slot = parseInt(slot);
	var oldItem = this.__set__[slot];
	if (item.item != undefined) {
	    this.__set__[slot] = item;
	} else if (item.id != undefined) {
	    this.__set__[slot] = {
		item: item,
		reforge: reforge,
		gems: gems || [],
		enchant: enchant
	    };
	} else {
	    throw new Exception("invalid input to setItem");
	}
	// set location bar
	var data = {
	    i: this.__set__[slot].item.id,
	    re: this.__set__[slot].reforge,
	    e: this.__set__[slot].enchant.id
	};
	for (var i = 0; i < this.__set__[slot].gems.length; i++) {
	    data["g" + i] = this.__set__[slot].gems[i].id;
	}
	StateManager.setItemData(slot,
				 data);
    },
    getItem: function(slot) {
	return this.__set__[parseInt(slot)];
    },
    overallStats: function() {
	var stats = {};
	for (var s = 0; s < 18; s++) {
	    if (this.__set__[s] != null) {
		var slotstats = overall_item_stats(this.__set__[s].item,
						   this.__set__[s].reforge,
						   this.__set__[s].gems,
						   this.__set__[s].enchant);
		stats = merge_stats(stats, slotstats);
	    }
	}
	var result = {};
	for (k in stats) {
	    if (k.search(/^bonus\w+$/) == 0) {
		result[k] = stats[k];
	    }
	}
	return result;
    },
    loadSetFromState: function() {
	
    }
};