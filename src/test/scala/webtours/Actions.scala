package webtours

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import java.time.LocalDate
import java.time.format.DateTimeFormatter


object Actions {

    val getMain: HttpRequestBuilder = http("get open root page")
      .get("/webtours")
      .check(status.is(301))
      .resources(
        http("get welcome page")
          .get("/cgi-bin/welcome.pl")
          .queryParam("signOff", true)
          .check(status.is(200))
      )

    val getUserSession: HttpRequestBuilder = http("get userSession value")
      .get("/cgi-bin/nav.pl")
      .queryParam("in", "home")
      .check(css("input[name='userSession']", "value").saveAs("USER_SESSION"))
      .check(status.is(200))

    val login: HttpRequestBuilder = http("user login - #{login}")
      .post("/cgi-bin/login.pl")
      .formParam("userSession", "#{USER_SESSION}")
      .formParam("username", "#{login}")
      .formParam("password", "#{password}")
      .check(status.is(200))

    val verifyLogin: HttpRequestBuilder = http("verify login user")
      .get("/cgi-bin/login.pl")
      .queryParam("intro", true)
      .check(status.is(200))

    val flights: HttpRequestBuilder = http("press flight button user")
      .get("/cgi-bin/welcome.pl")
      .queryParam("page", "search")
      .check(status.is(200))

    val getCities: HttpRequestBuilder = http("get cities")
      .get("/cgi-bin/reservations.pl")
      .queryParam("page", "welcome")
      .check(css("[name='depart'] option", "value").findRandom(2).saveAs("CITIES"))
      .check(status.is(200))

    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
    val today: LocalDate = LocalDate.now()

    val departDate: String = today.plusDays(1).format(formatter)
    val returnDate: String = today.plusDays(2).format(formatter)

    val sendCities:HttpRequestBuilder = http("send cities and get flights depart")
      .post("/cgi-bin/reservations.pl")
      .formParam("advanceDiscount", "0")
      .formParam("depart", "#{CITIES.random()}")
      .formParam("arrive", "#{CITIES.random()}")
      .formParam("departDate", departDate)
      .formParam("returnDate", returnDate)
      .formParam("numPassengers", "1")
      .formParam("seatPref", "None")
      .formParam("seatType", "Coach")
      .formParam("findFlights.x", "50")
      .formParam("findFlights.y", "14")
      .formParam(".cgifields", "roundtrip")
      .formParam(".cgifields", "seatType")
      .formParam(".cgifields", "seatPref")
      .check(css("input[name='outboundFlight']", "value").findRandom.saveAs("OUTBOUND_FLIGHT"))

    val choose_tickets:HttpRequestBuilder = http("choose tickets")
      .post("/cgi-bin/reservations.pl")
      .formParam("outboundFlight", "#{OUTBOUND_FLIGHT}")
      .formParam("numPassengers", "1")
      .formParam("advanceDiscount", "0")
      .formParam("seatType", "Coach")
      .formParam("seatPref", "None")
      .formParam("reserveFlights.x", "69")
      .formParam("reserveFlights.y", "9")
      .check(status.is(200))
      .check(css("input[name='outboundFlight']", "value").find.saveAs("OUTBOUND_FLIGHT_2"))

    val payment:HttpRequestBuilder = http("payment")
      .post("/cgi-bin/reservations.pl")
      .formParam("outboundFlight", "#{OUTBOUND_FLIGHT_2}")
      .formParam("advanceDiscount", "0")
      .formParam("returnFlight", "")
      .formParam("JSFormSubmit", "off")
      .formParam("buyFlights.x", "16")
      .formParam("buyFlights.y", "11")
      .formParam(".cgifields", "saveCC")
      .formParam("firstName", "")
      .formParam("lastName", "")
      .formParam("address1", "")
      .formParam("address2", "")
      .formParam("pass1", " ")
      .formParam("creditCard", "creditcard")
      .formParam("expDate", "expCa")
      .formParam("saveCC", "on")
      .formParam("oldCCOption", "on")
      .formParam("numPassengers", "1")
      .formParam("seatType", "Coach")
      .formParam("seatPref", "None")
      .check(status.is(200))

    val singOff:HttpRequestBuilder = http("go home page and logout")
      .get("cgi-bin/welcome.pl")
      .queryParam("signOff", 1)
      .check(status.is(200))
}
