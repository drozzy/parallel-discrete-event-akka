import akka.actor.{ActorRef, Actor}
import akka.actor.Actor.Receive

/**
 * Created by Andriy on 01-Apr-14.
 */
class Clock extends Actor {
  import Clock.{Ping,Pong, Stop, Start}
  // Once the simulation is fully setup - clock is sent a Start message.
  // If frozen - safe to use regular methods.
  private var running = false

  private var currentTime = 0
  private var agenda: List[WorkItem] = List()

  private var allSimulants: List[ActorRef] = List()

  // Only advance to new time once the busy simulants is empty
  private var busySimulants: Set[ActorRef] = Set.empty

  // Used for setting up the simulation
  def add(sim: ActorRef) {
    allSimulants = sim :: allSimulants
  }

  // Tick a clock one time step - internal object
  case object Tick

  override def receive = {
    case Add(target) => add(target)
    case Tick =>
      if(running && busySimulants.isEmpty) { advance }


    case AfterDelay(delay, msg, target) => {
      val item = WorkItem(currentTime + delay, msg, target)
      agenda = insert(agenda, item)
    }
    case Pong(time, sim) => {

      assert(time == currentTime)
      assert(busySimulants contains sim)

      busySimulants -= sim
      // As soon as all simulations are done
      // We can advance to the next step
      if (busySimulants.isEmpty) self ! Tick
    }

    case Start => {
      running = true
      sender ! Pong(currentTime, self)

     self ! Tick
    }

    case Stop => {

      for (sim <- allSimulants) {
        sim ! Stop
      }
      context.stop(self)
    }
  }

  def insert(ag: List[WorkItem], item: WorkItem): List[WorkItem] = {
    if (ag.isEmpty || item.time < ag.head.time) item :: ag
    else ag.head :: insert(ag.tail, item)
  }

  def advance {
    if (agenda.isEmpty && currentTime > 0) {
      println("** Agenda empty. Clock exiting at time " + currentTime + ".")
      self ! Stop
    }
    else{
      currentTime += 1
      println("Advancing to time " + currentTime)
      processCurrentEvents
      for (sim <- allSimulants) {
        sim ! Ping(currentTime)
      }

      busySimulants = Set.empty ++ allSimulants
    }
  }

  private def processCurrentEvents = {

    val todoNow = agenda.takeWhile(_.time <= currentTime)
    agenda = agenda.drop(todoNow.length)
    for (WorkItem(time, msg, target) <- todoNow) {
      assert(time == currentTime)
      target ! msg
    }
  }



}

object Clock {

  case class Ping (time:Int)
  case class Pong (time:Int, from:ActorRef)
  case object Stop
  case object Start

}
