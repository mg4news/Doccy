package com.mg4news.matcha

import org.mongodb.scala.MongoCollection
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Updates.set
import com.mg4news.matcha.Helpers._

// Document for categories. each cat has:
// - a category name
// - a detailed category description (defaults to now)
object DocCat {
  def apply(name: String, description: String = "None"): DocCat =
    DocCat(new ObjectId(), name, description)
}
case class DocCat(_id: ObjectId, name: String, description: String)

// Categories object, conceals the details of the collection
// All of the functions work in terms of DocXXX, there is no mapping to different representations
// That neeeds to be done by the caller.
object Categories extends Schema[DocCat] {
  val COLL_NAME: String = COLLECTION_WORDS
  val collection: MongoCollection[DocCat] = DB.getDb.getCollection(COLL_NAME)
  val KEY_FIELD: String = "name"

  // Checks for the presence of the name (not description) in the collection
  def contains(value: String): Boolean = containsKeyField[DocCat](value)

  // Finds a name::description option:
  // - Some(name,description) if the category exists
  // - None if the keyword does not exist
  def find(name: String): Option[(String,String)] = findByKeyField[DocCat](name) match {
    case h::_ => Some((h.name,h.description))
    case _    => None
  }

  // For debug, show the whole collection
  def show():Unit = show[DocCat]

  // Gets a list of all the categories by name and description
  def getAll: Seq[DocCat] = getAllDocs[DocCat]

  // Add a set of categories to the collection
  // This walks though one by one and checks for duplicates based on the name, not the description
  // If the name exists then the description is simply updated
  def add(cats: Seq[(String, String)]): Unit = {
    // iterate over sequence of key value pairs
    for (cat <- cats) {
      if (contains(cat._1)) {
        collection.updateOne(equal(KEY_FIELD, cat._1), set("description", cat._2)).
          printHeadResult("Update Result: ")
      } else {
        collection.insertOne(DocCat(cat._1, cat._2)).printHeadResult("Add Result: ")
      }
    }
  }

  // Deletes a category document from the database based SOLELY on the name, not the description
  def del(name: String): Unit = if (contains(name)) {
    collection.deleteOne(equal(KEY_FIELD, name)).printHeadResult("Delete Result: ")
  }

}



