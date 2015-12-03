package julienrf.bson

import reactivemongo.bson.{BSONDocumentReader, BSONDocumentWriter}

package object derived {

  def decoder[A](implicit decoder: DerivedDecoder[A]): BSONDocumentReader[A] = decoder

  def encoder[A](implicit encoder: DerivedEncoder[A]): BSONDocumentWriter[A] = encoder

  def codec[A](implicit decoder: DerivedDecoder[A], encoder: DerivedEncoder[A]): BSONDocumentHandler[A] =
    BSONDocumentHandler(decoder, encoder)

}
