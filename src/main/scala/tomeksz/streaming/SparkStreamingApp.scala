package tomeksz.streaming

import StreamingAppContext._
import org.apache.spark.streaming.Durations.seconds

object SparkStreamingApp extends App {
    wordSummarizer.summarize(kafkaStream)
      .foreachRDD(rdd => {
        if (!rdd.isEmpty()) {
            println("Summary so far: " + rdd.reduce(_ + _))
        }
    })

    kafkaStream
      .window(seconds(10))
      .foreachRDD(rdd => rdd.saveAsTextFile(config.getString("events.dump.file")))

    ssc.start
    ssc.awaitTermination

}
