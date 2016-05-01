package reactivemongo.bson
package derived

import shapeless.labelled.FieldType
import shapeless.{LabelledGeneric, HList, Inr, Inl, Lazy, Witness, :+:, Coproduct, HNil, CNil, ::}

import scala.annotation.implicitNotFound

@implicitNotFound("Unable to derive a BSON encoder for type ${A}. If it is a case class, check that all its fields can be encoded.")
trait DerivedEncoder[A] extends BSONDocumentWriter[A]

object DerivedEncoder extends DerivedEncoderLowPriority {

  def apply[A](implicit encoder: DerivedEncoder[A]): BSONDocumentWriter[A] = encoder

  implicit val encodeCNil: DerivedEncoder[CNil] =
    new DerivedEncoder[CNil] {
      def write(t: CNil) = sys.error("No BSON representation of CNil")
    }

  implicit val encodeHNil: DerivedEncoder[HNil] =
    new DerivedEncoder[HNil] {
      def write(t: HNil) = BSONDocument()
    }

  implicit def encodeCoproduct[K <: Symbol, L, R <: Coproduct](implicit
    typeName: Witness.Aux[K],
    encodeL: Lazy[BSONDocumentWriter[L]],
    encodeR: Lazy[DerivedEncoder[R]]
  ): DerivedEncoder[FieldType[K, L] :+: R] =
    new DerivedEncoder[FieldType[K, L] :+: R] {
      def write(t: FieldType[K, L] :+: R) = t match {
        case Inl(l) => BSONDocument(typeName.value.name -> encodeL.value.write(l))
        case Inr(r) => encodeR.value.write(r)
      }
    }

  implicit def encodeLabelledHList[K <: Symbol, H, T <: HList](implicit
    fieldName: Witness.Aux[K],
    encodeH: Lazy[BSONWriter[H, _ <: BSONValue]],
    encodeT: Lazy[DerivedEncoder[T]]
  ): DerivedEncoder[FieldType[K, H] :: T] =
    new DerivedEncoder[FieldType[K, H] :: T] {
      def write(l: FieldType[K, H] :: T) =
        BSONDocument(Seq(fieldName.value.name -> encodeH.value.write(l.head))) ++ encodeT.value.write(l.tail)
    }

}

trait DerivedEncoderLowPriority {

  // For convenience, automatically derive instances for coproduct types
  implicit def encodeCoproductDerived[K <: Symbol, L, R <: Coproduct](implicit
    typeName: Witness.Aux[K],
    encodeL: Lazy[DerivedEncoder[L]],
    encodeR: Lazy[DerivedEncoder[R]]
  ): DerivedEncoder[FieldType[K, L] :+: R] =
    new DerivedEncoder[FieldType[K, L] :+: R] {
      def write(t: FieldType[K, L] :+: R) = t match {
        case Inl(l) => BSONDocument(typeName.value.name -> encodeL.value.write(l))
        case Inr(r) => encodeR.value.write(r)
      }
    }

  implicit def encodeGeneric[A, R](implicit
    gen: LabelledGeneric.Aux[A, R],
    derivedEncoder: Lazy[DerivedEncoder[R]]
  ): DerivedEncoder[A] =
    new DerivedEncoder[A] {
      def write(a: A) = derivedEncoder.value.write(gen.to(a))
    }

}