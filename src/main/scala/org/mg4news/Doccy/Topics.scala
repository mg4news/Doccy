package org.mg4news.Doccy

import org.mongodb.scala.MongoCollection

// Topics object. conceals the details of the collection
// A Topic is is an instance of the NameDescSchema
object Topics extends NameDescSchema {
  val COLL_NAME: String = COLLECTION_TOPICS
  val collection: MongoCollection[DocNameDesc] = DB.getDb.getCollection(COLL_NAME)
}
