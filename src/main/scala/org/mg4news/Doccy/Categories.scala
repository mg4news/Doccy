package org.mg4news.Doccy

import org.mongodb.scala.MongoCollection
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Updates.set
import Helpers._

// Schema for categories. Each document has a single category
// Categories have:
// - a name
// - a description (defaults to now)
object DocCat {
  def apply(name: String, description: String): DocCat =
    DocCat(new ObjectId(), name, description)
}
case class DocCat(_id: ObjectId, name: String, description: String = "None")

// Categories object, conceals the details of the collection
// All of the functions work in terms of DocXXX, there is no mapping to different representations
// That neeeds to be done by the caller.
object Categories extends Schema[DocCat] {
  val COLL_NAME: String = COLLECTION_TOPICS
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
  def show(): Unit = show[DocCat]

  // Gets a list of all the categories by name and description
  def getAll: Seq[DocCat] = getAllDocs[DocCat]

  // Add a set of categories to the collection
  // This walks though one by one and checks for duplicates based on the name, not the description
  // If the name exists then the description is simply updated
  def add(cats: Seq[(String, String)]): Unit = {
    // iterate over sequence of key value pairs
    for (c <- cats) {
      if (contains(c._1)) {
        collection.updateOne(equal(KEY_FIELD, c._1), set("description", c._2)).
          printHeadResult("Update Result: ")
      } else {
        collection.insertOne(DocCat(c._1, c._2)).printHeadResult("Add Result: ")
      }
    }
  }

  // Deletes a category document from the database based SOLELY on the name, not the description
  def del(name: String): Unit = delOneByKeyField[DocCat](name)

}



