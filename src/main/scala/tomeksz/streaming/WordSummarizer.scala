package tomeksz.streaming

import org.apache.spark.streaming.dstream.DStream

class WordSummarizer() extends Serializable {
  def summarize(lines: DStream[String]): DStream[WordSummary] = {
    lines
      .flatMap(_.split(" "))
      .count()
      .map(cnt => WordSummary(cnt))
//      .reduceByKey((s1, s2) => WordSummary(s1.wordsCount + s2.wordsCount))
//      .updateStateByKey(updateWordSummary _)
//      .map(_._1)
  }

//  private def updateWordSummary(nextSummary: Seq[WordSummary], state: Option[WordSummary]): Option[WordSummary] = {
//    val nextWordsCnt = nextSummary.map(_.wordsCount).sum
//    println("CCC: " + nextWordsCnt)
//    val updatedWordsCnt = state.map(_.wordsCount).getOrElse(0L) + nextWordsCnt
//    println("STATE: " + state.nonEmpty)
//    println("DDD: " + updatedWordsCnt)
//    Some(WordSummary(updatedWordsCnt))
//  }
}
