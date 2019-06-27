package bank
import akka.actor.{Actor, Props}
import akka.event.LoggingReceive

class TransferMain extends Actor {
  val accountA = context.actorOf(Props[BankAccount], "accountA")
  val accountB = context.actorOf(Props[BankAccount], "accountB")

  accountA ! BankAccount.Deposit(100)

  override def receive: Receive = LoggingReceive {
    case BankAccount.Done =>
      transfer(50)
  }

  def transfer(amount: BigInt): Unit = {

    val transaction = context.actorOf(Props[WireTransfer], "transfer")
    transaction ! WireTransfer.Transfer(accountA,accountB,amount)
    context.become(LoggingReceive {
      case WireTransfer.Done =>
        println("success")
        context.stop(self)
      case WireTransfer.Failed =>
        println("failed")
        context.stop(self)
    })
  }
}
