package com.goldv.uploader

import java.util.concurrent.{TimeUnit, Executors}

import com.amazonaws.auth.AWSCredentials
import com.goldv.uploader.download.URLDownloader
import com.goldv.uploader.s3.S3Uploader

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by vince on 16/05/15.
 */
object Uploader extends App{

  val bucket = "felsenegg-cam"

  val scheduler = Executors.newSingleThreadScheduledExecutor()

  val credentials = new AWSCredentials{
    def getAWSAccessKeyId = args(0)
    def getAWSSecretKey = args(1)
  }


  val runnable = new Runnable {
    override def run() = {
      val downloader = new URLDownloader( List ("http://www.felsenegg.com/cam/cam02.jpg","http://www.felsenegg.com/cam/cam01.jpg") )

      val uploader = new S3Uploader(credentials)

      downloader.download.foreach{ inputs =>
        inputs.zipWithIndex.foreach{ case (input, idx) =>
          val name = s"cam$idx.jpg"
          println(s"Uploading $name")
          uploader.upload(bucket,name, input )
        }

      }
    }
  }

  scheduler.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.MINUTES)

}
