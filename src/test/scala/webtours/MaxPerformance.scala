package webtours

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder

import scala.concurrent.duration.{DurationInt, FiniteDuration}


class MaxPerformance extends Simulation {
  val intensity: Double = 5.0// Максимально 5.0 RPS целевое значение нагрузки от 0 до момента деградации
  val stagesNumber = 10 // Количество ступеней (от 0 до 100% с шагом 10%)
  val stageDuration: FiniteDuration = 90.seconds // Длительность каждой ступени
  val rampDuration: FiniteDuration = 30.seconds // Время разгона между ступенями
  val testDuration: FiniteDuration = stagesNumber * (stageDuration + rampDuration) // Общая длительность теста 20 минут

  val scenario: ScenarioBuilder = CommonScenario()

  setUp(
    CommonScenario().inject(
      incrementUsersPerSec(intensity / stagesNumber) // интенсивность на ступень
        .times(stagesNumber) // количество ступеней
        .eachLevelLasting(stageDuration)
        .separatedByRampsLasting(rampDuration)
        .startingFrom(0)
    )
  ).protocols(httpProtocol)
    // Общая длительность теста
    .maxDuration(testDuration)
    .assertions(
      global.responseTime.max.lt(3000), // Среднее время отклика менее 3 секунд
      global.failedRequests.percent.lt(5) // Процент ошибок менее 5%
    )
}

