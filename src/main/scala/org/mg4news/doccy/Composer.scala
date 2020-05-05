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

import spray.json._
import DefaultJsonProtocol._

object MyJsonProtocol extends DefaultJsonProtocol {
  implicit object DocNameDescJsonFormat extends RootJsonFormat[DocNameDesc] {
    def write(dnc: DocNameDesc): JsObject = JsObject(
      "name" -> JsString(dnc.name),
      "description" -> JsString(dnc.description)
    )
    def read(value:JsValue): DocNameDesc = {
      value.asJsObject.getFields("name", "description") match {
        case Seq(JsString(name), JsString(description)) => DocNameDesc(name,description)
        case _ => throw new DeserializationException("(name,description) pair expected")
      }
    }
  }

  implicit object DocDocJsonFormat extends RootJsonFormat[DocDoc] {
    def write(dd: DocDoc): JsObject = JsObject(
      "name" -> JsString(dd.name),
      "description" -> JsString(dd.description),
      "author" -> JsString(dd.author),
      "proj" -> JsString(dd.proj),
      "category" -> JsString(dd.category),
      "topics" -> dd.topics.toJson,
      "created" -> JsString(dd.created.toString)
    )
    def read(value: JsValue): DocDoc = {
      value.asJsObject.getFields("name", "description", "author", "proj", "category", "topics") match {
        case Seq(JsString(name), JsString(description), JsString(author), JsString(proj), JsString(category), JsArray(topics)) =>
          DocDoc(name,description,author,proj,category,topics.map(_.convertTo[String]).toSet)
      }
    }
  }
}

// Composes functionality from the various Mongo schema objects.
// This is the object that the rest of the program interacts with. It hides the
// details of the schema
object Composer {
  import MyJsonProtocol._

  // Doc getters.
  def getDocList: JsValue = Docs.getAll.toJson

  // For the rest, three forms:
  // - get all, no parameters
  // - get (find) by name
  // - set by name

  def getAuthorList: JsValue = Authors.getAll.toJson
  def getAuthor(name: String): JsValue = Authors.find(name).toJson

  def getCategoryList: JsValue = Categories.getAll.toJson
  def getCategory(name: String): JsValue = Categories.find(name).toJson

  def getTopicList: JsValue = Topics.getAll.toJson
  def getTopic(name: String): JsValue = Topics.find(name).toJson

  def getProjectList: JsValue = Projects.getAll.toJson
  def getProject(name: String): JsValue = Projects.find(name).toJson

  // Update or add. This set covers the simple cases (authors, cat, topic, proj)
  def setAuthor(name: String, desc: String): Unit = Authors.addOne(name,desc)
  def setCategory(name: String, desc: String): Unit = Categories.addOne(name,desc)
  def setTopic(name: String, desc: String): Unit = Topics.addOne(name,desc)
  def setProject(name: String, desc: String): Unit = Projects.addOne(name,desc)

  // Update or add a document

}
