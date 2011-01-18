function(data,elem,req) {
    // items/gem/after.js
    //$.log("gems>after", data);
    var gem = data.rows[0].value;
    var html = [];
    if (gem.bonusStamina != null) {
	html.push("Stamina: +" + gem.bonusStamina);
    }
    if (gem.bonusStrength != null) {
	html.push("Strength: +" + gem.bonusStrength);
    }
    if (gem.bonusAgility != null) {
	html.push("Agility: +" + gem.bonusAgility);
    }
    if (gem.bonusIntellect != null) {
	html.push("Intellect: +" + gem.bonusIntellect);
    }
    if (gem.bonusSpirit != null) {
	html.push("Spirit: +" + gem.bonusSpirit);
    }
    if (gem.bonusMasteryRating != null) {
	html.push("Mastery: +" + gem.bonusMasteryRating);
    }
    if (gem.bonusCritRating != null) {
	html.push("Crit: +" + gem.bonusCritRating);
    }
    if (gem.bonusHasteRating != null) {
	html.push("Haste: +" + gem.bonusHasteRating);
    }
    if (gem.bonusHitRating != null) {
	html.push("Hit: +" + gem.bonusHitRating);
    }
    if (gem.bonusExpertiseRating != null) {
	html.push("Expertise: +" + gem.bonusExpertiseRating);
    }
    if (gem.bonusParryRating != null) {
	html.push("Parry: +" + gem.bonusParryRating);
    }
    if (gem.bonusDodgeRating != null) {
	html.push("Dodge: +" + gem.bonusDodgeRating);
    }
    if (gem.bonusResilienceRating != null) {
	html.push("Resilience: +" + gem.bonusResilienceRating);
    }
    if (gem.bonusSpellPower != null) {
	html.push("Spell Power: +" + gem.bonusSpellPower);
    }
    if (gem.miscStats != null) {
	html = html.concat(gem.miscStats);
    }
    var gemsdiv = $(this).children(".gems");
    var socketdiv = gemsdiv.children(":nth-child(" + (parseInt(req.socket)+1) + ")");
    var a = socketdiv.find("a.socket");
    //$.log($(this), gemsdiv, socketdiv, a);
    var oldsubclassId = a.attr("class").search(/gem-subclassId-[\d]+/);
    if (oldsubclassId >= 0) {
	a.removeClass(a.attr("class").substring(oldsubclassId));
    }
    a.html(html.join(" ")).addClass("gem-subclassId-" + gem.subclassId);
    a.attr("gem-id", String(req.id));
    // set gems in location bar
    var gems = [];
    a.parent().parent()
	.find("a.socket")
	.each(function(index, element) {
		  if ($(element).attr("gem-id") == undefined) {
		      gems.push("");
		  } else {
		      gems.push($(element).attr("gem-id"));
		  }
	      });
    StateManager.setGems(req.slot, gems);

}