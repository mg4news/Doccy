package org.mg4news.Doccy

import org.mongodb.scala.MongoCollection

// Users object. conceals the details of the collection
// A User is is an instance of the NameDescSchema
object Users extends NameDescSchema {
  val COLL_NAME: String = COLLECTION_USERS
  val collection: MongoCollection[DocNameDesc] = DB.getDb.getCollection(COLL_NAME)
}
