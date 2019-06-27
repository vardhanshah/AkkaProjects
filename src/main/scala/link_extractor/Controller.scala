package link_extractor
import akka.actor.{Actor, ActorLogging, ActorRef, OneForOneStrategy, Props, ReceiveTimeout, SupervisorStrategy, Terminated}
import scala.concurrent.duration._
object Controller {
  case class Check(url: String, depth: Int)
  case class Result(links: Set[String])
}

class Controller extends Actor {

  import Controller._

  override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 5) {
    case _: Exception => SupervisorStrategy.Restart
  }

  context.setReceiveTimeout(10 seconds)
    var cache = Set.empty[String]
  def getterProps(url: String, depth: Int): Props = Props(new Getter(url,depth))

  override def receive: Receive = {
    case Check(url,depth) =>
//      log.debug("{} checking {}", depth, url)
      if (!cache(url) && depth > 0)
        context.watch(context.actorOf(getterProps(url,depth-1)))
      cache += url
    case Terminated(_) =>
      if (context.children.isEmpty)
        context.parent ! Result(cache)
    case ReceiveTimeout =>
      context.children foreach context.stop
  }
}