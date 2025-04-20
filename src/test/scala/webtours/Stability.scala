package webtours

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class Stability extends Simulation{

  setUp(
    CommonScenario().inject(
      // Длительность разгона
      rampUsersPerSec(0) to 10 during 5,
      // Длительность полки
      constantUsersPerSec(10) during 5,
    ),
  ).protocols(httpProtocol)
    // Длительность теста = разгон + полка
    .maxDuration(60)

}

