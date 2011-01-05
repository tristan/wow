function(doc) {
  if (doc.type == "item" && doc.id != undefined)
    emit(doc.id, doc);
}