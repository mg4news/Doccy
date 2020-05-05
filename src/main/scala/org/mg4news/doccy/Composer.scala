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
  import DoccyJsonProtocol._

  // Doc getters.
  def getDocList = Docs.getAll

  // For the rest, three forms:
  // - get all, no parameters
  // - get (find) by name
  // - set by name

  def getAuthorList = Authors.getAll
  def getAuthor(name: String) = Authors.find(name)
  def setAuthor(name: String, desc: String): Unit = Authors.addOne(name,desc)

  def getCategoryList = Categories.getAll
  def getCategory(name: String) = Categories.find(name)
  def setCategory(name: String, desc: String): Unit = Categories.addOne(name,desc)

  def getTopicList = Topics.getAll
  def getTopic(name: String) = Topics.find(name)
  def setTopic(name: String, desc: String): Unit = Topics.addOne(name,desc)

  def getProjectList = Projects.getAll
  def getProject(name: String) = Projects.find(name)
  def setProject(name: String, desc: String): Unit = Projects.addOne(name,desc)

}
