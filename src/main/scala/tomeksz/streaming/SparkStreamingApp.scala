package tomeksz.streaming

import StreamingAppContext._

object SparkStreamingApp extends App {
    wordSummarizer.summarize(kafkaStream).foreachRDD(rdd => println("Summary so far: " + rdd.reduce(_ + _)))

    ssc.start
    ssc.awaitTermination

}
