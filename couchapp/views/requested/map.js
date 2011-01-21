function(doc) {
  if ((doc.type == "item" || doc.type == "character")
      && doc.requested)
    emit(doc._id, doc);
}