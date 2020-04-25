package tomeksz.batch.transactions

sealed abstract class TransactionType extends Serializable

object MoneyPayment extends TransactionType
object Transfer extends TransactionType

object TransactionType {
  def apply(symbol: String): TransactionType = symbol match {
    case "M" => MoneyPayment
    case "T" => Transfer
    case s => throw new IllegalArgumentException("Cannot recognize transaction type symbol: " + s)
  }
}