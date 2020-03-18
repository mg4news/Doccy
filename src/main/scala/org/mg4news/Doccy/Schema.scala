package org.mg4news.Doccy

import org.mongodb.scala.{Completed, MongoCollection}
import com.mg4news.matcha.Helpers._
import org.mongodb.scala.model.Filters.equal

import scala.reflect.ClassTag

// Base trait to generalise some of the logic for each document. It may well be a massive intellectual snark hunt,
// especially given the classtag hoops I had to jump through to make some of this work. But..
// - I learned about classtags and type erasure
// - there is some reusability
// - I feel good about at least giving a shit..
trait Schema[T] {
  def collection: MongoCollection[T]
  def COLL_NAME: String
  def KEY_FIELD: String

  // Counts the number of elements in the collection
  def number: Long = collection.countDocuments().result()

  // Kill the entire collection
  def destroy(): Completed = collection.drop().headResult()

  // Gets all the documents in the colelction
  def getAllDocs[T](implicit t:ClassTag[T]): Seq[T] =
    collection.find[T]().results()

  // Find a document in a collection by an arbitrary field value
  def findByField[T](field: String, value: String)(implicit t: ClassTag[T]): Seq[T] =
    collection.find[T](equal(field, value)).first().results()

  // Find a document by key field value
  // Specific case of findByField
  def findByKeyField[T](value: String)(implicit t: ClassTag[T]): Seq[T] =
    findByField[T](KEY_FIELD, value)

  // Determine if a document exists in a collection using an arbitrary field value
  def containsField[T](field: String, value: String)(implicit t: ClassTag[T]): Boolean = findByField[T](field, value) match {
    case h::_ => true
    case _ => false
  }

  // Determine if a document exists in the collection using only the key field as parameter
  // Specific case of containsField
  def containsKeyField[T](value: String)(implicit t: ClassTag[T]): Boolean =
    containsField[T](KEY_FIELD, value)

  // Delete the first document where the specified filed has the specified value
  def delOneByField[T](field: String, value: String)(implicit t: ClassTag[T]): Unit =
    if (containsField[T](KEY_FIELD, value))
      collection.deleteOne(equal(KEY_FIELD, value)).printHeadResult("Delete Result: ")

  // Delete the first document matching the key field value
  def delOneByKeyField[T](value: String)(implicit t: ClassTag[T]): Unit =
    delOneByField[T](KEY_FIELD, value)

  // For debug, show the whole collection
  def show[T](implicit t:ClassTag[T]): Unit = {
    println(s"Contents of collection $COLL_NAME =")
    getAllDocs[T].foreach(println)
  }


}


