package org.mg4news.Doccy

import org.scalacheck.Prop.False
import org.scalatest._
import org.mongodb.scala.MongoCollection

object GenNameDesc extends NameDescSchema {
  val COLL_NAME: String = "TEST_NAME_DESC_COLLECTION"
  val collection: MongoCollection[DocNameDesc] = DB.getDb.getCollection(COLL_NAME)
}

class DocNameTest extends FeatureSpec with GivenWhenThen{
  feature("Basic functionality tests for NameDescSchema") {

    scenario("Ensure the collection is empty to begin") {
      Given("any set of starting conditions")
      When("destroy() is called")
      GenNameDesc.destroy()
      Then("the collection should be empty")
      assert(GenNameDesc.number == 0)
    }

    scenario("Test single insertion into collection") {
      Given("the collection is empty")
      assert(GenNameDesc.number == 0)
      When("a name::description is inserted")
      val name = TestData.full(0)._1
      val desc = TestData.full(0)._2
      GenNameDesc.addOne(name,desc)
      Then("the collection contains only the inserted data")
      assert(GenNameDesc.number == 1)
      assert(GenNameDesc.contains(name))
      assert(GenNameDesc.find(name) != None)
      val bad = TestData.bad(0)._1
      assert(GenNameDesc.contains(bad) == false)
      assert(GenNameDesc.find(bad) == None)
    }

    scenario("Test multiple insertion with duplication checks") {
      Given("the collection is empty")
      GenNameDesc.destroy()
      assert(GenNameDesc.number == 0)
      When("a set with duplicate descriptions in inserted")
      GenNameDesc.add(TestData.full)
      Then("There are more than 0 entries in the collection")
      assert(GenNameDesc.number > 0)
      And("The collection contains no duplicates")
      assert(GenNameDesc.number == TestData.dedup_size)
      And("all the correct entries exist")
      for (d <- TestData.dedup) {
        assert(GenNameDesc.contains(d._1))
      }
      And ("none of the wrong entries exist")
      for (d <- TestData.bad) {
        assert(GenNameDesc.contains(d._1) == false)
      }

    }
  }

  info("Dumping the collection")
  GenNameDesc.show()
  info("Cleaning the collection")
  GenNameDesc.destroy()
  assert(GenNameDesc.number == 0)

}
