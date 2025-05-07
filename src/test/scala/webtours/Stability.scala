package webtours

import io.gatling.core.Predef._

import scala.concurrent.duration.{DurationInt, FiniteDuration}

class Stability extends Simulation{
  val intensity: Double = 0.8*5.0// 80% процентов от максимальной нагрузки в 5.0 RPS
  val stageDuration: FiniteDuration = 20.minutes // Длительность полки
  val rampDuration: FiniteDuration = 5.minutes // Время разгона
  val testDuration: FiniteDuration = stageDuration + rampDuration // Общая длительность разгон + полка  25 минут
  val intensityStr: String = System.getProperty("intensity")

  println(s"Intensity string: '$intensityStr'")
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
    .assertions(
      global.responseTime.max.lt(10000))

}

