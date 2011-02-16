function(event) {
    $.log("evently/stats/update/data.js", event);
    var state = StateManager.getStats();
    var xhit = parseInt($(".extra-stats .x-hit").val()) || 0;
    var xexp = parseInt($(".extra-stats .x-exp").val()) || 0;
    state["bonusHitRating"] += xhit;
    state["bonusExpertiseRating"] += xexp;
    state["requiredHitRating"] = 1742 - state["bonusHitRating"];
    state["requiredExpertiseRating"] = 541 - state["bonusExpertiseRating"];
    return state;
}