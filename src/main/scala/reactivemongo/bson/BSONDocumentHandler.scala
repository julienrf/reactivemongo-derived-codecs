package reactivemongo.bson

/**
  * Allows the definition of both a reader and a writer in one shot.
  */
trait BSONDocumentHandler[A] extends BSONHandler[BSONDocument, A] with BSONDocumentReader[A] with BSONDocumentWriter[A] {
  val reader: BSONDocumentReader[A]
  val writer: BSONDocumentWriter[A]
  def read(bson: BSONDocument) = reader.read(bson)
  def write(a: A) = writer.write(a)
}

object BSONDocumentHandler {
  def apply[A](r: BSONDocumentReader[A], w: BSONDocumentWriter[A]): BSONDocumentHandler[A] =
    new BSONDocumentHandler[A] {
      val writer: BSONDocumentWriter[A] = w
      val reader: BSONDocumentReader[A] = r
    }
}