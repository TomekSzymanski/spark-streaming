package tomeksz.batch.transactions

import org.apache.spark.rdd.RDD

class UsersLookupFetch extends Serializable {

  def load(userLines: RDD[String]): Map[Long, User] = {
    userLines
      .map(parseUsers)
      .map(u => (u.userId, u))
      .collect()
      .toMap
  }

  private def parseUsers(line: String): User = {
    val items = line.split(TransactionReporter.lineSeparator)
    User(userId = items(0).toLong, fullName = items(1))
  }

}
