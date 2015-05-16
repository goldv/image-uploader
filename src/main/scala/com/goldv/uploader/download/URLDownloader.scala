package com.goldv.uploader.download

import java.io.{ByteArrayInputStream, InputStream}

import com.ning.http.client.{AsyncHttpClient, AsyncHttpClientConfig}
import play.api.libs.iteratee.Iteratee
import play.api.libs.ws.ning.{NingAsyncHttpClientConfigBuilder, NingWSClient}
import play.api.libs.ws.{DefaultWSClientConfig, WSClient}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by vince on 16/05/15.
 */


class URLDownloader(urls: List[String]) {

  val config = new NingAsyncHttpClientConfigBuilder(DefaultWSClientConfig()).build()
  val builder = new AsyncHttpClientConfig.Builder(config)

  val ws:WSClient = new NingWSClient(builder.build())


  def download: Future[List[InputStream]] = {
    val streams = urls.map{ url =>
      for{
        (headers, body) <- ws.url(url).stream()
        consume <- body |>>>  Iteratee.consume[Array[Byte]]()
      } yield new ByteArrayInputStream(consume)
    }

    Future.sequence(streams)
  }


  def close = ws.underlying[AsyncHttpClient].close()



}
