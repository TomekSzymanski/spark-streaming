package tomeksz.batch.transactions

import org.apache.spark.rdd.RDD

class UsersLookupFetch {

  def load(userLines: RDD[String]): Map[Long, User] = {
    userLines
      .map(UsersLookupFetch.parseUsers)
      .map(u => (u.userId, u))
      .collect()
      .toMap
  }
}

object UsersLookupFetch {
  private def parseUsers(line: String): User = {
    val items = line.split(TransactionReporter.LINE_SEPARATOR)
    User(userId = items(0).toLong, fullName = items(1))
  }
}
