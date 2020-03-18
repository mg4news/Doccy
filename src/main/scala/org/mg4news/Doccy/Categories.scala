package org.mg4news.Doccy

import org.mongodb.scala.MongoCollection

// Categories object, conceals the details of the collection
// A Category is an instance of the NameDescSchema
object Categories extends NameDescSchema {
  val COLL_NAME: String = COLLECTION_TOPICS
  val collection: MongoCollection[DocNameDesc] = DB.getDb.getCollection(COLL_NAME)
}



