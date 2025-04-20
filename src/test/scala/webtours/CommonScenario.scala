package webtours
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder



object CommonScenario {

  def apply(): ScenarioBuilder = new CommonScenario().scn

}

class CommonScenario {

  //  val myGroup = group("1 april"){
  //    exec(Actions.login)
  //  }

  val scn: ScenarioBuilder = scenario("otus 2025 webtours")
    .exec { session =>
      // Вывели в консоль только необходимый атрибут
      println(session("userSession").as[String])

      session}
    .feed(myFeed.users)
    .exec(Actions.getMain)
//    .exec(Actions.getUserSession)
    .exec(Actions.login)
    .exec(Actions.flights)
//    .exec(Actions.getCities)
    .exec(Actions.sendCities)
    .exec(Actions.choose_tickets)
}

//
////    .pause(5)
////    .pace(3)
//    .randomSwitch(
//      80.0 -> exec(Actions.courses),
//      20.0 -> exec(Actions.getNew)
//    )
