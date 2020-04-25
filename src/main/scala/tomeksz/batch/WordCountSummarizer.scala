package tomeksz.batch

import org.apache.spark.rdd.RDD

class WordCountSummarizer {

  def summarize(lines: RDD[String]): WordSummary = {
    val summary = lines
      .flatMap(_.split(" "))
      .groupBy(identity)
      .countByKey().toMap

    WordSummary(summary, summary.values.sum)
  }
}