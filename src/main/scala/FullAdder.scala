import akka.actor.{ActorRef, Actor, Props}

/**
 * Created by Andriy on 02-Apr-14.
 */
class FullAdder(clock:ActorRef, a: ActorRef, b: ActorRef, cin: ActorRef, sum: ActorRef, cout: ActorRef) extends Actor{
  val s, c1, c2 = context.actorOf(Props(classOf[Wire], clock))

  context.actorOf(Props(classOf[HalfAdder], clock, a, cin, s, c1))
  context.actorOf(Props(classOf[HalfAdder], clock, b, s, sum, c2))
  orGate(c1, c2, cout)

  def orGate(s1: ActorRef, s2: ActorRef, out: ActorRef):ActorRef = {
    context.actorOf(Props(classOf[OrGate], clock, s1, s2, out))
  }



  override def receive: Receive = {
    case _ => Unit
  }
}
