import akka.actor.ActorRef

/** Wires inform the gates they are inputs-to to whatever state changes.
 * Created by Andriy on 01-Apr-14.
 */
case class SignalChanged(wire: ActorRef, sig: Boolean)
