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

  val docs: Seq[(String,String,String,String,String,Set[String])] = Seq(
    ("ADK PRD", "High level ADK product requirements", "mgibson", "ADK", "PRD", Set("Streaming", "Brewing")),
    ("Doccy design", "Static and dynamic deisgn specification", "mgibson", "Doccy", "ADD", Set("Streaming", "Scala"))
  )
}
