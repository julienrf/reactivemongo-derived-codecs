# ReactiveMongo Derived Codecs

`BSONDocumentReader` and `BSONDocumentWriter` derivation for algebraic data types (sealed traits and case classes)

## Installation

Add the following dependency to your build:

~~~ scala
libraryDependencies += "org.julienrf" %% "reactivemongo-derived-codecs" % "1.0"
~~~

## Usage

### Case classes

~~~ scala
import julienrf.bson.{derived, BSONDocumentHandler}

case class User(name: String, age: Int)
object User {
  implicit val bsonCodec: BSONDocumentHandler[User] = derived.codec[User]
}
~~~

### Sealed traits

~~~ scala
import julienrf.bson.{derived, BSONDocumentHandler}

sealed trait Foo
case class Bar(i: Int, b: Boolean) extends Foo
case class Baz(s: String) extends Foo
object Foo {
  implicit val bsonDecoder: BSONDocumentHandler[Foo] = derived.codec[Foo]
}
~~~

## Changelog

- 1.0
    - First release

## License

This content is released under the [MIT License](http://opensource.org/licenses/mit-license.php).