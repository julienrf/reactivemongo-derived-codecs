package julienrf.bson

import reactivemongo.bson.{BSONDocumentReader, BSONDocumentWriter}
import shapeless.Lazy

package object derived {

  def decoder[A](implicit decoder: Lazy[DerivedDecoder[_, A]]): BSONDocumentReader[A] = decoder.value

  def encoder[A](implicit encoder: Lazy[DerivedEncoder[A]]): BSONDocumentWriter[A] = encoder.value

  def codec[A](implicit decoder: Lazy[DerivedDecoder[_, A]], encoder: Lazy[DerivedEncoder[A]]): BSONDocumentHandler[A] =
    BSONDocumentHandler(decoder.value, encoder.value)

}
