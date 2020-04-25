package tomeksz.batch.transactions

import java.time.LocalDateTime

import com.holdenkarau.spark.testing.RDDComparisons
import tomeksz.IntegrationSpec

class UserLastTransactionReporterTest extends IntegrationSpec with RDDComparisons {

  val reporter = new UserLastTransactionReporter()

  it should "get last credit transaction for each user" in {
    Given("users")
    val users = sc.parallelize(Seq(
      User(1, "John Doe"),
      User(2, "Eric Paul"),
      User(3, "Third One")
    ))

    And("transactions log, which does not contain any transaction for the Third One")
    val transactions = sc.parallelize(Seq(
      new Transaction(1, LocalDateTime.parse("2020-01-10T00:00:00"), creditAmount = 100, 999),
      new Transaction(1, LocalDateTime.parse("2020-02-10T00:00:00"), creditAmount = 200, 999),
      new Transaction(2, LocalDateTime.parse("2020-02-10T00:00:00"), creditAmount = 50, 999)
    ))
    When("create report")
    val report = reporter.report(transactions, users)
    Then("Report contains all users and their last transaction if there was any")
    val expected = sc.parallelize(Seq(
      UserLastTransaction("John Doe", Some(CreditTransaction(200))),
      UserLastTransaction("Eric Paul", Some(CreditTransaction(50))),
      UserLastTransaction("Third One", None)
    ))
    compareRDD(expected, report) shouldBe 'empty

  }
}
