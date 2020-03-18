//==============================================================================================
// This is free and unencumbered software released into the public domain.
//
// Anyone is free to copy, modify, publish, use, compile, sell, or distribute this software,
// either in source code form  or as a compiled binary, for any purpose, commercial or
// non-commercial, and by any means.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
// BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
// CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//
// This is a simplified version of UNLICENSE.
// For more information, please refer to <http://unlicense.org/>
//==============================================================================================
package org.mg4news.Doccy

import org.mongodb.scala.{MongoClient, MongoDatabase}
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.bson.codecs.configuration.CodecRegistries._
import ch.rasc.bsoncodec.time._
import Helpers._

object DB {

  // Codecs
  private val javaCodecs = fromCodecs(new LocalDateTimeDateCodec())
  private val customCodecs = fromProviders(
    classOf[DocNameDesc],
    classOf[DocDoc])
  private val codecRegistry = fromRegistries(
    customCodecs,
    javaCodecs,
    DEFAULT_CODEC_REGISTRY)

  // Database client
  private val mongoClient: MongoClient = MongoClient()
  private val database = mongoClient.getDatabase(DATABASE).withCodecRegistry(codecRegistry)

  // Public functions and values
  def getDb: MongoDatabase = database
  def printCollections(): Unit = database.listCollectionNames().printResults("Collection Names: ")
}
