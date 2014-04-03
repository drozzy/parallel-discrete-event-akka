import Clock.{Pong, Ping}
import akka.actor._


/**
 * Created by Andriy on 02-Apr-14.
 */
class Probe(clock: ActorRef, wire: ActorRef) extends Actor{

  override def preStart = {
    clock ! Add(self)
    wire ! Add(self)
  }
  override def receive: Receive = {
    case Ping(time) =>
      clock ! Pong(time, self)

    case SignalChanged(w, s) => println("signal " + w.path.name + " changed to " + s)
  }
}
