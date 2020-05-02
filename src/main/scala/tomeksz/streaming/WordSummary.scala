package tomeksz.streaming

case class WordSummary private (wordsSoFarCount: Long, uniqueWordsSoFar: Set[String]) {
  require(wordsSoFarCount >= uniqueWordsSoFar.size)
  if (wordsSoFarCount > 0) {
    require(uniqueWordsSoFar.nonEmpty)
  }

  def +(other: WordSummary): WordSummary = {
    new WordSummary(wordsSoFarCount + other.wordsSoFarCount, uniqueWordsSoFar ++ other.uniqueWordsSoFar)
  }

}

object WordSummary {
  def empty(): WordSummary = new WordSummary(0, Set.empty)

  def apply(words: Seq[String]): WordSummary = new WordSummary(words.size, words.toSet)
}