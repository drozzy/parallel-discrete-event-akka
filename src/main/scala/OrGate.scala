import akka.actor.ActorRef

/**
 * Created by Andriy on 01-Apr-14.
 */
class OrGate(clock:ActorRef, in1:ActorRef, in2:ActorRef, out:ActorRef) extends Gate(clock, in1, in2, out)
{
  override val delay: Int = Circuit.OrGateDelay

  override def computeOutput(s1: Boolean, s2: Boolean): Boolean = s1 || s2


}