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
package org.mg4news

import com.typesafe.config.{Config, ConfigFactory}

package object doccy {

  // Config stuff, all names and settings are there
  val config: Config = ConfigFactory.load()
  assert(!config.isEmpty)
  val db_config = config.getConfig("database")
  assert(!db_config.isEmpty)
  val srv_config = config.getConfig("server")
  assert(!srv_config.isEmpty)

  // database name
  val DATABASE       = db_config.getString("database_name")

  // String names. Used in collections, REST API, etc
  val STR_CATEGORIES = db_config.getString("categories_name")
  val STR_TOPICS     = db_config.getString("topics_name")
  val STR_AUTHORS    = db_config.getString("authors_name")
  val STR_PROJECTS   = db_config.getString("projects_name")
  val STR_DOCS       = db_config.getString("docs_name")
}
