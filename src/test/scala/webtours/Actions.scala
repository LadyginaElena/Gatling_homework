package webtours

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import java.time.LocalDate
import java.time.format.DateTimeFormatter


object Actions {

    val getMain: HttpRequestBuilder = http("get open root page")
      .get("/webtours")
      .check(
          status.is(301), // проверка, что есть перенаправление
          header("Location").saveAs("redirectUrl"))
    //      .check(status.is(301))
//      .check(headerRegex("Set-Cookie","MSO=([^;]*);"))

    val getUserSession: HttpRequestBuilder = http("get userSession value")
      .get("#{redirectUrl}")
//      .queryParam("in", "home")
      .check(css("input[name='userSession']", "value").saveAs("USER_SESSION"))
      .check(status.is(200))

    val login: HttpRequestBuilder = http("user login - #{login} - userSession -#{USER_SESSION}")
      .post("cgi-bin/login.pl")
      .formParam("userSession", "#{USER_SESSION}")
      .formParam("username", "#{login}")
      .formParam("password", "#{password}")
      .check(
          status.is(301), // проверка, что есть перенаправление
          header("Location").saveAs("redirectUrl"))

    val flights: HttpRequestBuilder = http("press flight button user")
      .get("#{redirectUrl}")
//      .queryParam("page", "search")
      .check(css("[name='depart'] option", "value").findRandom(2).saveAs("CITIES"))
      .check(status.is(200))

    val getCities: HttpRequestBuilder = http("get cities")
      .get("cgi-bin/reservations.pl")
      .queryParam("page", "welcome")
      .check(css("[name='depart'] option", "value").findRandom(2).saveAs("CITIES"))
      .check(status.is(200))

    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
    val today: LocalDate = LocalDate.now()

    val departDate: String = today.plusDays(1).format(formatter)
    val returnDate: String = today.plusDays(2).format(formatter)

    val sendCities:HttpRequestBuilder = http("send cities and get flights depart - #{CITY[0]} arrive -#{CITY[1]}")
      .post("/cgi-bin/reservations.pl")
      .formParam("depart", "#{CITY[0]}")
      .formParam("arrive", "#{CITY[1]}")
      .formParam("departDate", departDate)
      .formParam("returnDate", returnDate)
      .check(css("input[name='outboundFlight']", "value").findRandom.saveAs("OUTBOUND_FLIGHT"))


    val choose_tickets:HttpRequestBuilder = http("choose tickets")
      .post("/cgi-bin/reservations.pl")
      .formParam("outboundFlight", "#{OUTBOUND_FLIGHT}")
      .check(status.is(200))


    val payment:HttpRequestBuilder = http("payment")
      .post("/cgi-bin/reservations.pl")
      .check(status.is(200))

    val singOff:HttpRequestBuilder = http("go home page and logout")
      .get("cgi-bin/welcome.pl")
      .queryParam("signOff", 1)
      .check(status.is(200))


}
