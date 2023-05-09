package com.travelsmartplus

import com.travelsmartplus.fixtures.FlightFixtures
import com.travelsmartplus.fixtures.OrgFixture
import com.travelsmartplus.fixtures.UserFixture
import com.travelsmartplus.models.*
import com.travelsmartplus.models.requests.SignInRequest
import com.travelsmartplus.models.responses.AuthResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.time.toJavaDuration

object DatabaseTestHelper {
    fun setup() {
        TestDatabaseFactory.init()
        // Add test data
        transaction() {

            Orgs.batchInsert(OrgFixture.orgs) {org ->
                this[Orgs.orgName] = org.orgName
                this[Orgs.duns] = org.duns
            }

            Airports.batchInsert(FlightFixtures.airports) { airport ->
                this[Airports.airportName] = airport.airportName
                this[Airports.city] = airport.city
                this[Airports.country] = airport.country
                this[Airports.iataCode] = airport.iataCode
                this[Airports.icaoCode] = airport.icaoCode
                this[Airports.latitude] = airport.latitude
                this[Airports.longitude] = airport.longitude
            }

            Users.batchInsert(UserFixture.users) { user ->
                this[Users.orgId] = OrgEntity[user.orgId].id
                this[Users.firstName] = user.firstName
                this[Users.lastName] = user.lastName
                this[Users.email] = user.email
                this[Users.admin] = user.admin
                this[Users.password] = user.password
                this[Users.salt] = user.salt
            }

            FlightBookings.batchInsert(FlightFixtures.flightBookings) { booking ->
                this[FlightBookings.bookingReference] = booking.bookingReference
                this[FlightBookings.oneWay] = booking.oneWay
                this[FlightBookings.originCity] = booking.originCity
                this[FlightBookings.destinationCity] = booking.destinationCity
                this[FlightBookings.travelClass] = booking.travelClass
                this[FlightBookings.status] = booking.status
                this[FlightBookings.totalPrice] = booking.totalPrice
            }

            FlightSegments.batchInsert(FlightFixtures.flightSegments) { segment ->
                this[FlightSegments.flightBookingId] = FlightBookingEntity[1].id
                this[FlightSegments.direction] = segment.direction
                this[FlightSegments.duration] = segment.duration.toJavaDuration()
                this[FlightSegments.stops] = segment.stops
            }

            Flights.batchInsert(FlightFixtures.flights) {flight ->
                this[Flights.flightSegmentId] = FlightSegmentEntity[1].id
                this[Flights.departureIataCode] = flight.departureAirport.iataCode
                this[Flights.departureTime] = flight.departureTime.toJavaLocalDateTime()
                this[Flights.arrivalIataCode] = flight.arrivalAirport.iataCode
                this[Flights.arrivalTime] = flight.arrivalTime.toJavaLocalDateTime()
                this[Flights.carrierIataCode] = flight.carrierIataCode
            }
        }
    }

    fun cleanup() {
        // Clean the database and reset the auto-increment
        transaction {
            Orgs.deleteAll()
            Users.deleteAll()
            FlightBookings.deleteAll()
            FlightSegments.deleteAll()
            Flights.deleteAll()
            Airports.deleteAll()
            exec("ALTER SEQUENCE orgs_id_seq RESTART WITH 1;")
            exec("ALTER SEQUENCE users_id_seq RESTART WITH 1;")
            exec("ALTER SEQUENCE airports_id_seq RESTART WITH 1;")
            exec("ALTER SEQUENCE flightbookings_id_seq RESTART WITH 1;")
            exec("ALTER SEQUENCE flightsegments_id_seq RESTART WITH 1;")
            exec("ALTER SEQUENCE flights_id_seq RESTART WITH 1;")
        }
    }

    fun signIn(email: String, password: String): String {
        lateinit var token: String
        testApplication {
            application { module() }
            val signInRequest = SignInRequest(email, password)
            val response = client.post("api/signin") {
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(signInRequest))
            }
            token = Json.decodeFromString<AuthResponse>(response.body()).token
        }
        return token
    }
}
