function(doc) {
  if (doc.type == "item" && doc.name != undefined)
    emit(doc.name, doc);
}