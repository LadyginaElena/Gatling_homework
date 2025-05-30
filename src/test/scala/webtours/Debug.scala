package webtours

import io.gatling.core.Predef._

class Debug extends Simulation{

  setUp(CommonScenario().inject(atOnceUsers(1)))
    .protocols(httpProtocol)
    .assertions(global.responseTime.max.lt(5000))
    .maxDuration(10)
}
