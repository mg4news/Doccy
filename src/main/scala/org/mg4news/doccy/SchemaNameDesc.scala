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

import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Updates.set
import Helpers._

// Schema for documents with two standard fields:
// - name
// - description (defaults to "None")
// This document is used for projects, topics, categories, etc.
object DocNameDesc {
  def apply(name: String, description: String): DocNameDesc =
    DocNameDesc(new ObjectId(), name, description)

}
case class DocNameDesc(_id: ObjectId, name: String, description: String = "None")

/**
 * NameDescSchema trait. Used to derive a range of object
 * All of the functionality for documents of the pattern (name::description) is centralised here
 */
trait NameDescSchema extends Schema[DocNameDesc] {
  val KEY_FIELD = "name"

  // Checks for the presence of the name (not description) in the collection
  /**
   * Checks for the presence of the name (not description) in the collection
   * @param name
   * @return true if the entry exists, else false
   */
  def contains(name: String): Boolean = containsKeyField[DocNameDesc](name)

  /**
   * Finds a name::description option by name in a collection
   * @param name Name string
   * @return Some(DocNameDesc) if the name exists
   * @return None if the name does not exist
   */
  def find(name: String): Option[DocNameDesc] = findByKeyField[DocNameDesc](name) match {
    case h::_ => Some(h)
    case _    => None
  }

  /**
   * For debug, shows the whole collection
   */
  def show(): Unit = show[DocNameDesc]

  /**
   * Gets a list of all the categories
   * @return A sequence of DocNameDesc objects
   */
  def getAll: Seq[DocNameDesc] = getAllDocs[DocNameDesc]

  /**
   * Adds a single name/description pair to the collection. Checks for duplicates.
   * If the "name" field exists, then the description field is updated. If the name
   * is not found in the collection, the new (name,description) is inserted
   * @param name Name string
   * @param description Descriptive string
   */
  def addOne(name: String, description: String): Unit = {
    if (contains(name)) {
      collection.updateOne(equal(KEY_FIELD, name), set("description", description)).
        printHeadResult("Update Result: ")
    } else {
      collection.insertOne(DocNameDesc(name, description)).printHeadResult("Add Result: ")
    }
  }

  /**
   * Bulk adds a sequence of (name,description) pairs to the collection. Uses the @see addOne method
   * to handle duplicate checking
   * @param docs A sequence of (name,description) pairs
   */
  def add(docs: Seq[(String, String)]): Unit = for (d <- docs) addOne(d._1, d._2)

  // Deletes a document from the database based SOLELY on the name, not the description

  /**
   * Deletes an element from the collection, based purely on the name field
   * @param name Name string, i.e. the "name" field
   */
  def del(name: String): Unit = delOneByKeyField[DocNameDesc](name)
}

