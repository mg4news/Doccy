import com.mg4news.matcha._

object TestKeywords {

  val badwords: Seq[String] = Seq("bill", "ben", "ott")

  /**
   * Run this main method to see the output of this quick example.
   *
   * @param args takes an optional single argument for the connection string
   * @throws Throwable if an operation fails
   */
  def main(args: Array[String]): Unit = {
    Keywords.destroy

    Keywords.add(TestData.keywords)
    println("Testing good words")
    for (kvp <- TestData.keywords) {
      println("Collection contains", kvp._1, Keywords.contains(kvp._1))
    }
    Keywords.show()
    println("Testing bad words")
    for (w <- badwords) {
      println("Collection contains", w, Keywords.contains(w))
    }
    for (kvp <- TestData.keywords) {
      Keywords.del(kvp._1)
    }
    println("Testing deleted words")
    for (kvp <- TestData.keywords) {
      println("Collection contains", kvp._1, Keywords.contains(kvp._1))
    }

    Keywords.destroy
  }

}
