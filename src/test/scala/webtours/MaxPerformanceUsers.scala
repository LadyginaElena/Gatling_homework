package webtours

import io.gatling.core.Predef._

import scala.concurrent.duration.{DurationInt, FiniteDuration}


class MaxPerformanceUsers extends Simulation {
  val maxUsers = 20 //Максимальное количество пользователей
  val stagesNumber: Int = maxUsers // Количество ступеней (от 0 до 100% с шагом 10%)
  val stageDuration: FiniteDuration = 2.minutes // Длительность каждой ступени
  val rampDuration: FiniteDuration = 30.seconds // Время разгона между ступенями
  val testDuration: FiniteDuration = maxUsers * (stageDuration + rampDuration) // Общая длительность теста 25 минут

  setUp(
    CommonScenario().inject(      // Постепенно увеличиваем пользователей с 0 до maxUsers
      incrementUsersPerSec(1) // Добавляем пользователя каждую секунду
        .times(maxUsers) // Увеличиваем до maxUsers
        .eachLevelLasting(stageDuration) // Длительность каждой ступени
        .separatedByRampsLasting(rampDuration) // Время разгона
    )
  ).protocols(httpProtocol) // Протокол HTTP
    .maxDuration(testDuration) // Общая длительность теста
    .assertions(
      global.responseTime.mean.lt(3000), // Среднее время отклика менее 3 секунд
      global.failedRequests.percent.lt(5) // Процент ошибок менее 5%
    )
}

