package julienrf.bson.derived

import reactivemongo.bson.{BSONValue, BSONReader, BSONDocument, BSONDocumentReader}
import shapeless.labelled.{field, FieldType}
import shapeless.{Inl, Inr, :+:, Witness, HNil, CNil, HList, Lazy, LabelledGeneric, Coproduct, ::}

import scala.annotation.implicitNotFound

/**
  * @tparam A Decoded type
  */
@implicitNotFound("Unable to derive a BSON decoder for type ${A}. If it is a case class, check that all its fields can be decoded.")
trait DerivedDecoder[A] extends BSONDocumentReader[A]

/**
  * As usual the derivation process is as follows:
  *  - let shapeless represent our type A in terms of Coproduct (if it is a sealed trait) or HList (if it is a case class) ;
  *  - define how to decode Coproducts and HLists using implicit definitions
  */
object DerivedDecoder extends DerivedDecoderLowPriority {

  def apply[A](implicit decoder: DerivedDecoder[A]): BSONDocumentReader[A] = decoder

  implicit val decodeCNil: DerivedDecoder[CNil] =
    new DerivedDecoder[CNil] {
      def read(bson: BSONDocument) = sys.error(s"Unable to decode a sum type.")
    }

  implicit val decodeHNil: DerivedDecoder[HNil] =
    new DerivedDecoder[HNil] {
      def read(bson: BSONDocument) = HNil
    }

  implicit def decodeCoproduct[K <: Symbol, L, R <: Coproduct](implicit
    typeName: Witness.Aux[K],
    decodeL: Lazy[BSONDocumentReader[L]],
    decodeR: Lazy[DerivedDecoder[R]]
  ): DerivedDecoder[FieldType[K, L] :+: R] =
    new DerivedDecoder[FieldType[K, L] :+: R] {
      def read(bson: BSONDocument) =
        bson.getAs(typeName.value.name)(decodeL.value)
          .fold[FieldType[K, L] :+: R](Inr(decodeR.value.read(bson)))(l => Inl(field(l)))
    }

  implicit def decodeLabelledHList[K <: Symbol, H, T <: HList](implicit
    fieldName: Witness.Aux[K],
    decodeH: Lazy[BSONReader[_ <: BSONValue, H]],
    decodeT: Lazy[DerivedDecoder[T]]
  ): DerivedDecoder[FieldType[K, H] :: T] =
    new DerivedDecoder[FieldType[K, H] :: T] {
      def read(bson: BSONDocument) = {
        val h = bson.getAs(fieldName.value.name)(decodeH.value).getOrElse(sys.error(s"Unable to decode field $fieldName"))
        val t = decodeT.value.read(bson)
        field[K](h) :: t
      }
    }

}

trait DerivedDecoderLowPriority {

  // For convenience, automatically derive instances for coproduct types. The only difference with decodeCoproduct is the type of `decodeL`.
  implicit def decodeCoproductDerived[K <: Symbol, L, R <: Coproduct](implicit
    typeName: Witness.Aux[K],
    decodeL: Lazy[DerivedDecoder[L]],
    decodeR: Lazy[DerivedDecoder[R]]
  ): DerivedDecoder[FieldType[K, L] :+: R] =
    new DerivedDecoder[FieldType[K, L] :+: R] {
      def read(bson: BSONDocument) =
        bson.getAs(typeName.value.name)(decodeL.value)
          .fold[FieldType[K, L] :+: R](Inr(decodeR.value.read(bson)))(l => Inl(field(l)))
    }

  implicit def decodeGeneric[A, R](implicit
    gen: LabelledGeneric.Aux[A, R],
    derivedDecoder: Lazy[DerivedDecoder[R]]
  ): DerivedDecoder[A] =
    new DerivedDecoder[A] {
      def read(bson: BSONDocument) = gen.from(derivedDecoder.value.read(bson))
    }

}
