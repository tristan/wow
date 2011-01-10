function(doc) {
  if (doc.type == "item" && doc.name != undefined && doc.classId == "2")
    emit(doc.id, doc);
}