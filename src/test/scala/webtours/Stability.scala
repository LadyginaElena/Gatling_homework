package webtours

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.{DurationInt, FiniteDuration}

class Stability extends Simulation{
  val intensity: Int = 80 // Максимально 80 RPS целевое значение нагрузки
  val stageDuration: FiniteDuration = 20.minutes // Длительность полки
  val rampDuration: FiniteDuration = 5.minutes // Время разгона
  val testDuration: FiniteDuration = stageDuration + rampDuration // Общая длительность разгон + полка  25 минут

  setUp(
    CommonScenario().inject(
      // Длительность разгона
      rampUsersPerSec(0)  to intensity during rampDuration,
      // Длительность полки
      constantUsersPerSec(intensity) during stageDuration,
    ),
  ).protocols(httpProtocol)
    // Длительность теста = разгон + полка
    .maxDuration(testDuration)

}

