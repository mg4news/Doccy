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

import argonaut._
import Argonaut._

/**
 * Utility object for the JSON encoders and decoders
 */
object MyJsonCodecs {

  /**
   * Encodes a DocNameDesc to JSON, ignores the ID field
   * @return JSON
   */
  implicit def DocNameDescEncodeJson: EncodeJson[DocNameDesc] =
    EncodeJson((nd: DocNameDesc) =>
      ("name" := nd.name) ->: ("description" := nd.description) ->: jEmptyObject)

  /**
   * Decodes a JSON to a DocNameDesc
   * @return DocNameDesc
   */
  implicit def DocNameDescDecodeJson: DecodeJson[DocNameDesc] =
    DecodeJson(c => for {
      name <- (c --\ "name").as[String]
      description <- (c --\ "description").as[String]
    } yield DocNameDesc(name,description))

  implicit def DocEncodeJson: EncodeJson[DocDoc] =
    EncodeJson((d: DocDoc) => ("name" := d.name) ->:
      ("description" := d.description) ->:
      ("author" := d.author) ->:
      ("proj" := d.proj) ->:
      ("category" := d.category) ->:
      ("topics" := d.topics) ->:
      ("created" := d.created.toString) ->:
      jEmptyObject)

  implicit def DocDecodeJson: DecodeJson[DocDoc] =
    DecodeJson(c => for {
      name        <- (c --\ "name").as[String]
      description <- (c --\ "description").as[String]
      author      <- (c --\ "author").as[String]
      proj        <- (c --\ "proj").as[String]
      category    <- (c --\ "category").as[String]
      topics      <- (c --\ "topics").as[Set[String]]
    } yield DocDoc(name,description,author,proj,category,topics))
}
