
function state_str_to_map(str) {
    var data = {};
    var r = str.split("&");
    for (var i = 0; i < r.length; i++) {
	var s = r[i].split("=");
	if (s.length == 2) {
	    var key = s[0];
	    var val = s[1];
	    data[key] = val;
	}
    }
    return data;	
}

StateManager = {
    __set__: function() { 
	var s = [];
	for (var i = 0; i < 18; i++) {
	    s.push({});
	}
	return s;
    }(),
    getKeys: function() {
	var hash = ["","","","","","","","","","","","","","","","","",""];
	if (location.hash != "") {
	    var th = location.hash.substr(1).split("|");
	    if (th.length == 18)
		hash = th;
	}
	return hash;
    },
    setKeys: function(keys) {
	if (keys.length != 18) {
	    throw new Exception("invalid keys list");
	} else {
	    location.hash = keys.join("|");
	}
    },
    getKey: function(slot) {
	return this.getKeys()[parseInt(slot)];
    },
    setKey: function(slot, key) {
	var keys = this.getKeys();
	keys[parseInt(slot)] = key;
	this.setKeys(keys);
    },
    setItem: function(slot, item) {
	slot = parseInt(slot);
	this.__set__[slot].item = item;
	var key = this.getKey(slot);
	if (key.match(/i=\d+/)) {
	    this.setKey(slot, key.replace(/i=\d+/, "i=" + item.id));
	} else {
	    this.setKey(slot, "i=" + item.id + (key == "" ? "" : "&") + key);
	}
    },
    setReforge: function(slot, f, t) {
	slot = parseInt(slot);
	var re = null;
	if (f == undefined) { // remove reforge
	    this.__set__[slot].reforge = null;
	    var key = this.getKey(slot);
	    if (key.match(/&re=\d+/)) {
		this.setKey(slot, key.replace(/&re=\d+/, ""));
	    } else if (key.match(/re=\d+&/)) {
		this.setKey(slot, key.replace(/re=\d+&/, ""));
	    } else if (key.match(/^re=\d+$/)) {
		this.setKey(slot, "");
	    }
	    return;
	} else if (t == undefined && !isNaN(parseInt(f))) {
	    f = parseInt(f);
	    if (reforgeIdsReverse[f] != null) {
		re = f;
	    }
	} else {
	    re = reforgeIds[f];
	    if (re != null) {
		re = re[t];
	    }
	}
	if (re == null) {
	    throw new Exception("invalid reforge: " + f + " to " + t);
	} else {
	    this.__set__[slot].reforge = re;
	    var key = this.getKey(slot);
	    if (key.match(/re=\d+/)) {
		this.setKey(slot, key.replace(/re=\d+/, "re=" + re));
	    } else {
		this.setKey(slot, key + (key == "" ? "" : "&") + "re=" + re);
	    }
	}
    },
    setGems: function(slot, gems) {
	var key = this.getKey(slot);
	var sgems = this.__set__[slot].gems || [];
	for (k in gems) {
	    var rx = new RegExp(k + "=\\d+");
	    var g = key.match(rx);
	    if (g != null) {
		key = key.replace(rx, k + "=" + gems[k].id);
	    } else {
		key += (key == "" ? "" : "&") + k + "=" + gems[k].id;
	    }
	    var socket = k.match(/^g(\d+)$/);
	    if (socket.length > 1) {
		socket = socket[1];
		sgems[socket] = gems[k];
	    }
	}
	this.__set__[slot].gems = sgems;
	this.setKey(slot, key);
    },
    setEnchant: function(slot, enchant) {
    },
    getStats: function() {
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
    }
};