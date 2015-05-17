package com.goldv.uploader

import java.io.File
import java.util.concurrent.{TimeUnit, Executors}

import com.amazonaws.auth.AWSCredentials
import com.goldv.uploader.download.URLDownloader
import com.goldv.uploader.s3.S3Uploader
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory
import scala.collection.JavaConversions._


import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by vince on 16/05/15.
 */
object Uploader extends App{

  val log = LoggerFactory.getLogger(Uploader.getClass)
  val config = ConfigFactory.parseFile(new File(args(0)))

  val bucket = config.getString("bucket")
  val interval = config.getDuration("interval", TimeUnit.MINUTES)

  val scheduler = Executors.newSingleThreadScheduledExecutor()


  val credentials = new AWSCredentials{
    def getAWSAccessKeyId = config.getConfig("aws").getString("public-key")
    def getAWSSecretKey = config.getConfig("aws").getString("secret-key")
  }


  val runnable = new Runnable {
    override def run() = {
      val downloader = new URLDownloader( config.getStringList("urls").toList )

      val uploader = new S3Uploader(credentials)

      downloader.download.foreach{ inputs =>
        inputs.zipWithIndex.foreach{ case (input, idx) =>
          val name = s"cam$idx.jpg"
          log.info(s"Uploading $name")
          uploader.upload(bucket,name, input )
        }

      }
    }
  }

  scheduler.scheduleAtFixedRate(runnable, 0, interval, TimeUnit.MINUTES)

}
