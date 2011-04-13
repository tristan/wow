
function load(scripts) {
  for (var i=0; i < scripts.length; i++) {
    document.write('<script src="'+scripts[i]+'"><\/script>');
  };
};

load(["js/jquery-1.5.2.min.js",
      "js/jLog.min.js"]);