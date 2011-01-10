function(doc) {
  if (doc.type == "item" && doc.name != undefined && doc.classId == "4")
    emit(doc.id, doc);
}