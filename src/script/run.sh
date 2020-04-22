#!/bin/bash

spark-submit \
	--master local[2] \
	--files application.conf,log4j.properties \
	--conf "spark.executor.extraJavaOptions=-XX:+UseParallelGC -Dlog4j.configuration=file:log4j.properties" \
	--conf "spark.driver.extraJavaOptions=-Dlog4j.configuration=file:log4j.properties" \
	--class streaming.SparkStreamingApp \
	./spark-streaming-assembly-1.0-SNAPSHOT.jar
