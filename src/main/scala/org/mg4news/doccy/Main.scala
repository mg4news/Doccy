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

object Mockit {
  def load(): Unit = {
    if (Categories.number == 0) {
      Categories.add(MockData.categories)
      assert(Categories.number == MockData.categories.length)
    }
    if (Authors.number == 0) {
      Authors.add(MockData.authors)
      assert(Authors.number == MockData.authors.length)
    }
    if (Topics.number == 0) {
      Topics.add(MockData.topics)
      assert(Topics.number == MockData.topics.length)
    }
    if (Docs.number == 0) {
      Docs.add(MockData.docs.map(t => DocDoc(t._1,t._2,t._3,t._4,t._5,t._6)))
      assert(Docs.number == MockData.docs.length)
    }
  }

  def unload(): Unit = {
    Categories.destroy()
    assert(Categories.number == 0)
    Authors.destroy()
    assert(Authors.number == 0)
    Topics.destroy()
    assert(Topics.number == 0)
    Docs.destroy()
    assert(Docs.number == 0)
  }
}

object Main extends App {
  import MyJsonCodecs._

  // Load - if needed
  Mockit.load()

  DB.printCollections()

  // Do stuff
  val json1 = """
    { "name" : "dtrump", "description" : "Donald Trump"}
  """
  val json2 = """
    { "name" : "seinfeld", "descraption" : "Jerry Seinfeld"}
  """
  val json3 = """
    { "name" : "rr", "description" : "Rob Roy". "country" : "Scotland"}
  """
  val json4 = """
    { "name" : "joeblogs"}
  """

  println(s"Authors: ${Composer.getAuthorList.spaces2}")
  println(s"Find author = mgibson => ${Composer.getAuthor("mgibson").spaces2}")
  println(s"Find author = dindong => ${Composer.getAuthor("dingdong").spaces2}")
  println(" ")
  println("Testing the decoding..")
  Composer.setAuthor(json1)
  Composer.setAuthor(json2)
  Composer.setAuthor(json3)
  Composer.setAuthor(json4)
  println(" ")
  println(s"Categories: ${Composer.getCategoryList.spaces2}")
  println(" ")
  println(s"Topics: ${Composer.getTopicList.spaces2}")

  println("Documents tests..")
  println(s"All documents: ${Composer.getDocList.spaces2}")

  val jsonDoc1 = Composer.getDoc("ADK PRD")
  println(s" - Looking for ADK PRD: ${jsonDoc1.spaces2}")
  val jsonDoc2 = Composer.getDoc("ADK PDR")
  println(s" - Looking for ADK PDR: ${jsonDoc2.spaces2}")

  println(jsonDoc1.as[DocDoc])
  println(jsonDoc2.as[DocDoc])

  jsonDoc1.as[DocDoc].result match {
    case Left(_) => println("Cant convert from JSON")
    case Right(d) => {
      println("Modify the doc..")
      Docs.addOne(DocDoc(d.name,d.description,"bthornton",d.proj,d.category,d.topics))
    }
  }
  println(s"All documents: ${Composer.getDocList.spaces2}")

  // Until HTTP4S is in place, kill all collections on exit..
  Mockit.unload()
}
