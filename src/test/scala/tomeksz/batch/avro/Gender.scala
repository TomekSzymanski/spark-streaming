package tomeksz.batch.avro

sealed abstract case class Gender(value: String) {
  def apply(value: String): Gender = value match {
    case "M" => Male
    case "F" => Female
  }
}

object Male extends Gender("M")
object Female extends Gender("F")
