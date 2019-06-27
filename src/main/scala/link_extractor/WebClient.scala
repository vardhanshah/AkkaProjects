package link_extractor
import java.util.concurrent.Executor

import com.ning.http.client.AsyncHttpClient

import scala.concurrent.{Future, Promise}
case class BadStatus(i: Int) extends RuntimeException

trait WebClient {
  def get(url: String)(implicit exec: Executor): Future[String]
}

object AsyncWebClient extends WebClient {

  private val client = new AsyncHttpClient()

  def get(url: String)(implicit exec: Executor): Future[String] = {
    val f = client.prepareGet(url).execute()
    val p = Promise[String]()
    f.addListener(new Runnable {
      override def run(): Unit = {
        val response = f.get
        if (response.getStatusCode < 400)
          p.success(response.getResponseBodyExcerpt(131072))
        else p.failure(BadStatus(response.getStatusCode))
      }
      }, exec)
    p.future
  }

  def shutdown(): Unit = client.close()
}

