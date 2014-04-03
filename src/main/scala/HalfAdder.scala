import akka.actor.Actor.Receive
import akka.actor.{ActorRef, Actor, Props, ActorSystem}

/**
 * Created by Andriy on 02-Apr-14.
 */
class HalfAdder(clock:ActorRef, a: ActorRef, b: ActorRef, s: ActorRef, c: ActorRef) extends Actor{
  val d, e = context.actorOf(Props(classOf[Wire], clock))
  context.actorOf(Props(new OrGate(clock, a, b, d)))
  context.actorOf(Props(new AndGate(clock, a, b, c)))
  context.actorOf(Props(new NotGate(clock, c, e)))
  context.actorOf(Props(new AndGate(clock, d, e, s)))



  override def receive: Receive = {
    case _ => Unit
  }
}
