package tomeksz.batch.transactions

sealed abstract case class TransactionType(symbol: String) extends Serializable

object MoneyPayment extends TransactionType("M")
object Transfer extends TransactionType("T")

object TransactionType {
  def apply(symbol: String): TransactionType = symbol match {
    case "M" => MoneyPayment
    case "T" => Transfer
    case s => throw new IllegalArgumentException("Cannot recognize transaction type symbol: " + s)
  }
}