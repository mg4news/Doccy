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

// Composes functionality from the various Mongo schema objects.
// This is the object that the rest of the program interacts with. It hides the
// details of the schema
object Composer {
  // Each of these functions produces a Seq of the name field from each of
  // the various building blocks classes. This is something that can be used by
  // a web page or UX
  def getDocList(): Seq[String] = Docs.getAll.map(_.name)
  def getAuthorList(): Seq[String] = Authors.getAll.map(_.name)
  def getCategoryList(): Seq[String] = Categories.getAll.map(_.name)
  def getTopicList(): Seq[String] = Topics.getAll.map(_.name)
  def getProjectList(): Seq[String] = Projects.getAll.map(_.name)

  // Update or add. This set covers the simple cases (authors, cat, topic, proj)
  def setAuthor(name: String, desc: String): Unit = Authors.addOne(name,desc)
  def setCategory(name: String, desc: String): Unit = Categories.addOne(name,desc)
  def setTopic(name: String, desc: String): Unit = Topics.addOne(name,desc)
  def setProject(name: String, desc: String): Unit = Projects.addOne(name,desc)

  // Update or add a document

}
