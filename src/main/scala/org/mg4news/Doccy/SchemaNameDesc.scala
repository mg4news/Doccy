//==============================================================================================
// This is free and unencumbered software released into the public domain.
//
// Anyone is free to copy, modify, publish, use, compile, sell, or distribute this software,
// either in source code form  or as a compiled binary, for any purpose, commercial or
// non-commercial, and by any means.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
// BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
// CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//
// This is a simplified version of UNLICENSE.
// For more information, please refer to <http://unlicense.org/>
//==============================================================================================
package org.mg4news.Doccy

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

// NameDescSchema trait. Used to derive a range of objects
// All of the functionality for documents of the type name::description is centralised here
trait NameDescSchema extends Schema[DocNameDesc] {
  val KEY_FIELD = "name"

  // Checks for the presence of the name (not description) in the collection
  def contains(value: String): Boolean = containsKeyField[DocNameDesc](value)

  // Finds a name::description option:
  // - Some(name,description) if the category exists
  // - None if the keyword does not exist
  def find(name: String): Option[(String,String)] = findByKeyField[DocNameDesc](name) match {
    case h::_ => Some((h.name,h.description))
    case _    => None
  }

  // For debug, show the whole collection
  def show(): Unit = show[DocNameDesc]

  // Gets a list of all the categories by name and description
  def getAll: Seq[(String,String)] = getAllDocs[DocNameDesc].map(c => (c.name, c.description))

  // Add a single name::desc to the collection
  // Checks for duplicates
  // If the name exists then the description is simply updated
  def addOne(name: String, description: String): Unit = {
    if (contains(name)) {
      collection.updateOne(equal(KEY_FIELD, name), set("description", description)).
        printHeadResult("Update Result: ")
    } else {
      collection.insertOne(DocNameDesc(name, description)).printHeadResult("Add Result: ")
    }
  }

  // Add a set of name::desc to the collection
  // This walks though one by one and checks for duplicates based on the name, not the description
  // If the name exists then the description is simply updated
  def add(docs: Seq[(String, String)]): Unit = for (d <- docs) addOne(d._1, d._2)

  // Deletes a category document from the database based SOLELY on the name, not the description
  def del(name: String): Unit = delOneByKeyField[DocNameDesc](name)
}

