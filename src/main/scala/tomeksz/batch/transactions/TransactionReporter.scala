package tomeksz.batch.transactions

import java.time.LocalDateTime

import org.apache.spark.rdd.RDD

class TransactionReporter extends Serializable {

  def buildReport(transactionLines: RDD[String], userLines: RDD[String]): TransactionSummary = {
    val usersRdd = userLines
      .map(parseUsers)
      .map(u => (u.userId, u))

    val userSummaries = transactionLines
      .map(parseTransactions)
      .groupBy(_.userId)
      .mapValues(_.map(_.creditAmount).sum)
      .join(usersRdd)
      .mapValues { case (creditNet, user) => UserSummaryItem(user.fullName, creditNet) }
      .values
      .collect().toSeq
    TransactionSummary(userSummaries)
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

  private def parseUsers(line: String): User = {
    val items = line.split(TransactionReporter.lineSeparator)
    User(userId = items(0).toLong, fullName = items(1))
  }

}

object TransactionReporter {
  val lineSeparator: String = ","
}
