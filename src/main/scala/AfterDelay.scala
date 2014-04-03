import akka.actor.ActorRef

/**
 * Execute after a delay.
 * Created by Andriy on 01-Apr-14.
 */
case class AfterDelay (delay: Int, msg: Any, target: ActorRef)
