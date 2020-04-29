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

import Helpers._

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
  }

  def unload(): Unit = {
    Categories.destroy()
    assert(Categories.number == 0)
    Authors.destroy()
    assert(Authors.number == 0)
    Topics.destroy()
    assert(Topics.number == 0)
  }
}

object Main extends App {

  // Load - if needed
  Mockit.load()

  DB.printCollections()

  // Do stuff
  println("Authors:")
  Composer.getAuthorList().foreach(a => println(s"- $a"))
  println(" ")
  println("Categories:")
  Composer.getCategoryList().foreach(c => println(s"- $c"))
  println(" ")
  println("Topics:")
  Composer.getTopicList().foreach(t => println(s"- $t"))

  // Until HTTP4S is in place, kill all collections on exit..
  Mockit.unload()
}
