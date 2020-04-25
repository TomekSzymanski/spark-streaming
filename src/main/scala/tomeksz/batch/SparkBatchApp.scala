package tomeksz.batch

import tomeksz.batch.BatchAppContext._
import tomeksz.batch.transactions.TransactionSummary

object SparkBatchApp extends App {
    val summary: TransactionSummary = transactionReporter.buildReport(transactionsRdd, usersRdd)
    println("TRANSACTIONS SUMMARY")
    println(summary)
}
