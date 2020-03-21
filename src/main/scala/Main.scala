import org.mg4news.Doccy._

object Main {

  /**
   * Run this main method to see the output of this quick example.
   *
   * @param args takes an optional single argument for the connection string
   * @throws Throwable if an operation fails
   */
  def main(args: Array[String]): Unit = {
    println("Doccy!!!")

    // Dumb dev test
    val docs_with_dups = Seq(
      ("Doc1", "Doc about 1", "bob", "proj1", "cat1", Seq("top1", "top2")),
      ("Doc2", "Doc about 2", "jim", "proj2", "cat2", Seq("top2", "top3")),
      ("Doc3", "Doc about 3", "sam", "proj1", "cat3", Seq("top1", "top3")),
      ("Doc4", "Doc about 4", "bob", "proj2", "cat1", Seq("top1", "top2", "top3")),
      ("Doc5", "Doc about 5", "jim", "proj1", "cat2", Seq("top1")),
      ("Doc6", "Doc about 6", "sam", "proj2", "cat3", Seq("top3")),
      ("Doc1", "Doc about 1", "bob", "proj1", "cat1", Seq("top2")),
      ("Doc4", "Doc about 4", "bob", "proj2", "cat1", Seq("top2", "top3"))
    )

    val some_topics = Seq("top2", "top3")
    val none_topics = Seq("top4", "top5")

    // Insert all, get them all
    Docs.destroy()
    Docs.add(docs_with_dups.map(DocDoc(_)))
    Docs.show()

    val all = Docs.getAll
    println("all = ")
    print(all)
    println(" ")
    val res1 = all.filter(_.topics.intersect(some_topics).length > 0)
    println(s"all filtered by some_topics = ${res1.length} docs")

    println(" ")
    val res2 = all.filter(_.topics.intersect(none_topics).length > 0)
    println(s"all filtered by none_topics = ${res2.length} docs")
  }

}
