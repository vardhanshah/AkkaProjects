package link_extractor
import akka.actor.{Actor, ActorRef, Props, SupervisorStrategy, Terminated}

object Receptionist {

  private case class Job(client: ActorRef, url: String)
  case class Get(url: String)
  case class Result(url: String, links: Set[String])
  case class Failed(url: String, reason: String)
}

class Receptionist extends Actor {
  import Receptionist._

  override def supervisorStrategy: SupervisorStrategy = SupervisorStrategy.stoppingStrategy

  var reqNo = 0

  override def receive: Receive = waiting

  val waiting: Receive = {
    case Get(url) =>
      context.become(runNext(Vector(Job(sender,url))))
  }
  def runNext(jobs: Vector[Job]): Receive = {
    reqNo += 1
    if (jobs.isEmpty) waiting
    else {
      val controller = context.actorOf(Props[Controller], s"c$reqNo")
      controller ! Controller.Check(jobs.head.url, 2)
      running(jobs)
    }
  }

  def running(jobs: Vector[Job]): Receive = {
    case Controller.Result(links) =>
      val job = jobs.head
      job.client ! Result(job.url,links)
      context.stop(context.unwatch(sender))
      context.become(runNext(jobs.tail))
    case Terminated(_) =>
      val job = jobs.head
      job.client ! Failed(job.url,"controller failed unexpectedly")
      context.become(runNext(jobs.tail))
    case Get(url) =>
      context.become(enqueueJob(jobs, Job(sender,url)))
  }

  def enqueueJob(jobs: Vector[Job], job: Job): Receive = {
    if (jobs.size > 3) {
      sender ! Failed(job.url, "jobs overflow")
    running(jobs)
    }
    else running(jobs :+ job)
  }
}
