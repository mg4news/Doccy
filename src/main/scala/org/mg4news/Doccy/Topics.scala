package org.mg4news.Doccy

import org.mongodb.scala.MongoCollection
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Updates.set
import org.mg4news.Doccy.Helpers._

// Document for topic, i.e. topics used to categorise what subjects an issued document
// deals with. Each topic has:
// - the topic title
// - a description (defaults to "None")
object DocTopic {
  def apply(topic: String, description: String = "None"): DocTopic =
    DocTopic(new ObjectId(), topic, description)
}
case class DocTopic(_id: ObjectId, topic: String, description: String)

// Topics object. conceals the details of the collection
// All of the functions work in terms of DocXXX, there is no mapping to different representations
// That neeeds to be done by the caller.
object Topics extends Schema[DocTopic]{
  val COLL_NAME: String = COLLECTION_TOPICS
  val collection: MongoCollection[DocTopic] = DB.getDb.getCollection(COLL_NAME)
  val KEY_FIELD: String = "topic"

  // Checks for the presence of the topic (not description) in the collection
  def contains(value: String): Boolean = containsKeyField[DocTopic](value)

  // Finds a topic::description option taking only the value of the topic:
  // - Some(topic,description) if the topic exists
  // - None if the topic does not exist
  def find(topic: String): Option[(String,String)] = findByKeyField[DocTopic](topic) match {
    case h::_ => Some((h.topic,h.description))
    case _    => None
  }

  // For debug, show the whole collection
  def show():Unit = show[DocTopic]

  // Gets a list of all the topics by topic and description
  def getAll: Seq[DocTopic] = getAllDocs[DocTopic]

  // Add a set of topics to the collection
  // This walks though one by one and checks for duplicates based on the topic, not the description
  // If the topic exists then the description is simply updated
  def add(keys: Seq[(String, String)]): Unit = {
    // iterate over sequence of key value pairs
    for (kvp <- keys) {
      if (contains(kvp._1)) {
        collection.updateOne(equal(KEY_FIELD, kvp._1), set("description", kvp._2)).printHeadResult("Update Result: ")
      } else {
        collection.insertOne(DocTopic(kvp._1, kvp._2)).printHeadResult("Add Result: ")
      }
    }
  }

  // Deletes a topic document from the database based SOLELY on the topic, not the description
  def del(topic: String):Unit = if (contains(topic)) {
    collection.deleteOne(equal(KEY_FIELD, topic)).printHeadResult("Delete Result: ")
  }

}
