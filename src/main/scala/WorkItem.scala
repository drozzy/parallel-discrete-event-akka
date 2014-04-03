import akka.actor.ActorRef

/**
 * Created by Andriy on 01-Apr-14.
 */
case class WorkItem (time: Int, msg: Any, target: ActorRef)

