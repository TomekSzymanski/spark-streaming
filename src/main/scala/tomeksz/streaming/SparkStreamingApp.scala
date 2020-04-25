package tomeksz.streaming

import StreamingAppContext._

object SparkStreamingApp extends App {
    wordSummarizer.summarize(kafkaStream).foreachRDD(rdd => println("num words: " + rdd.map(_.wordsCount).sum))

    ssc.start
    ssc.awaitTermination

}
