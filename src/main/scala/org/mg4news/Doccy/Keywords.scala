package org.mg4news.Doccy

import org.mongodb.scala.MongoCollection
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.model.Filters.equal
import com.mg4news.matcha.Helpers._
import org.mongodb.scala.model.Updates.set

// Document for keyword, i.e. topic words used to categorise what subjects an issued document
// deals with. Each keyword has:
// - the word
// - a description (defaults to "None")
object DocWord {
  def apply(word: String, description: String = "None"): DocWord =
    DocWord(new ObjectId(), word, description)
}
case class DocWord(_id: ObjectId, word: String, description: String)

// Keywords object. conceals the details of the collection
// All of the functions work in terms of DocXXX, there is no mapping to different representations
// That neeeds to be done by the caller.
object Keywords extends Schema[DocWord]{
  val COLL_NAME: String = COLLECTION_WORDS
  val collection: MongoCollection[DocWord] = DB.getDb.getCollection(COLL_NAME)
  val KEY_FIELD: String = "word"

  // Checks for the presence of the keyword (not description) in the collection
  def contains(value: String): Boolean = containsKeyField[DocWord](value)

  // Finds a keyword::description option taking only the value of the keyword:
  // - Some(Keyword,description) if the keyword exists
  // - None if the keyword does not exist
  def find(word: String): Option[(String,String)] = findByKeyField[DocWord](word) match {
    case h::_ => Some((h.word,h.description))
    case _    => None
  }

  // For debug, show the whole collection
  def show():Unit = show[DocWord]

  // Gets a list of all the keywords by word and description
  def getAll: Seq[DocWord] = getAllDocs[DocWord]

  // Add a set of keywords to the collection
  // This walks though one by one and checks for duplicates based on the keyword, not the description
  // If the keyword exists then the description is simply updated
  def add(keys: Seq[(String, String)]): Unit = {
    // iterate over sequence of key value pairs
    for (kvp <- keys) {
      if (contains(kvp._1)) {
        collection.updateOne(equal(KEY_FIELD, kvp._1), set("description", kvp._2)).printHeadResult("Update Result: ")
      } else {
        collection.insertOne(DocWord(kvp._1, kvp._2)).printHeadResult("Add Result: ")
      }
    }
  }

  // Deletes a keyword document from the database based SOLELY on the keyword, not the description
  def del(word: String):Unit = if (contains(word)) {
    collection.deleteOne(equal(KEY_FIELD, word)).printHeadResult("Delete Result: ")
  }

}
