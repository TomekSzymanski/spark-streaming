package tomeksz.batch.transactions

import java.time.LocalDateTime

case class Transaction(userId: Long,
                       dateTime: LocalDateTime,
                       creditAmount: BigDecimal,
                       otherPartyId: Long,
                       transactionType: TransactionType,
                       description: String) {

}
