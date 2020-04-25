package tomeksz.batch

case class WordSummary(frequencies: Map[String, Long],
                      totalWordCount: Long) {
}

object WordSummary {
  def empty(): WordSummary = {
    WordSummary(Map.empty, 0)
  }
}
