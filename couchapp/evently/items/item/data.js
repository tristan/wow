function(data, evt, req) {
    //$.log("called data", req);
    if (data.rows.length > 0) {
	var item = data.rows[0].value;
	item.slot = $(this).children("td.slot").html();
	if (item.socketBonus != null) { //TODO: socket bonus processing here is ugly
	    var sb = [];
	    if (item.socketBonus.bonusStamina != null) {
		sb.push("+" + item.socketBonus.bonusStamina + " Stamina");
	    }
	    if (item.socketBonus.bonusStrength != null) {
		sb.push("+" + item.socketBonus.bonusStrength + " Strength");
	    }
	    if (item.socketBonus.bonusAgility != null) {
		sb.push("+" + item.socketBonus.bonusAgility + " Agility");
	    }
	    if (item.socketBonus.bonusIntellect != null) {
		sb.push("+" + item.socketBonus.bonusIntellect + " Intellect");
	    }
	    if (item.socketBonus.bonusSpirit != null) {
		sb.push("+" + item.socketBonus.bonusSpirit + " Spirit");
	    }
	    if (item.socketBonus.bonusMasteryRating != null) {
		sb.push("+" + item.socketBonus.bonusMasteryRating + " Mastery");
	    }
	    if (item.socketBonus.bonusCritRating != null) {
		sb.push("+" + item.socketBonus.bonusCritRating + " Crit");
	    }
	    if (item.socketBonus.bonusHasteRating != null) {
		sb.push("+" + item.socketBonus.bonusHasteRating + " Haste");
	    }
	    if (item.socketBonus.bonusHitRating != null) {
		sb.push("+" + item.socketBonus.bonusHitRating + " Hit");
	    }
	    if (item.socketBonus.bonusExpertiseRating != null) {
		sb.push("+" + item.socketBonus.bonusExpertiseRating + " Expertise");
	    }
	    if (item.socketBonus.bonusParryRating != null) {
		sb.push("+" + item.socketBonus.bonusParryRating + " Parry");
	    }
	    if (item.socketBonus.bonusDodgeRating != null) {
		sb.push("+" + item.socketBonus.bonusDodgeRating + " Dodge");
	    }
	    if (item.socketBonus.bonusResilienceRating != null) {
		sb.push("+" + item.socketBonus.bonusResilienceRating + " Resilience");
	    }
	    if (item.socketBonus.bonusSpellPower != null) {
		sb.push("+" + item.socketBonus.bonusSpellPower + " Spell Power");
	    }
	    item.socketBonus = sb.join("");
	}
	return item;
    } else {
	var itemid = req;
	if (typeof(req) == "object") {
	    itemid = req.id;
	}
	$.log("didn't find: " + itemid);
	var ireq = {
	    id: itemid,
	    type: "item",
	    _id: "item-" + itemid
	};
	$$(this).app.db.saveDoc(ireq, {
				    success : function() {
					$.log("success");
				    },
				    error : function(e) {
					if (e == 409) {
					    $.log("item already requested");
					}
				    }
				});
	return {
	    name: false,
	    id: itemid
	};
    }
}