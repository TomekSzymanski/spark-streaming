package streaming

class WordSummarizerTest extends IntegrationSpec {

  val summarizer = new WordSummarizer()

  ignore should "count total number of words for empty input" in {
    Given("empty input")
    val emptyInput = Seq(Seq.empty[String])
    val expectedOutput = Seq(Seq(WordSummary(0)))
    testOperation[String, WordSummary](emptyInput, summarizer.summarize _, expectedOutput, ordered = true)
  }

  ignore should "count total number of words for single empty input line" in {
    val emptyInput = Seq(Seq(""))
    val expectedOutput = Seq(Seq(WordSummary(1)))
    testOperation[String, WordSummary](emptyInput, summarizer.summarize _, expectedOutput, ordered = true)
  }

  ignore should "count total number of words in all inputs seen so far" in {
    Given("two windows")
    val emptyInput = Seq(Seq("hello Spark"), Seq("hello"))
    val expectedOutput = Seq(Seq(WordSummary(2)), Seq(WordSummary(3)))
    testOperation[String, WordSummary](emptyInput, summarizer.summarize _, expectedOutput, ordered = true)
  }

}
