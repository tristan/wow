function(doc) {
  if (doc.type == "item" && doc.id != undefined)
    emit([doc.classId, doc.subclassId, doc.invType, parseInt(doc.itemLevel)], doc);
}