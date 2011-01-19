
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
    getState: function() {
	var hash = ["","","","","","","","","","","","","","","","","",""];
	if (location.hash != "") {
	    var th = location.hash.substr(1).split("|");
	    if (th.length == 18)
		hash = th;
	}
	return hash;
    },
    setState: function(s) {
	if (s.length != 18) {
	    $.log("invalid state");
	} else {
	    location.hash = s.join("|");
	}
    },
    getItemData: function(slot) {
	var raw = this.getState()[parseInt(slot)];
	return state_str_to_map(raw);
    },
    setItem: function(slot, item, reforge, gems, enchant) {
	gems = gems || [];
	var olddata = this.getItemData(slot);
	var newdata = $.extend(olddata, data);
	var item = [];
	for (key in newdata) {
	    item.push(key + "=" + newdata[key]);
	}
	var state = this.getState();
	state[parseInt(slot)] = item.join("&");
	this.setState(state);
    },
    setReforge: function(slot, f, t) {
	var re = null;
	if (t == undefined && !isNaN(parseInt(f))) {
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
	    $.log("invalid reforge", f, t);
	} else {
	    this.setItemData(slot, {re: re});
	}
    },
    setGems: function(slot, gems) {
	var data = {};
	for (var i = 0; i < gems.length; i++) {
	    if (gems[i] != "") {
		data["g" + i] = gems[i];
	    }
	}
	this.setItemData(slot, data);
    },
    setEnchant: function(slot, enchant) {
    }
};