package tomeksz.batch.transactions

import java.io.File

import tomeksz.IntegrationSpec

import scala.io.Source

class TransactionReportTest extends IntegrationSpec {

  val reporter = new TransactionReporter()

  it should "produce empty summary for no transactions input" in {
    Given("")
    def emptyTransactions = sc.parallelize(Seq.empty[String])
    def users = sc.parallelize(readFile("src/test/resources/users.csv"))
    When("")
    val report = reporter.buildReport(emptyTransactions, users)
    Then("")
    report.userSummaries shouldBe empty
  }

  it should "produce transactions summary" in {
    Given("")
    def trx = sc.parallelize(readFile("src/test/resources/transactions.csv"))
    def users = sc.parallelize(readFile("src/test/resources/users.csv"))
    When("")
    val report = reporter.buildReport(trx, users)
    Then("")
    report.userSummaries should have size 2
    report.userSummaries should contain (UserSummaryItem("John Smith", -1100))
    report.userSummaries should contain (UserSummaryItem("Eric Brown", 9800))
  }


  def readFile(fileName: String): Seq[String] = {
    Source.fromFile(fileName).getLines.toSeq.tail
  }

}
