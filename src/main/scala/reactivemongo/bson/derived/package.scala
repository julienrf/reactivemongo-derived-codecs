package reactivemongo.bson

import shapeless.Lazy

/**
  * {{{
  *   import reactivemongo.bson.{derived, BSONDocumentHandler}
  *
  *   sealed trait Foo
  *   case class Bar(i: Int, s: String) extends Foo
  *   case class Baz(b: Boolean) extends Foo
  *
  *   object Foo {
  *     implicit val codec: BSONDocumentHandler[Foo] = derived.codec
  *   }
  * }}}
  */
package object derived {

  def decoder[A](implicit decoder: Lazy[DerivedDecoder[_, A]]): BSONDocumentReader[A] = decoder.value

  def encoder[A](implicit encoder: Lazy[DerivedEncoder[A]]): BSONDocumentWriter[A] = encoder.value

  def codec[A](implicit decoder: Lazy[DerivedDecoder[_, A]], encoder: Lazy[DerivedEncoder[A]]): BSONDocumentHandler[A] =
    BSONDocumentHandler(decoder.value, encoder.value)

}
