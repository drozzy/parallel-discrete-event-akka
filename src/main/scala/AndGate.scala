import akka.actor.ActorRef

/**
 * Created by Andriy on 02-Apr-14.
 */
class AndGate(clock:ActorRef, in1:ActorRef, in2:ActorRef, out:ActorRef) extends Gate(clock, in1, in2, out) {
  override def computeOutput(s1: Boolean, s2: Boolean): Boolean = s1 && s2

  override val delay: Int = Circuit.AndGateDelay
}
