package link_extractor
import akka.actor.{Actor, Props, ReceiveTimeout}

import scala.concurrent.duration._

class Main extends Actor{

  import Receptionist._

  val receptionist = context.actorOf(Props[Receptionist], name="receptionist")
  context.watch(receptionist)

//  receptionist ! Get("http://wikipedia.com")
  receptionist ! Get("https://www.keras.io/")

  context.setReceiveTimeout(10.seconds)

  override def receive: Receive = {
    case Result(url, set) =>
      println(set.toVector.sorted.mkString(s"Results for '$url': \n", "\n", "\n"))
    case Failed(url, reason) =>
      println(s"Failed to fetch '$url': $reason\n")
    case ReceiveTimeout =>
      context.stop(self)
  }
  override def postStop(): Unit = {
    AsyncWebClient.shutdown()
  }
}
