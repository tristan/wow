function(doc) {
  if (doc.type == "item" && doc.name != undefined 
      && doc.classId == "2" && doc.invType == "One-Hand")
    emit(doc.id, doc);
}