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

import org.scalatest.matchers.should.Matchers
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.server._
import Directives._
import org.scalatest.wordspec.AnyWordSpec
import com.typesafe.config.{Config, ConfigFactory}
import org.mongodb.scala.MongoCollection

class NameDescRouteTest extends AnyWordSpec with Matchers with ScalatestRouteTest {
  object TestNDRT extends NameDescSchema {
    val COLL_NAME: String = "ROUTE_TEST_COLL"
    val collection: MongoCollection[DocNameDesc] = DB.getDb.getCollection(COLL_NAME)
  }

  // Empty the DB, then add the "full" seq
  TestNDRT.destroy()
  TestNDRT.add(TestData.full)

  "The Doccy service" should {

    "return an UP message in response to a GET on the root path" in {
      Get() -> Server.upRoute -> check(responseAs[String] shouldEqual "Doccy HTTP server is UP!")
    }

    s"return a version message (${srv_config.getString("version")}) in response to a GET on the version path" in {
      Get("version") -> Server.versionRoute -> check(responseAs[String] shouldEqual srv_config.getString("version"))
    }
    
    "return a NotFound status if query results not found" in {
      Get("GND?name=jim") -> NameDescRoutes.routeGetNd(TestNDRT,"GND") -> check {
        status shouldEqual StatusCodes.NotFound
      }
    }
  }

  TestNDRT.destroy()
}
