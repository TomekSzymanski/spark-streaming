package tomeksz.streaming

import org.apache.spark.streaming.dstream.DStream

class WordSummarizer() extends Serializable {
  def summarize(lines: DStream[String]): DStream[WordSummary] = {
    lines
      .map(_.split(" ").filter(_.nonEmpty))
      .map(words => (WordSummary.getClass.getSimpleName, WordSummary(words)))
      .updateStateByKey(updateWordSummary)
      .reduceByKey(_ + _)
      .map(_._2)
  }

  private def updateWordSummary(nextWords: Seq[WordSummary], state: Option[WordSummary]): Option[WordSummary] = {
    val updatedSummary = state.getOrElse(WordSummary.empty()) + nextWords.reduce(_ + _)
    Some(updatedSummary)
  }
}
