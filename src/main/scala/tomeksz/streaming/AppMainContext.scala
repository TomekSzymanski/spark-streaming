package tomeksz.streaming

import com.typesafe.config.ConfigFactory
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe

object AppMainContext {

  val config = ConfigFactory.load()

  private val sparkConf : SparkConf =  new  SparkConf()
    .setAppName("Spark Streaming")

  val ssc = new StreamingContext(sparkConf, Seconds(2))
  ssc.checkpoint("/tmp/spark_checkpoint")

  val kafkaParams = Map[String, Object](
    "bootstrap.servers" -> (config.getString("kafka.host") + ":" + config.getString("kafka.port")),
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "group.id" -> config.getString("kafka.consumerGroup"),
    "auto.offset.reset" -> "earliest"
  )

  val topics = Array(config.getString("kafka.topic"))
  val kafkaStream = KafkaUtils.createDirectStream[String, String](
    ssc,
    PreferConsistent,
    Subscribe[String, String](topics, kafkaParams)
  ).map(r => r.value())

  val wordSummarizer = new WordSummarizer()
}
