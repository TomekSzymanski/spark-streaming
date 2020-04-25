package tomeksz.batch.transactions

case class UserLastTransaction(userName: String, lastTransactionAmount: Option[CreditTransaction])
