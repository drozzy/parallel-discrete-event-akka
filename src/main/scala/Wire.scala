
import akka.actor.{ActorIdentity, Identify, Actor, ActorRef}
import Clock.{Ping,Pong,Stop}
//import akka.actor.Actor.Receive
//import akka.actor.ActorRef
//
///**
// * Created by Andriy on 01-Apr-14.
// */
class Wire(clock:ActorRef, name:String, init: Boolean) extends Actor{
  def this(clock:ActorRef, name:String) {this(clock, name, false)}
  def this(clock:ActorRef){this(clock, "unnamed")}

  override def preStart(){
    clock ! Add(self)
  }

  private var sigVal = init
  private var observers: List[ActorRef] = List()

  def receive: Receive  = {
    case Add(target: ActorRef) => addObserver(target)

    case Ping(time) =>
      // Send out the initial wire value
      if (time == 1) { signalObservers(clock) }
      clock ! Pong(time, self)

    case SetSignal(s) =>
      println("Seeting signal")
      if (s != sigVal){
        sigVal = s
        signalObservers(clock)
      }
    case Stop => context.stop(self)
  }

  // Indicate to the observers that the value changed
  def signalObservers(clock: ActorRef){
    for(obs <- observers)
      clock ! AfterDelay(Circuit.WireDelay, SignalChanged(self, sigVal), obs )
  }

  def addObserver(obs: ActorRef){
    observers = obs::observers
  }

  override def toString = "Wire(" + name + ")"

}

