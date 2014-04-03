import akka.actor.{ActorSystem, Props, ActorRef}
import java.util.concurrent.TimeUnit
import akka.pattern.ask
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}
import ExecutionContext.Implicits.global
import scala.concurrent.duration._
import akka.util.Timeout
/**
 * Created by Andriy on 02-Apr-14.
 */
object Demo extends App{
  import Clock.{Start,Stop}
  implicit val timeout = Timeout(5.seconds)
  val system = ActorSystem("demo")
  // Create the 'clock' actor
  val clock = system.actorOf(Props[Clock], "clock")

  val ain = wire("ain", true)
  val bin = wire("bin", false)
  val cin = wire("cin", true)
  val sout = wire("sout", false)
  val cout = wire("cout", false)


  probe(ain)
  probe(bin)
  probe(cin)
  probe(sout)
  probe(cout)

  fullAdder(ain, bin, cin, sout, cout)

  println("Await start")
  Await.result( clock ? Clock.Start, 1.seconds)
  println("Await end")


  /*system.scheduler.scheduleOnce(Duration.create(50,TimeUnit.MILLISECONDS),
  clock, Clock.Start)
  system.scheduler.scheduleOnce(Duration.create(1450,TimeUnit.MILLISECONDS),
    clock, Clock.Tick)
*/
  def fullAdder(ain: ActorRef, bin: ActorRef, cin: ActorRef, sout: ActorRef, cout: ActorRef) ={
    system.actorOf(Props(new FullAdder(clock, ain, bin, cin, sout, cout)))
  }
  def halfAdder(a: ActorRef, b: ActorRef, s: ActorRef, c: ActorRef):ActorRef = {
    system.actorOf(Props(new HalfAdder(clock, a,b,s,c)))
  }
  def wire(name: String, init:Boolean):ActorRef={
    system.actorOf(Props(new Wire(clock, name, init)), name)
  }
  def probe(wire:ActorRef)={
    system.actorOf(Props(new Probe(clock, wire)))
  }

}
