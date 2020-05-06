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

import argonaut._
import Argonaut._

// Composes functionality from the various Mongo schema objects.
// This is the object that the rest of the program interacts with. It hides the
// details of the schema
object Composer {
  import MyJsonCodecs._

  // Doc getters and setters
  def getDocList: Json = Docs.getAll.toList.asJson
  def getDoc(name: String): Json = Docs.find(name).asJson

  // For the rest, three forms:
  // - get all, no parameters
  // - get (find) by name
  // - set by name

  def getAuthorList: Json = Authors.getAll.toList.asJson
  def getAuthor(name: String): Json = Authors.find(name).asJson
  def setAuthor(json: String): Unit = {
    Parse.decodeOption[DocNameDesc](json) match {
      case Some(nd) => Authors.addOne(nd.name, nd.description)
      case None => ()
    }
  }
  def delAuthor(name: String): Unit = Authors.del(name)

  def getCategoryList: Json = Categories.getAll.toList.asJson
  def getCategory(name: String): Json = Categories.find(name).asJson
  def setCategory(json: String): Unit = {
    Parse.decodeOption[DocNameDesc](json) match {
      case Some(nd) => Categories.addOne(nd.name,nd.description)
      case None => ()
    }
  }
  def delCategory(name: String): Unit = Categories.del(name)

  def getTopicList: Json = Topics.getAll.toList.asJson
  def getTopic(name: String): Json = Topics.find(name).asJson
  def setTopic(json: String): Unit = {
    Parse.decodeOption[DocNameDesc](json) match {
      case Some(nd) => Topics.addOne(nd.name,nd.description)
      case None => ()
    }
  }
  def delTopic(name: String): Unit = Topics.del(name)

  def getProjectList: Json = Projects.getAll.toList.asJson
  def getProject(name: String): Json = Projects.find(name).asJson
  def setProject(json: String): Unit = {
    Parse.decodeOption[DocNameDesc](json) match {
      case Some(nd) => Projects.addOne(nd.name,nd.description)
      case None => ()
    }
  }
  def delProject(name: String): Unit = Projects.del(name)

}
