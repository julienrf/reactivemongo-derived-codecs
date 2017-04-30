# ReactiveMongo Derived Codecs

`BSONDocumentReader` and `BSONDocumentWriter` derivation for algebraic data types (sealed traits and case classes).

Compared to the ReactiveMongo’s macros, this project brings support for:
- sealed traits ;
- recursive types.

## Installation

Add the following dependency to your build:

~~~ scala
libraryDependencies += "org.julienrf" %% "reactivemongo-derived-codecs" % "3.0"
~~~

Version 3.0 is built against Scala 2.11 and 2.12, shapeless 2.3 and ReactiveMongo 0.12.

## Usage

See [API documentation](http://julienrf.github.io/reactivemongo-derived-codecs/3.0/api/).

### Case classes

~~~ scala
import reactivemongo.bson.{derived, BSONDocumentHandler}

case class User(name: String, age: Int)
object User {
  implicit val bsonCodec: BSONDocumentHandler[User] = derived.codec[User]
}
~~~

### Sealed traits

~~~ scala
import reactivemongo.bson.{derived, BSONDocumentHandler}

sealed trait Foo
case class Bar(i: Int, b: Boolean) extends Foo
case class Baz(s: String) extends Foo
object Foo {
  implicit val bsonCodec: BSONDocumentHandler[Foo] = derived.codec[Foo]
}
~~~

## Changelog

- 3.0
    - ReactiveMongo 0.12 support
    - Scala 2.11 and 2.12 support
- 2.0
    - Upgrade to shapeless 2.3.0, enum 3.0 and ReactiveMongo 0.11.11
    - Make `BSONDocumentHandler[A]` extend `BSONHandler[BSONDocument, A]`
    - Move everything into package `reactivemongo.bson.derived`
- 1.1
    - Improved error message in case of failure when decoding a sum type
- 1.0
    - First release

## License

This content is released under the [MIT License](http://opensource.org/licenses/mit-license.php).
