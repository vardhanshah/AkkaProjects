package demo
import bank._
import akka.actor.{Actor, ActorContext, Props}
class CounterMain extends Actor {

  val counter = context.actorOf(Props[Counter],"Counter")
  println(self.toString())
  counter ! "incr"
  counter ! "incr"
  counter ! "incr"
  counter ! "decr"
  counter ! "get"
  println(counter)
  override def receive: Receive = {
    case count: Int =>
      println(s"count was $count")
      context.stop(self)
  }
}
