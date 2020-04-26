package tomeksz.batch.transactions

import java.time.LocalDateTime

import com.holdenkarau.spark.testing.DatasetSuiteBase
import org.apache.spark.sql.{Encoder, Encoders}
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.expressions._
import tomeksz.IntegrationSpec

class BiggestAbsoluteTransactionReporterTest extends IntegrationSpec with DatasetSuiteBase {

  ignore should "get biggest absolute transaction for each user" in {
    import spark.implicits._

    // this is tmp workaround for not being able to provide kryo encoder for LocalDateTime (by an implicit like below, in lexical scope!)
    implicit val localDateTimeEncoder: Encoder[Transaction] = Encoders.kryo[Transaction]

    val reporter = new BiggestAbsoluteTransactionReporter(spark)

    Given("users")
    val users = sc.parallelize(Seq(
      User(1, "John Doe"),
      User(2, "Eric Paul"),
      User(3, "Third One")
    )).toDS

    And("transactions log, which does not contain any transaction for the Third One")
    val transactions = spark.createDataset(Seq(
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
    )).toDS
    assertDatasetEquals(expected, report)
  }

}
