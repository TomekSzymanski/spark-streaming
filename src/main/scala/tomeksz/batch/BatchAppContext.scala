package tomeksz.batch

import com.typesafe.config.ConfigFactory
import org.apache.spark.{SparkConf, SparkContext}
import tomeksz.batch.transactions.{TransactionReporter, UsersLookupFetch}

object BatchAppContext {

  val config = ConfigFactory.load()

  private val sparkConf : SparkConf =  new  SparkConf().setAppName("Spark Batch")

  val sc = new SparkContext(sparkConf)

  private val usersRdd = sc.textFile(config.getString("transactionReporting.users"))
  private val usersLookupFetch = new UsersLookupFetch()
  val usersLookupBroadcast = sc.broadcast(usersLookupFetch.load(usersRdd))

  val transactionsRdd = sc.textFile(config.getString("transactionReporting.transactions"))
  val transactionReporter = new TransactionReporter()
}
