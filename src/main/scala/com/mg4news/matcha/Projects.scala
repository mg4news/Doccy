package com.mg4news.matcha

import org.mongodb.scala.MongoCollection
import org.mongodb.scala.bson.ObjectId

// Document for projects, projects have:
// - a project name
// - a project accronym
// - a detailed project description
object DocProj {
  def apply(name: String, accronym: String, description: String): DocProj =
    DocProj(new ObjectId(), name, accronym, description)
}
case class DocProj(_id: ObjectId, name: String, accronym: String, description: String)

object Projects extends Schema[DocProj] {
  val COLL_NAME: String = COLLECTION_PROJ
  val collection: MongoCollection[DocProj] = DB.getDb.getCollection(COLL_NAME)
  val KEY_FIELD: String = "name"

  // Checks for the presence of the name (not description) in the collection
  def contains(value: String): Boolean = findByField[DocProj](KEY_FIELD, value) match {
    case h :: _ => true
    case _ => false
  }

}
