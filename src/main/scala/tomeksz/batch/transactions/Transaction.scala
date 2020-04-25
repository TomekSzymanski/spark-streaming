package tomeksz.batch.transactions

import java.time.LocalDateTime

case class Transaction(userId: Long,
                       dateTime: LocalDateTime,
                       creditAmount: BigDecimal,
                       otherPartyId: Long,
                       transactionType: TransactionType,
                       description: String) {
  def this(userId: Long,
           dateTime: LocalDateTime,
           creditAmount: BigDecimal,
           otherPartyId: Long) = {
    this(userId, dateTime, creditAmount, otherPartyId, MoneyPayment, "")
  }
}
