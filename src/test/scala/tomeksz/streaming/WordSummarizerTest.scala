package tomeksz.streaming

import tomeksz.IntegrationSpec

class WordSummarizerTest extends IntegrationSpec {

  val summarizer = new WordSummarizer()

  it should "return empty RDD for no events" in {
    Given("No records within first window")
    val emptyInput = Seq(Seq.empty[String])
    val expectedOutput = Seq(Seq()) // empty RDD output from first window
    testOperation[String, WordSummary](emptyInput, summarizer.summarize _, expectedOutput, ordered = true)
  }

  it should "Return empty summary for single event not containing any word" in {
    Given("Single event within first window, but with not words")
    val emptyInput = Seq(Seq(""))
    val expectedOutput = Seq(Seq(WordSummary.empty()))
    testOperation[String, WordSummary](emptyInput, summarizer.summarize _, expectedOutput, ordered = true)
  }

  it should "Maintain running aggregate summary across the windows - single element windows" in {
    Given("two windows, each has one event")
    val emptyInput = Seq(Seq("hello Spark"), Seq("hello"))
    val expectedOutput = Seq(Seq(WordSummary(2, Set("hello", "Spark"))), Seq(WordSummary(3, Set("hello", "Spark"))))
    testOperation[String, WordSummary](emptyInput, summarizer.summarize _, expectedOutput, ordered = true)
  }

  it should "Maintain running aggregate summary across the windows - multiple events per window" in {
    Given("two windows, each has one event")
    val emptyInput = Seq(Seq("hello", "Spark"), Seq("hello"))
    val expectedOutput = Seq(Seq(WordSummary(2, Set("hello", "Spark"))), Seq(WordSummary(3, Set("hello", "Spark"))))
    testOperation[String, WordSummary](emptyInput, summarizer.summarize _, expectedOutput, ordered = true)
  }


}
