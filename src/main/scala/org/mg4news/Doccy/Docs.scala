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

import java.time.LocalDateTime
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Updates.set
import org.mongodb.scala.model.Updates.combine
import Helpers._

// Document for the actual issued document (recursive much?). An issued document has:
// - a name
// - a description
// - a project, TLA from DocProj
// - a category (because "type" is a keyword!) from DocCat
// - an author (name from User)
// - a list of topics from DocWord
// - an autogenerated creation timestamp
object DocDoc {
  def apply(
             name: String,
             description: String,
             author: String,
             proj: String,
             category: String,
             topics: Seq[String]): DocDoc =
    DocDoc(new ObjectId(), name, description, author, proj, category, topics, created=LocalDateTime.now())
}
case class DocDoc(
  _id: ObjectId,
  name: String,
  description: String,
  author: String,
  proj: String,
  category: String,
  topics: Seq[String],
  created: LocalDateTime)

// Object for the Docs document type. Most of the functions work in terms of the DocDoc case class type.
// This is because it is easier manage the complexity in a class
object Docs extends Schema[DocDoc] {
  val KEY_FIELD = "name"
  val COLL_NAME: String = COLLECTION_DOCS
  val collection: MongoCollection[DocDoc] = DB.getDb.getCollection(COLL_NAME)

  def generateID(): String = ???

  // For debug, show the whole collection
  def show(): Unit = show[DocDoc]

  // Gets a list of all the docs by name and description
  // For this class, the cose class is not converted to a sequence. This way member names are preserved
  def getAll: Seq[DocDoc] = getAllDocs[DocDoc]

  // Checks for the presence of the name (not description) in the collection
  def contains(value: String): Boolean = containsKeyField[DocDoc](value)

  // Finds a name::description option:
  // - Some(name,description) if the category exists
  // - None if the keyword does not exist
  def find(name: String): Option[DocDoc] = findByKeyField[DocDoc](name) match {
    case h::_ => Some(h)
    case _    => None
  }

  // Add a single document to the collection
  // Checks for duplicates
  // If the name exists then the BSON document is simply updated
  // Note: The followong fields are NEVER updated:
  // - created (time and date of creation)
  def addOne(doc: DocDoc): Unit = {
    if (contains(doc.name)) {
      collection
        .updateOne(
          equal(KEY_FIELD, doc.name),
          combine(
            set("description", doc.description),
            set("author", doc.author),
            set("proj", doc.proj),
            set("category", doc.category),
            set("topics", doc.topics)))
        .printHeadResult("Update result: ")
    } else {
      collection.insertOne(doc).printHeadResult("Add result: ")
    }
  }

  // Add a set of DocDoc instances to the collection
  def add(docs: Seq[DocDoc]): Unit = for (doc <- docs) addOne(doc)

  // Deletes a document from the database based SOLELY on the name
  def del(name: String): Unit = delOneByKeyField[DocDoc](name)

  //--------------------------------------------------------------------------------------------
  // The rest of the methods dont appear in any object derived from SchemaNameDesc
  // These are more complex nethods, things like:
  // - operations based on a field, return sequences of objects
  //--------------------------------------------------------------------------------------------
  def descContainsAnyOf(words: Seq[String]): Option[DocDoc] = ???
}


