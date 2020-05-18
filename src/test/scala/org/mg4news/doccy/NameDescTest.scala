//==============================================================================================
// This is free and unencumbered software released into the public domain.
//
// Anyone is free to copy, modify, publish, use, compile, sell, or distribute this software,
// either in source code form  or as a compiled binary, for any purpose, commercial or
// non-commercial, and by any means.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
// BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
// CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//
// This is a simplified version of UNLICENSE.
// For more information, please refer to <http://unlicense.org/>
//==============================================================================================
package org.mg4news.doccy

import org.scalatest._
import org.mongodb.scala.MongoCollection
import org.scalatest.featurespec.AnyFeatureSpec


class NameDescTest extends AnyFeatureSpec with GivenWhenThen{
  object TestNDT extends NameDescSchema {
    val COLL_NAME: String = "TEST_NAME_DESC_COLLECTION"
    val collection: MongoCollection[DocNameDesc] = DB.getDb.getCollection(COLL_NAME)
  }

  Feature("Basic functionality tests for NameDescSchema") {

    info("As a generic instance (object) of NameDescSchema")
    info("I want to Test all aspects of the trait")
    info("So that I can prove that all simple derivations of the trait will work correctly")
    info(" ")

    Scenario("Ensure the collection is empty to begin") {
      Given("any set of starting conditions")
      When("destroy() is called")
      TestNDT.destroy()
      Then("the collection should be empty")
      assert(TestNDT.number == 0)
      info(" ")
    }

    Scenario("Test single insertion into collection") {
      Given("the collection is empty")
      assert(TestNDT.number == 0)
      When("a name::description is inserted")
      val name = TestData.full.head._1
      val desc = TestData.full.head._2
      TestNDT.addOne(name,desc)
      Then("the collection contains only the inserted data")
      assert(TestNDT.number == 1)
      assert(TestNDT.contains(name))
      assert(TestNDT.find(name).isDefined)
      val bad = TestData.bad.head
      assert(!TestNDT.contains(bad))
      assert(TestNDT.find(bad).isEmpty)
      info(" ")
    }

    Scenario("Test multiple insertion with duplication checks") {
      Given("the collection is empty")
      TestNDT.destroy()
      assert(TestNDT.number == 0)
      When("a set with duplicate descriptions in inserted")
      TestNDT.add(TestData.full)
      Then("There are more than 0 entries in the collection")
      assert(TestNDT.number > 0)
      And("The collection contains no duplicates")
      assert(TestNDT.number == TestData.dedup_size)
      And("all the correct entries exist")
      for (d <- TestData.dedup) {
        assert(TestNDT.contains(d._1))
      }
      And ("none of the wrong entries exist")
      for (s <- TestData.bad) {
        assert(!TestNDT.contains(s))
      }
      info(" ")
    }
  }

  info("Collection sanity dump:")
  for (s <- TestNDT.getAll) {
    info(s" - $s")
  }
  info("Cleaning the collection")
  TestNDT.destroy()
  assert(TestNDT.number == 0)

  info(" ")
  info(" ")
}
