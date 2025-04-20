package webtours

import io.gatling.core.Predef._
import io.gatling.core.feeder.BatchableFeederBuilder
import io.gatling.http.Predef._

object myFeed {
  val users: BatchableFeederBuilder[String] = csv("users.csv").circular.eager
}
