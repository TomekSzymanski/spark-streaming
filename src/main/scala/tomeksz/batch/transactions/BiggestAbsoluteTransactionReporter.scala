package tomeksz.batch.transactions

import org.apache.spark.sql.{Dataset, SparkSession}

class BiggestAbsoluteTransactionReporter(sparkSession: SparkSession) {
  import sparkSession.implicits._

  def report(transactions: Dataset[Transaction], users: Dataset[User]): Dataset[UserLastTransaction] = {
    transactions.groupBy("userId").max("creditAmount").alias("maxTransactionAmount").show()
//    users.groupByKey(_.userId)
    Seq.empty[UserLastTransaction].toDS()
  }
}
