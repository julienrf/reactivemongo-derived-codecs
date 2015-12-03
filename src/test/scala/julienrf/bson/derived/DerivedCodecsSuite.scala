package julienrf.bson.derived

import julienrf.bson.BSONDocumentHandler
import org.scalacheck.{Gen, Arbitrary}
import org.scalatest.FunSuite
import org.scalatest.prop.Checkers

class DerivedCodecsSuite extends FunSuite with Checkers {

  // Stolen from http://github.com/travisbrown/circe
  sealed trait RecursiveAdtExample
  case class BaseAdtExample(a: String) extends RecursiveAdtExample
  case class NestedAdtExample(r: RecursiveAdtExample) extends RecursiveAdtExample

  implicit val adtCodec: BSONDocumentHandler[RecursiveAdtExample] = codec[RecursiveAdtExample]

  private def atDepth(depth: Int): Gen[RecursiveAdtExample] = if (depth < 3)
    Gen.oneOf(
      Arbitrary.arbitrary[String].map(BaseAdtExample(_)),
      atDepth(depth + 1).map(NestedAdtExample(_))
    ) else Arbitrary.arbitrary[String].map(BaseAdtExample(_))

  implicit val arbitraryRecursiveAdtExample: Arbitrary[RecursiveAdtExample] =
    Arbitrary(atDepth(0))

  test("identity") {
    check((v: RecursiveAdtExample) => adtCodec.read(adtCodec.write(v)) == v)
  }

}
