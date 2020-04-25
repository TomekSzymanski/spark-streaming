package tomeksz.batch

import tomeksz.IntegrationSpec

class WordCountSummarizerTest extends IntegrationSpec {

  val summarizer = new WordCountSummarizer()

  it should "return zero summary for empty input" in {
    Given("empty input: no input lines")
    val emptyInput = sc.parallelize(Seq.empty[String])
    When("")
    val summary = summarizer.summarize(emptyInput)
    Then("")
    summary.totalWordCount shouldBe 0
    summary.frequencies shouldBe empty
  }

  it should "single line two words input" in {
    Given("empty input: no input lines")
    val singleLine = sc.parallelize(Seq("first second"))
    When("")
    val summary = summarizer.summarize(singleLine)
    Then("")
    summary.totalWordCount shouldBe 2
    And("summaries")
    summary.frequencies should have size 2
    summary.frequencies should contain ("first" -> 1)
    summary.frequencies should contain ("second" -> 1)
  }

  it should "multiple line input" in {
    Given("empty input: no input lines")
    val multiLinesInput = sc.parallelize(Seq(
      "first second",
      "third"
    ))
    When("")
    val summary = summarizer.summarize(multiLinesInput)
    Then("")
    summary.totalWordCount shouldBe 3
    And("summaries")
    summary.frequencies should have size 3
    summary.frequencies should contain ("first" -> 1)
    summary.frequencies should contain ("second" -> 1)
    summary.frequencies should contain ("third" -> 1)
  }
}
