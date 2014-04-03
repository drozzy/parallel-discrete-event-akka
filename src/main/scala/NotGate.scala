import Clock.{Pong, Ping}
import akka.actor.Actor.Receive
import akka.actor.{ActorIdentity, Identify, Actor, ActorRef}

/**
 * Created by Andriy on 02-Apr-14.
 */
class NotGate (clock: ActorRef, in:ActorRef, out:ActorRef) extends Actor{
  override def preStart(){
    clock ! Add(self)
    in ! Add(self)
  }

  // store input value
  var s1 = false

  def receive: Receive = {
    case Ping(time) =>
      clock ! Pong(time, self)

    case SignalChanged(wire, sig) =>
      s1 = sig
      clock ! AfterDelay(Circuit.InverterDelay, SetSignal(!s1), out)
  }
}
