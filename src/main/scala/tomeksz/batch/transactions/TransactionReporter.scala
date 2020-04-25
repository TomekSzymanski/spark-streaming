package tomeksz.batch.transactions

import java.time.LocalDateTime

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD

class TransactionReporter extends Serializable {

  def buildReport(transactionLines: RDD[String], users: Broadcast[Map[Long, User]]): TransactionSummary = {

    val userSummaries = transactionLines
      .map(parseTransactions)
      .mapPartitions(transactions => transactions.toSeq.groupBy(_.userId).mapValues(sumCredits).iterator)
      .reduceByKey(_ + _ )
      .map { case (userId, creditNet) => UserSummaryItem(users.value(userId).fullName, creditNet)}
      .collect().toSeq
    TransactionSummary(userSummaries)
  }

  private def sumCredits(transactions: Seq[Transaction]): BigDecimal = {
    transactions.map(_.creditAmount).sum
  }

  private def parseTransactions(line: String): Transaction = {
    val items = line.split(TransactionReporter.lineSeparator)
    require(items.size == 6, "input line does not contain required number of elements. Problem line: " + line)
    Transaction(
      userId = items(0).toLong,
      dateTime = LocalDateTime.parse(items(1)),
      creditAmount = BigDecimal(items(2)),
      otherPartyId = items(3).toLong,
      transactionType = TransactionType(items(4)),
      description = items(5)
    )
  }


}

object TransactionReporter {
  val lineSeparator: String = ","
}
