package tomeksz.batch.transactions

import java.time.LocalDateTime

import org.apache.spark.rdd.RDD

class UserLastTransactionReporter {

  def report(transactions: RDD[Transaction], users: RDD[User]): RDD[UserLastTransaction] = {
    val usersWithIds = users.map(u => (u.userId, u))

    val lastTransactions = transactions
      .mapPartitions(t => UserLastTransactionReporter.getLastTrx(t.toSeq).iterator)
      .reduceByKey((a,b) => if (a.dateTime.isAfter(b.dateTime)) a else b)
      .mapValues(_.amount)

    usersWithIds.leftOuterJoin(lastTransactions)
      .values
      .map { case (user, amountOption) => UserLastTransaction(user.fullName, amountOption.map(CreditTransaction)) }
  }
}

object UserLastTransactionReporter {

  case class TimestampedTransaction(amount: BigDecimal, dateTime: LocalDateTime)

  def getLastTrx(transactions: Seq[Transaction]): Map[Long, TimestampedTransaction] = {
    transactions
      .groupBy(_.userId)
      .mapValues(trxs => {
        val lastTrx = trxs.maxBy(_.dateTime)
        TimestampedTransaction(lastTrx.creditAmount, lastTrx.dateTime)
      })
  }

  implicit val localDateTimeOrdering: Ordering[LocalDateTime] = new Ordering[LocalDateTime] {
    override def compare(x: LocalDateTime, y: LocalDateTime): Int = x compareTo y
  }
}