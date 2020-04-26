package tomeksz.batch.transactions

import com.holdenkarau.spark.testing.DatasetSuiteBase
import tomeksz.IntegrationSpec
import org.apache.spark.sql.functions._

class DataSetApiBasicsTest extends IntegrationSpec with DatasetSuiteBase {

  it should "simple operations" in {
    import spark.implicits._

    val users = sc.parallelize(Seq(
      User(1, "John Doe"),
      User(2, "Eric Paul"),
      User(3, "Third One")
    )).toDS

    val transfers = spark.createDataset(Seq(
      MoneyTransfer(1, 2, 1000, true, 1001),
      MoneyTransfer(1, 2, 2000, true, 1005),
      MoneyTransfer(2, 3, 500, true, 1002),
      MoneyTransfer(3, 1, 100, true, 1003)
    ))

    val largestIncomingTransfersPerUser = transfers
      .groupBy(col("recipientId").as("userId"))
      .agg(max("amount").as("maxAmount"))
      .sort("userId")
      .as[UserMaxTransfers]

    val expected = spark.createDataset(Seq(
      UserMaxTransfers(1, 100),
      UserMaxTransfers(2, 2000),
      UserMaxTransfers(3, 500)
    ))

    assertDatasetEquals(largestIncomingTransfersPerUser , expected )
  }

}
