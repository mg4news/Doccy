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
