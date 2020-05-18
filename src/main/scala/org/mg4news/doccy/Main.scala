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

import scala.io.StdIn
import akka.http.scaladsl.server.Route
import akka.http.scaladsl._
import akka.http.scaladsl.server.Directives._

object Main extends App {

  // Load - if needed
  MockData.load()
  DB.printCollections()

  // Start, run till user presses return, stop
  val srv = Server.start(NameDescRoutes.routes ~ DocDocRoutes.routes)
  println("..press return to terminate..")
  StdIn.readLine()
  Server.stop(srv)

  // Until HTTP4S is in place, kill all collections on exit..
  MockData.unload()
}
