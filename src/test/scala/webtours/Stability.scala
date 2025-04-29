package webtours

import io.gatling.core.Predef._

import scala.concurrent.duration.{DurationInt, FiniteDuration}

class Stability extends Simulation{
  val intensity: Double = 1.94 // Максимально 2.42 RPS целевое значение нагрузки
  val stageDuration: FiniteDuration = 20.minutes // Длительность полки
  val rampDuration: FiniteDuration = 5.minutes // Время разгона
  val testDuration: FiniteDuration = stageDuration + rampDuration // Общая длительность разгон + полка  25 минут

  setUp(
    CommonScenario().inject(
      // Длительность разгона
      rampUsersPerSec(0)  to intensity.toDouble during rampDuration,
      // Длительность полки
      constantUsersPerSec(intensity) during stageDuration,
    ),
  ).protocols(httpProtocol)
    // Длительность теста = разгон + полка
    .maxDuration(testDuration)

}

