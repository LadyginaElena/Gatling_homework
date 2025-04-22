package webtours
import io.gatling.core.Predef._
import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}



object CommonScenario {

  def apply(): ScenarioBuilder = new CommonScenario().scn

}

class CommonScenario {

   val rootPage: ChainBuilder = group("root page and get userSession")
   {
     exec(Actions.getMain)
       .exec(Actions.getUserSession)
   }

  val loginGroup: ChainBuilder = group("login user and verify")
  {
     exec(Actions.login)
    .exec(Actions.verifyLogin)
  }

  val flights: ChainBuilder = group("get cities and choose tickets")
  {
     exec(Actions.flights)
    .exec(Actions.getCities)
    .exec(Actions.sendCities)
    .exec(Actions.choose_tickets)
  }


  val scn: ScenarioBuilder = scenario("otus 2025 webtours")
    .feed(myFeed.users)
    .exec(rootPage)
    .exec(loginGroup)
    .exec(flights)
    .exec(Actions.payment)
    .exec(Actions.singOff)
}

