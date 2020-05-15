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

import akka.http.scaladsl.server.Route
import com.typesafe.scalalogging.LazyLogging
import de.heikoseeberger.akkahttpargonaut.ArgonautSupport
import akka.http.scaladsl.server.Directives._
import argonaut._
import Argonaut._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.unmarshalling.PredefinedFromStringUnmarshallers.CsvSeq

case object DocDocRoutes extends ArgonautSupport with LazyLogging {
  import MyJsonCodecs._

  /**
   * Generates a route for getAllDocs
   */
  val routeAll: Route = path("docs") {
    get {
      logger.info("GET docs")
      complete(Docs.getAll.toList)
    }
  }

  /**
   * Generates a route for each valid field for Docs.findByXXX
   */
  val routeByField: Route = path("doc") {
    parameters("name") {name =>
      logger.info(s"PARAMETER docs by name = $name")
      complete(Docs.find(name))
    } ~ parameters("category") {category =>
        logger.info(s"PARAMETER docs by category = $category")
        Docs.findByCategory(category) match {
          case Some(seq) => complete(seq.toList)
          case None => complete(StatusCodes.NotFound)
        }
    } ~ parameters("author") {author =>
      logger.info(s"PARAMETER docs by author = $author")
      Docs.findByAuthor(author) match {
        case Some(seq) => complete(seq.toList)
        case None => complete(StatusCodes.NotFound)
      }
    } ~ parameters("project") {project =>
      logger.info(s"PARAMETER docs by project = $project")
      Docs.findByProject(project) match {
        case Some(seq) => complete(seq.toList)
        case None => complete(StatusCodes.NotFound)
      }
    }
  }

  /**
   * route to add or modify a Doc
   */
  val routeAddDoc: Route = path("doc") {
    post {
      entity(as[Json]) { doc =>
        logger.info(s"POST create/update doc = ${doc.spaces2}")
        doc.as[DocDoc].result match {
          case Left(_) =>
            logger.info("Cant convert from Json")
            complete(StatusCodes.BadRequest)
          case Right(d) =>
            Docs.addOne(d)
            complete(StatusCodes.OK)
        }
      }
    }
  }

  /**
   * routes to get docs by any or all topics
   */
    val routeByTopics: Route = path("doc") {
      parameters("any_topics".as(CsvSeq[String])) { any_topics =>
        logger.info(s"PARAMETER docs by ANY topics in $any_topics")
        Docs.findByAnyTopics(any_topics.toSet) match {
          case Some(seq) => complete(seq.toList)
          case None => complete(StatusCodes.NotFound)
        }
      } ~ parameters("all_topics".as(CsvSeq[String])) { all_topics =>
        logger.info(s"PARAMETER docs by ALL topics in $all_topics")
        Docs.findByAllTopics(all_topics.toSet) match {
          case Some(seq) => complete(seq.toList)
          case None => complete(StatusCodes.NotFound)
        }
      }
    }

  val routes: Route = routeAll ~ routeByField ~ routeAddDoc ~ routeByTopics
}
