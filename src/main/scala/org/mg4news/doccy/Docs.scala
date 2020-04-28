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
  def apply( name: String,
             description: String,
             author: String,
             proj: String,
             category: String,
             topics: Set[String]): DocDoc =
    DocDoc(new ObjectId(), name, description, author, proj, category, topics, created=LocalDateTime.now())

  def apply( ds: (String,String,String,String,String,Set[String])): DocDoc =
    DocDoc(new ObjectId(), ds._1, ds._2, ds._3, ds._4, ds._5, ds._6, created=LocalDateTime.now())

}
case class DocDoc(
  _id: ObjectId,
  name: String,
  description: String,
  author: String,
  proj: String,
  category: String,
  topics: Set[String],
  created: LocalDateTime) {

  // Generates the immutable ID
  def generateID(): String = ???

}

// Object for the Docs document type. Most of the functions work in terms of the DocDoc case class type.
// This is because it is easier manage the complexity in a class
object Docs extends Schema[DocDoc] {
  val KEY_FIELD = "name"
  val COLL_NAME: String = STR_DOCS
  val collection: MongoCollection[DocDoc] = DB.getDb.getCollection(COLL_NAME)


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
  // Note: The following fields are NEVER updated:
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
  // The rest of the methods don't appear in any object derived from SchemaNameDesc
  // These are more complex methods, things like:
  // - operations based on a field, return options of sequences of objects
  //--------------------------------------------------------------------------------------------

  // Finds all documents where one or more of the topics provided match a topic in the document
  // Because the topics are in a sequence, we need to extract all and do the filtering "above ground"
  def findByAnyTopics(topics: Set[String]): Option[Seq[DocDoc]] = {
    val res = getAll.filter(_.topics.intersect(topics).nonEmpty)
    if (res.nonEmpty) Some(res) else None
  }

  // Finds all documents where all of the topics provided are in the list of topics in the document
  // Because the topics are in a sequence, we need to extract all and do the filtering "above ground"
  def findByAllTopics(topics: Set[String]): Option[Seq[DocDoc]] = {
    def containsAllOf(d: DocDoc): Boolean = topics.subsetOf(d.topics)
    val res = getAll.filter(containsAllOf)
    if (res.nonEmpty) Some(res) else None
  }

  // Find all documents matching a particular category
  // Uses only the category name
  def findByCategory(cat: String): Option[Seq[DocDoc]] = {
    val res = findByField[DocDoc]("category", cat)
    if (res.nonEmpty) Some(res) else None
  }

  // Find all documents matching a particular category
  // Uses only the category name
  def findByProject(proj: String): Option[Seq[DocDoc]] = {
    val res = findByField[DocDoc]("proj", proj)
    if (res.nonEmpty) Some(res) else None
  }

  // Find all documents matching a particular category
  // Uses only the category name
  def findByAuthor(author: String): Option[Seq[DocDoc]] = {
    val res = findByField[DocDoc]("author", author)
    if (res.nonEmpty) Some(res) else None
  }
}


