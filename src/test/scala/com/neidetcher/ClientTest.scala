package com.neidetcher

import com.twitter.finagle.{ Service }
import com.twitter.finagle.http.Http

import com.twitter.util.{ Await, Future }
import java.net.InetSocketAddress
import org.jboss.netty.handler.codec.http._
import org.junit.Test
import com.twitter.finagle.builder.ClientBuilder
import com.twitter.util.{Duration, TimeLike}
import java.util.concurrent.TimeUnit._

class ClientTest {

  @Test
  def test() {
    //val client: Service[HttpRequest, HttpResponse] = Http.newService("www.google.com:80")

    val client: Service[HttpRequest, HttpResponse] = ClientBuilder()
      .codec(Http())
      .hosts("www.google.com:80")
      .hostConnectionLimit(10)
      .connectionTimeout(Duration(10, SECONDS))
      //.retries(2)
      .build()

    val request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/")
    val response: Future[HttpResponse] = client(request)
    response onSuccess { resp: HttpResponse =>
      println("GET success: " + resp)
      println(">>" + resp.getContent())
    } onFailure { exception: Throwable =>
      exception.printStackTrace()
      println("FAIL: " + exception.getMessage())
    }
    Await.ready(response)

  }

}