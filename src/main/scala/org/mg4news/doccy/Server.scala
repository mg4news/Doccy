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
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

object Server extends LazyLogging {

  // Implicits: actor system and system dispatcher
  implicit val system: ActorSystem = ActorSystem("doccy-http-rest-server")
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  // Server routes
  val versionRoute: Route = path("version") {
    get {
      val serverVersion = srv_config.getString("version")
      complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, serverVersion))
    }
  }
  val upRoute: Route = get {
    val serverName = srv_config.getString("name")
    complete(s"$serverName HTTP server is UP!")
  }

  // Start the server
  def start(routes: Route): Future[Http.ServerBinding] = {
    val httpServerFuture: Future[Http.ServerBinding] = Http().bindAndHandle(
      routes ~ versionRoute ~ upRoute,
      srv_config.getString("host"),
      srv_config.getInt("port"))
    httpServerFuture.onComplete {
      case Success(binding) =>
        logger.info(s"Doccy Http Server is UP and is bound to ${binding.localAddress}")
      case Failure(e) =>
        logger.error(s"Doccy Http server failed to start", e)
        system.terminate()
    }
    return httpServerFuture
  }

  // Shut down server
  def stop(httpServerFuture: Future[Http.ServerBinding]): Unit =
    httpServerFuture.
      flatMap(_.unbind()). // Trigger unbinding from port
      onComplete(_ => system.terminate()) // Shutdown akka system when done

}
