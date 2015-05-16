package com.goldv.uploader.s3

import java.io.InputStream

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.regions.{Region, Regions}
import com.amazonaws.services.s3.AmazonS3Client

/**
 * Created by vince on 16/05/15.
 */
class S3Uploader(credentials: AWSCredentials) {

  val s3 = new AmazonS3Client(credentials)
  s3.setRegion(Region.getRegion(Regions.EU_WEST_1))


  def upload(bucket: String, key: String, input: InputStream) = {
    s3.putObject(bucket, key, input, null)
  }

}
