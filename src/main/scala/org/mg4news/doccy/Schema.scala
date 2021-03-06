// ==============================================================================================
// This is free and unencumbered software released into the public domain.
//
// Anyone is free to copy, modify, publish, use, compile, sell, or distribute this software,
// either in source code form  or as a compiled binary, for any purpose, commercial or
// non-commercial, and by any means.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
// BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
// CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//
// This is a simplified version of UNLICENSE.
// For more information, please refer to <http://unlicense.org/>
// ==============================================================================================
package org.mg4news.doccy

import org.mongodb.scala.MongoCollection
import org.mongodb.scala.model.Filters.equal
import scala.reflect.ClassTag
import Helpers._

/**
 * Base trait to generalise some of the logic for each document. It may well be a massive intellectual snark hunt,
 * especially given the classtag hoops I had to jump through to make some of this work. But..
 * - I learned about classtags and type erasure
 * - there is some reusability
 * - I feel good about at least giving a shit..
 * @tparam T Any class type that works as a Mongo document
 */
trait Schema[T] {
  val collection: MongoCollection[T]
  val COLL_NAME: String
  val KEY_FIELD: String

  /**
   * Counts the number of elements in the collection
   * @return
   */
  def number: Long = collection.countDocuments().result()

  /**
   * Kill the entire collection
   */
  def destroy(): Unit = collection.drop().printResults()

  /**
   * Gets all the documents in the colelction
   * @param t class tag (IMPLICIT!!)
   * @tparam T class type
   * @return Sequence of objects of type T
   */
  def getAllDocs[T](implicit t:ClassTag[T]): Seq[T] =
    collection.find[T]().results()

  // Find documents in a collection by an arbitrary field value
  // Does NOT stop after the first result. This does not assume the key field
  def findByField[T](field: String, value: String)(implicit t: ClassTag[T]): Seq[T] =
    collection.find[T](equal(field, value)).results()

  // Find a document by key field value
  // Given that the key field is unique, this stops after the first result
  def findByKeyField[T](value: String)(implicit t: ClassTag[T]): Seq[T] =
    collection.find[T](equal(KEY_FIELD, value)).first().results()

  // Determine if at least one document exists in a collection using an arbitrary field value
  def containsField[T](field: String, value: String)(implicit t: ClassTag[T]): Boolean =
    collection.find[T](equal(field, value)).first().results() match {
      case _::_ => true
      case _ => false
    }

  // Determine if a document exists in the collection using only the key field as parameter
  // Specific case of containsField
  def containsKeyField[T](value: String)(implicit t: ClassTag[T]): Boolean =
    containsField[T](KEY_FIELD, value)

  // Delete the first document where the specified filed has the specified value
  def delOneByField[T](field: String, value: String)(implicit t: ClassTag[T]): Unit =
    if (containsField[T](KEY_FIELD, value)) {
      collection.deleteOne(equal(KEY_FIELD, value)).printHeadResult("Delete Result: ")
    }

  // Delete the first document matching the key field value
  def delOneByKeyField[T](value: String)(implicit t: ClassTag[T]): Unit =
    delOneByField[T](KEY_FIELD, value)

  // For debug, show the whole collection
  def show[T](implicit t:ClassTag[T]): Unit = {
    println(s"Contents of collection $COLL_NAME =")
    getAllDocs[T].foreach(println)
  }


}



