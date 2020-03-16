import com.mg4news.matcha._
import org.mg4news.Doccy.{Topics, TestData}

object TestKeywords {

  val badwords: Seq[String] = Seq("bill", "ben", "ott")

  /**
   * Run this main method to see the output of this quick example.
   *
   * @param args takes an optional single argument for the connection string
   * @throws Throwable if an operation fails
   */
  def main(args: Array[String]): Unit = {
    Topics.destroy

    Topics.add(TestData.keywords)
    println("Testing good words")
    for (kvp <- TestData.keywords) {
      println("Collection contains", kvp._1, Topics.contains(kvp._1))
    }
    Topics.show()
    println("Testing bad words")
    for (w <- badwords) {
      println("Collection contains", w, Topics.contains(w))
    }
    for (kvp <- TestData.keywords) {
      Topics.del(kvp._1)
    }
    println("Testing deleted words")
    for (kvp <- TestData.keywords) {
      println("Collection contains", kvp._1, Topics.contains(kvp._1))
    }

    Topics.destroy
  }

}
