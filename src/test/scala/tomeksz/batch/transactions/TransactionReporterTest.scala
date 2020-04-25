package tomeksz.batch.transactions

import tomeksz.IntegrationSpec

import scala.io.Source

class TransactionReporterTest extends IntegrationSpec {

  val reporter = new TransactionReporter()

  it should "produce empty summary for no transactions input" in {
    Given("")
    def emptyTransactions = sc.parallelize(Seq.empty[String])
    And("")
    val usersBroadcast = sc.broadcast(loadUserLookup)
    When("")
    val report = reporter.buildReport(emptyTransactions, usersBroadcast)
    Then("")
    report.userSummaries shouldBe empty
  }

  it should "produce transactions summary" in {
    Given("")
    def trx = sc.parallelize(readFile("src/test/resources/transactions.csv"))
    And("")
    val usersBroadcast = sc.broadcast(loadUserLookup)
    When("")
    val report = reporter.buildReport(trx, usersBroadcast)
    Then("")
    report.userSummaries should have size 2
    report.userSummaries should contain (UserSummaryItem("John Smith", -1100))
    report.userSummaries should contain (UserSummaryItem("Eric Brown", 9800))
  }

  def readFile(fileName: String): Seq[String] = {
    Source.fromFile(fileName).getLines.toSeq.tail
  }

  def loadUserLookup = {
    def users = sc.parallelize(readFile("src/test/resources/users.csv"))
    def userLookupFetch = new UsersLookupFetch()
    userLookupFetch.load(users)
  }

}
