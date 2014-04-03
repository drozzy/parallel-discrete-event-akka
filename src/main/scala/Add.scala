import akka.actor.ActorRef

/** Sends a "subscribe" message, with the target being the requesting actor.
 * Created by Andriy on 01-Apr-14.
 */
case class Add(target: ActorRef)