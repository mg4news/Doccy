package org.mg4news.Doccy

import org.mongodb.scala.MongoCollection

// Projects object. conceals the details of the collection
// A Project is is an instance of the NameDescSchema
object Projects extends NameDescSchema {
  val COLL_NAME: String = COLLECTION_PROJ
  val collection: MongoCollection[DocNameDesc] = DB.getDb.getCollection(COLL_NAME)
}
