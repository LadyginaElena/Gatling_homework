package webtours

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder

import scala.concurrent.duration.{DurationInt, FiniteDuration}


class MaxPerformance extends Simulation {
  val intensity =100 // Максимально 100 RPS целевое значение нагрузки
  val stagesNumber = 10 // Количество ступеней (от 0 до 100% с шагом 10%)
  val stageDuration: FiniteDuration = 2.minutes // Длительность каждой ступени
  val rampDuration: FiniteDuration = 30.seconds // Время разгона между ступенями
  val testDuration: FiniteDuration = stagesNumber * (stageDuration + rampDuration) // Общая длительность теста 25 минут

  val scenario: ScenarioBuilder = CommonScenario()

  // Собираем все ступени вместе
  setUp(
    CommonScenario().inject(
      incrementUsersPerSec((intensity / stagesNumber).toDouble)
        .times(stagesNumber)
        .eachLevelLasting(stageDuration)
        .separatedByRampsLasting(rampDuration)
        .startingFrom(0)
    )
  ).protocols(httpProtocol)
    // Общая длительность теста
    .maxDuration(testDuration)
    .assertions(
      global.responseTime.mean.lt(3000), // Среднее время отклика менее 3 секунд
      global.failedRequests.percent.lt(5) // Процент ошибок менее 5%
    )
}
