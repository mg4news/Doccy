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

// Mock data object, used with the main app until the point we transition to
// the web interface. At that point we can actually add data in a useful
// and representative way
object MockData {
  val categories: Seq[(String, String)] = Seq(
    ("PRD", "Product Requirements Document"),
    ("Whitepaper", "Technical document that analyses a subject, and derives a set of recommendations"),
    ("ADD", "Architecture Design Document"),
    ("Presentation", "Google Slides, Microsoft Powerpoint or Apple Keynote"),
    ("Spreadsheet", "Google sheets, Microsoft Excel, Apple Numbers"),
    ("Recipe", "Ingredients and instructions"),
    ("Other", "General document, no specific category")
  )

  val authors: Seq[(String,String)] = Seq(
    ("aeinstein", "Albert Einstein"),
    ("mgibson", "Martin Gibson"),
    ("jpublic", "John Q Public")
  )

  val topics: Seq[(String,String)] = Seq(
    ("Brewing", "Anything to do with brewing"),
    ("Scala", "Related to Scala or Dotty (Scala 3)"),
    ("Streaming", "Related to media streaming, i.e. CMAF, DASH")
  )

  val projects: Seq[(String,String)] = Seq(
    ("ADK", "ADK related"),
    ("Floor", "Project to lqy new laminate flooring")
  )

  val docs: Seq[(String,String,String,String,String,Set[String])] = Seq(
    ("ADK PRD", "High level ADK product requirements", "mgibson", "ADK", "PRD", Set("Streaming", "Brewing")),
    ("Doccy design", "Static and dynamic deisgn specification", "mgibson", "Doccy", "ADD", Set("Streaming", "Scala"))
  )

  // load the data
  def load(): Unit = {
    if (Categories.number == 0) {
      Categories.add(categories)
      assert(Categories.number == categories.length)
    }
    if (Authors.number == 0) {
      Authors.add(authors)
      assert(Authors.number == authors.length)
    }
    if (Topics.number == 0) {
      Topics.add(topics)
      assert(Topics.number == topics.length)
    }
    if (Docs.number == 0) {
      Docs.add(docs.map(t => DocDoc(t._1,t._2,t._3,t._4,t._5,t._6)))
      assert(Docs.number == docs.length)
    }
  }

  // Unload the data
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
