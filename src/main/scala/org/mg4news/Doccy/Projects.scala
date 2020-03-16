package org.mg4news.Doccy

import org.mongodb.scala.MongoCollection
import org.mongodb.scala.bson.ObjectId

// Document for projects, projects have:
// - a project name
// - a project TLA (three letter acronym)
// - a detailed project description
object DocProj {
  def apply(name: String, tla: String, description: String): DocProj =
    DocProj(new ObjectId(), name, tla, description)
}
case class DocProj(_id: ObjectId, name: String, tla: String, description: String)

object Projects extends Schema[DocProj] {
  val COLL_NAME: String = COLLECTION_PROJ
  val collection: MongoCollection[DocProj] = DB.getDb.getCollection(COLL_NAME)
  val KEY_FIELD: String = "name"

  // Checks for the presence of the name in the collection
  def contains(value: String): Boolean = containsKeyField[DocProj](value)

  // Checks for the presence of the name in the collection
  def containsTLA(value: String): Boolean = ???

  // Finds a project option by name (keyfield)
  // - Some(name,tla,description) if the project exists
  // - None if the keyword does not exist
  def find(name: String): Option[(String,String,String)] = findByKeyField[DocProj](name) match {
    case h::_ => Some((h.name,h.tla,h.description))
    case _    => None
  }

  // Finds a name::tla::description option by TLA
  // - Some(name,tla,description) if the project exists
  // - None if the keyword does not exist
  def findByTla(tla: String): Option[(String,String,String)] = findByField[DocProj]("tla", tla) match {
    case h::_ => Some((h.name, h.tla, h.description))
    case _    => None
  }
}
