package demo
import akka.actor.{Actor, Props, Terminated}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
class timeOutMain extends Actor{
  val another_actor = context.actorOf(Props[CheckingTimeOut],"first")
  implicit val exec = context.dispatcher.asInstanceOf[ExecutionContext]
  context.system.scheduler.schedule(0.milliseconds,100.milliseconds,another_actor,"top notch")
  context.watch(another_actor)
  override def receive: Receive = {
    case Terminated(_) =>
      context.stop(self)
    case _ => println("fuck off")
  }
}
