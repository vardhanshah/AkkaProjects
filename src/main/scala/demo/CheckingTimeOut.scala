package demo
import akka.actor.{Actor, ReceiveTimeout}

import scala.concurrent.duration._

class CheckingTimeOut extends Actor{

  context.setReceiveTimeout(1.milliseconds)
  private var x = 3
  override def receive: Receive = {
    case ReceiveTimeout =>
      if (x==0)
        context.stop(self)
      x -= 1
      println("Received Timeout " + x)
    case "top notch" =>
      println("received top notch")
      if (x==0) {
        context.stop(self)
      }
      x -= 1
      println("still fucking running",x)

  }
}
