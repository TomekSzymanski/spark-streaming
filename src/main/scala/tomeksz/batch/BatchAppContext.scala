package tomeksz.batch

import com.typesafe.config.ConfigFactory
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession
import tomeksz.batch.transactions.{TransactionReporter, UsersLookupFetch}

object BatchAppContext {

  val appConfig = ConfigFactory.load()

  private val sparkConf : SparkConf =  new  SparkConf().setAppName("Spark Batch")

  val sc = new SparkContext(sparkConf)
  val sparkSession = SparkSession.builder().config(sparkConf).getOrCreate()

  private val usersRdd = sc.textFile(appConfig.getString("transactionReporting.users"))
  private val usersLookupFetch = new UsersLookupFetch()
  val usersLookupBroadcast = sc.broadcast(usersLookupFetch.load(usersRdd))

  val transactionsRdd = sc.textFile(appConfig.getString("transactionReporting.transactions"))
  val transactionReporter = new TransactionReporter()
}
