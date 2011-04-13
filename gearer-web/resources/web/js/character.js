
function load_character() {
    var url = window.location.href;
    var char = url.substring(url.indexOf('/', 8)+1).split("/");
    // do ajax call to get slot info
    // for each slot, load the item
}