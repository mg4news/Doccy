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

import com.typesafe.scalalogging.LazyLogging
import de.heikoseeberger.akkahttpargonaut.ArgonautSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import argonaut._
import Argonaut._
import akka.http.scaladsl.model.StatusCodes

object NameDescRoutes extends ArgonautSupport with LazyLogging {
  import MyJsonCodecs._

  /**
   * Route builder for "NameDescSchema.getAll"
   * @param obj Object (must extend trait NameDescSchema)
   * @param p Path to be used in route
   * @return Route
   */
  def routeAllNd(obj: NameDescSchema, p: String): Route = path(p) {
    get {
      logger.info(s"GET $p")
      complete(obj.getAll.toList)
    }
  }

  /**
   * Route builder for NameDescSchema.find
   * @param obj Object (must extend trait NameDescSchema)
   * @param p Path to be used in route
   * @return Route
   */
  def routeGetNd(obj: NameDescSchema, p: String): Route = path(p) {
    parameters("name") {name =>
      logger.info(s"PARAMETER ${obj.COLL_NAME} name=$name")
      obj.find(name) match {
        case Some(res) => complete(res)
        case None => complete(StatusCodes.NotFound)
      }
    }
  }

  /**
   * Route builder for NameDescSchema.addOne
   * @param obj Object (must extend trait NameDescSchema)
   * @param p Path to be used in route
   * @return Route
   */
  def routeAddNd(obj: NameDescSchema, p: String): Route = path(p) {
    post {
      entity(as[Json]) { namedesc =>
        logger.info(s"POST create/update $p = ${namedesc.spaces2}")
        namedesc.as[DocNameDesc].result match {
          case Left(_) =>
            logger.info("Cant convert from JSON")
            complete(StatusCodes.BadRequest)
          case Right(nd) =>
            obj.addOne(nd.name, nd.description)
            complete(StatusCodes.OK)
        }
      }
    }
  }

  // Published routes
  val routes: Route = routeAllNd(Authors, "authors") ~ routeGetNd(Authors, "author") ~ routeAddNd(Authors, "author")
    routeAllNd(Categories, "categories") ~ routeGetNd(Categories, "category") ~ routeAddNd(Categories, "category")
    routeAllNd(Projects, "projects") ~ routeGetNd(Projects, "project") ~ routeAddNd(Projects, "project")
    routeAllNd(Topics, "topics") ~ routeGetNd(Topics, "topic") ~ routeAddNd(Topics, "topic")
}
