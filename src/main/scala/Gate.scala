import akka.actor.{ActorIdentity, Identify, ActorRef, Actor}
import Clock.{Ping,Pong}
/**
 * Created by Andriy on 01-Apr-14.
 */
abstract class Gate(clock:ActorRef, in1:ActorRef, in2:ActorRef, out:ActorRef) extends Actor{
  override def preStart(){
    clock ! Add(self)
    in1 ! Add(self)
    in2 ! Add(self)
  }

  // Propagation delay
  val delay: Int
  def computeOutput(s1: Boolean, s2: Boolean): Boolean

  // Store input states
  var s1, s2 = false

  override def receive:Receive  = {
    case Ping(time) =>
      clock ! Pong(time, self)

    case SignalChanged(wire, sig) =>
      if(wire == in1) s1 = sig
      if(wire == in2) s2 = sig
      clock ! AfterDelay(delay, SetSignal(computeOutput(s1, s2)), out)

  }


}
