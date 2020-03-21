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

import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec

class DocDocTest  extends AnyFeatureSpec with GivenWhenThen {

  // Helpers to transform raw sequence data to a document
  def dataToDocs(ds: Seq[(String,String,String,String,String,Seq[String])]): Seq[DocDoc] =
    ds.map(DocDoc(_))

  Feature("Basic functionality tests for Docs object and underlying schema") {

    Scenario("Ensure the collection is empty to begin") {
      Given("any set of starting conditions")
      When("destroy() is called")
      Docs.destroy()
      Then("the collection should be cleaned, no entries")
      assert(Docs.number == 0)
    }

    Scenario("Test single insertion") {
      Given("the collection is empty")
      assert(Docs.number == 0)
      When("one document is inserted")
      Docs.addOne(DocDoc(TestData.docs_with_dups.head))
      Then("the collection contains one document")
      assert(Docs.number == 1)
      And("that document is the one that was inserted")
      val res = Docs.find(TestData.docs_with_dups.head._1)
      assert(res.isDefined)
      if (res.isDefined) {
        val v = res.get
        info(s" - inserted: $v")
      }
    }

    Scenario("Test single deletion") {
      Given("the collection contains one document")
      assert(Docs.number == 1)
      When("that document is deleted")
      Docs.del(TestData.docs_with_dups.head._1)
      Then("the collection is empty")
      assert(Docs.number == 0)
    }

    Scenario("Test multiple insertion with duplication checks") {
      Given("the collection is empty")
      assert(Docs.number == 0)
      When("a set with duplicate descriptions in inserted")
      Docs.add(dataToDocs(TestData.docs_with_dups))
      Then("There are more than 0 entries in the collection")
      assert(Docs.number > 0)
      And("The collection contains no duplicates")
      assert(Docs.number == TestData.docs_no_dups.length)
      And("all the correct entries exist")
      for (d <- TestData.docs_no_dups) {
        assert(Docs.contains(d._1))
      }
      And("none of the wrong entries exist")
      for (s <- TestData.bad) {
        assert(!Docs.contains(s))
      }
    }

    Scenario("Test the findBytopic logic") {
      Given("the collection has multiple entries")
      val entries = Docs.number
      assert(entries > 1)
      info(s" - collection has $entries entries")
      When("i search by some topics")
      info(s" - using: ${TestData.some_topics}")
      val res = Docs.findByTopic(TestData.some_topics).getOrElse(Seq())
      Then("i get a subset of the entries")
      info(s" - got ${res.length} results")
      for (d <- res) {
        info(s" - $d")
      }
    }

  }

  info("Collection sanity dump:")
  for (d <- Docs.getAll) {
    info(s" - $d")
  }
  info("Cleaning the collection")
  Docs.destroy()
  assert(Docs.number == 0)
}
