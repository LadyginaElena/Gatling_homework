package webtours


import io.gatling.core.Predef._
import io.gatling.http.Predef._


class MaxPerformance extends Simulation {
  setUp(
    CommonScenario().inject(
      // Интенсивность на ступень
      incrementUsersPerSec(10)
        // Количество ступеней
        .times(5)
        // Длительность полки
        .eachLevelLasting(5)
        // Длительность разгона
        .separatedByRampsLasting(10)
        // Начало нагрузки с 0 rps
        .startingFrom(0)
    )
  ).protocols(httpProtocol)
    // Общая длительность теста
    .maxDuration(60)
}
