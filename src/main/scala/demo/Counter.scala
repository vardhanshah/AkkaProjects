package demo
import akka.actor.Actor

class Counter extends Actor {
  //  var count = 0
  //
  //  override def receive: Receive = {
  //    case "incr" => count += 1
  //    case "get" => sender ! count
  //  }

  def counter(n: Int): Receive = {
    case "incr" =>
      context.become(counter(n + 1))
    case "decr" => context.become(counter(n-1))
    case "get" =>
      sender ! n
  }

  override def receive: Receive = {
//    case "decr" => println("decr")
//    case _ =>
    println("only one time occur")
    counter(0)
  }
}
