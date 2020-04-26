package tomeksz.batch.transactions

case class MoneyTransfer(senderId: Long,
                         recipientId: Long,
                         amount: BigDecimal,
                         accepted: Boolean,
                         executedTimestamp: Long)