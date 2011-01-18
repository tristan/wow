reforgeIds = {
    bonusSpirit: {
	bonusDodgeRating: 113,
	bonusParryRating: 114,
	bonusHitRating: 115,
	bonusCritRating: 116,
	bonusHasteRating: 117,
	bonusExpertiseRating: 119,
	bonusMasteryRating: 119
    },
    bonusDodgeRating: {
	bonusSpirit: 120,
	bonusParryRating: 121,
	bonusHitRating: 122,
	bonusCritRating: 123,
	bonusHasteRating: 124,
	bonusExpertiseRating: 125,
	bonusMasteryRating: 126
    },
    bonusParryRating: {
	bonusSpirit: 127,
	bonusDodgeRating: 128,
	bonusHitRating: 129,
	bonusCritRating: 130,
	bonusHasteRating: 131,
	bonusExpertiseRating: 132,
	bonusMasteryRating: 133
    },
    bonusHitRating: {
	bonusSpirit: 134,
	bonusDodgeRating: 135,
	bonusParryRating: 136,
	bonusCritRating: 137,
	bonusHasteRating: 138,
	bonusExpertiseRating: 139,
	bonusMasteryRating: 140
    },
    bonusCritRating: {
	bonusSpirit: 141,
	bonusDodgeRating: 142,
	bonusParryRating: 143,
	bonusHitRating: 144,
	bonusHasteRating: 145,
	bonusExpertiseRating: 146,
	bonusMasteryRating: 147
    },
    bonusHasteRating: {
	bonusSpirit: 148,
	bonusDodgeRating: 149,
	bonusParryRating: 150,
	bonusHitRating: 151,
	bonusCritRating: 152,
	bonusExpertiseRating: 153,
	bonusMasteryRating: 154
    },
    bonusExpertiseRating: {
	bonusSpirit: 155,
	bonusDodgeRating: 156,
	bonusParryRating: 157,
	bonusHitRating: 158,
	bonusCritRating: 159,
	bonusHasteRating: 160,
	bonusMasteryRating: 161
    },
    bonusMasteryRating: {
	bonusSpirit: 162,
	bonusDodgeRating: 163,
	bonusParryRating: 164,
	bonusHitRating: 165,
	bonusCritRating: 166,
	bonusHasteRating: 167,
	bonusExpertiseRating: 168
    }
};
reforgeIdsReverse = {
    113: ["bonusSpirit", "bonusDodgeRating"],
    114: ["bonusSpirit", "bonusParryRating"],
    115: ["bonusSpirit", "bonusHitRating"],
    116: ["bonusSpirit", "bonusCritRating"],
    117: ["bonusSpirit", "bonusHasteRating"],
    118: ["bonusSpirit", "bonusExpertiseRating"],
    119: ["bonusSpirit", "bonusMasteryRating"],
    120: ["bonusDodgeRating", "bonusSpirit"],
    121: ["bonusDodgeRating", "bonusParryRating"],
    122: ["bonusDodgeRating", "bonusHitRating"],
    123: ["bonusDodgeRating", "bonusCritRating"],
    124: ["bonusDodgeRating", "bonusHasteRating"],
    125: ["bonusDodgeRating", "bonusExpertiseRating"],
    126: ["bonusDodgeRating", "bonusMasteryRating"],
    127: ["bonusParryRating", "bonusSpirit"],
    128: ["bonusParryRating", "bonusDodgeRating"],
    129: ["bonusParryRating", "bonusHitRating"],
    130: ["bonusParryRating", "bonusCritRating"],
    131: ["bonusParryRating", "bonusHasteRating"],
    132: ["bonusParryRating", "bonusExpertiseRating"],
    133: ["bonusParryRating", "bonusMasteryRating"],
    134: ["bonusHitRating", "bonusSpirit"],
    135: ["bonusHitRating", "bonusDodgeRating"],
    136: ["bonusHitRating", "bonusParryRating"],
    137: ["bonusHitRating", "bonusCritRating"],
    138: ["bonusHitRating", "bonusHasteRating"],
    139: ["bonusHitRating", "bonusExpertiseRating"],
    140: ["bonusHitRating", "bonusMasteryRating"],
    141: ["bonusCritRating", "bonusSpirit"],
    142: ["bonusCritRating", "bonusDodgeRating"],
    143: ["bonusCritRating", "bonusParryRating"],
    144: ["bonusCritRating", "bonusHitRating"],
    145: ["bonusCritRating", "bonusHasteRating"],
    146: ["bonusCritRating", "bonusExpertiseRating"],
    147: ["bonusCritRating", "bonusMasteryRating"],
    148: ["bonusHasteRating", "bonusSpirit"],
    149: ["bonusHasteRating", "bonusDodgeRating"],
    150: ["bonusHasteRating", "bonusParryRating"],
    151: ["bonusHasteRating", "bonusHitRating"],
    152: ["bonusHasteRating", "bonusCritRating"],
    153: ["bonusHasteRating", "bonusExpertiseRating"],
    154: ["bonusHasteRating", "bonusMasteryRating"],
    155: ["bonusExpertiseRating", "bonusSpirit"],
    156: ["bonusExpertiseRating", "bonusDodgeRating"],
    157: ["bonusExpertiseRating", "bonusParryRating"],
    158: ["bonusExpertiseRating", "bonusHitRating"],
    159: ["bonusExpertiseRating", "bonusCritRating"],
    160: ["bonusExpertiseRating", "bonusHasteRating"],
    161: ["bonusExpertiseRating", "bonusMasteryRating"],
    162: ["bonusMasteryRating", "bonusSpirit"],
    163: ["bonusMasteryRating", "bonusDodgeRating"],
    164: ["bonusMasteryRating", "bonusParryRating"],
    165: ["bonusMasteryRating", "bonusHitRating"],
    166: ["bonusMasteryRating", "bonusCritRating"],
    167: ["bonusMasteryRating", "bonusHasteRating"],
    168: ["bonusMasteryRating", "bonusExpertiseRating"]
};

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
	raw = raw.split("&");
	var data = {id: null};
	for (var i = 0; i < raw.length; i++) {
	    var s = raw[i].split("=");
	    if (s.length == 2) {
		var key = s[0];
		var val = s[1];
		if (key == "i") {
		    key = "id";
		}
		data[key] = val;
	    }
	}
	return data;
    },
    setItemData: function(slot, data) {
	var olddata = this.getItemData(slot);
	var newdata = $.extend(olddata, data);
	var item = [];
	for (key in newdata) {
	    if (key == "id") {
		item.push("i=" + newdata[key]);
	    } else {
		item.push(key + "=" + newdata[key]);
	    }
	}
	var state = this.getState();
	state[parseInt(slot)] = item.join("&");
	this.setState(state);
    },
    addItem: function(slot, item) {
	$.log("sm", slot, item);
	var hash = this.getState();
	$.log(hash);
	var data = "i=" + item.id;
	hash[slot] = data;
	location.hash = hash.join("|");
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
    },
    loadState: function() {
	//$.log("loading state");
	if (location.hash != "") {
	    for (var slot = 0; slot < 18; slot++) {
		var data = this.getItemData(slot);
		//$.log(slot, data);
		if (data.id != null) {
		    // trigger items replacement
		    $('tr.slot[slot="' + slot + '"]').trigger('item', data);
		}
	    }
	}
    }
}