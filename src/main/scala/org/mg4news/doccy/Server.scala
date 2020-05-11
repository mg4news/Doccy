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

import akka.actor.ActorSystem
import akka.http.scaladsl._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import com.typesafe.scalalogging.LazyLogging
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn
import scala.util.{Failure, Success}

object ServerVersion {
  def route(): Route = {
    path("server-version") {
      get {
        val serverVersion = srv_config.getString("version")
        complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, serverVersion))
      }
    }
  }
}

object Server extends App with LazyLogging {

  // Load - if needed
  MockData.load()
  DB.printCollections()

  implicit val system: ActorSystem = ActorSystem("doccy-http-rest-server")
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  // Create routes for server up and server version
  // chain (~) them
  val serverVerRoute = ServerVersion.route()
  val serverUpRoute: Route = get {
    complete("Doccy HTTP server is UP!")
  }
  val routes: Route = serverVerRoute ~ serverUpRoute

  val httpServerFuture = Http().bindAndHandle(
    routes,
    srv_config.getString("host"),
    srv_config.getInt("port"))
  httpServerFuture.onComplete {
    case Success(binding) =>
      logger.info(s"Doccy Http Server is UP and is bound to ${binding.localAddress}")

    case Failure(e) =>
      logger.error(s"Doccy Http server failed to start", e)
      system.terminate()
  }

  // Run till user presses return..
  println("..press return to terminate..")
  StdIn.readLine()

  // Until HTTP4S is in place, kill all collections on exit..
  MockData.unload()

  // Shut downn server
  httpServerFuture.
    flatMap(_.unbind()).                // Trigger unbinding from port
    onComplete(_ => system.terminate()) // Shutdown akka system when done
}
