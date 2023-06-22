package com.travelsmartplus

import com.travelsmartplus.fixtures.*
import com.travelsmartplus.models.*
import com.travelsmartplus.models.requests.SignInRequest
import com.travelsmartplus.models.responses.AuthResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.time.toJavaDuration

/**
 * DatabaseTestHelper sets up the test database by adding necessary test data and provides a function
 * to clear the database after each test. It also provides sign in functionality for tests that required
 * authorisation.
 * @author Gabriel Salas
 */

object DatabaseTestHelper {
    fun setup() {
        TestDatabaseFactory.init()

        // Create mock data
        val bookings = BookingFixtures.createMockBookings()

        // Add test data
        transaction {

            Orgs.batchInsert(OrgFixture.orgs) { org ->
                this[Orgs.id] = org.id!!
                this[Orgs.orgName] = org.orgName
                this[Orgs.duns] = org.duns
            }

            Users.batchInsert(UserFixtures.users) { user ->
                this[Users.id] = user.id!!
                this[Users.orgId] = OrgEntity[user.orgId].id
                this[Users.firstName] = user.firstName
                this[Users.lastName] = user.lastName
                this[Users.email] = user.email
                this[Users.admin] = user.admin
                this[Users.password] = user.password
                this[Users.salt] = user.salt
                this[Users.accountSetup] = user.accountSetup
                this[Users.preferredAirlines] = user.preferredAirlines.toString()
                this[Users.preferredHotelChains] = user.preferredHotelChains.toString()
            }

            Airports.batchInsert(AirportFixtures.airports) { airport ->
                this[Airports.airportName] = airport.airportName
                this[Airports.city] = airport.city
                this[Airports.country] = airport.country
                this[Airports.iataCode] = airport.iataCode
                this[Airports.icaoCode] = airport.icaoCode
                this[Airports.latitude] = airport.latitude
                this[Airports.longitude] = airport.longitude
            }

            Airlines.batchInsert(AirlineFixtures.airlines) { airline ->
                this[Airlines.id] = airline.id
                this[Airlines.airlineName] = airline.airlineName
                this[Airlines.iataCode] = airline.iataCode
                this[Airlines.icaoCode] = airline.icaoCode
            }

            Hotels.batchInsert(HotelChainFixtures.hotelChains) { hotelChain ->
                this[Hotels.hotelChain] = hotelChain.hotelChain
                this[Hotels.code] = hotelChain.code
            }

            val hotelBookings = bookings.flatMap { listOf(it.hotelBooking) }.filterNotNull()
            HotelBookings.batchInsert(hotelBookings) { hotelBooking ->
                this[HotelBookings.hotelName] = hotelBooking.hotelName
                this[HotelBookings.hotelChainCode] = hotelBooking.hotelChainCode
                this[HotelBookings.address] = hotelBooking.address
                this[HotelBookings.checkInDate] = hotelBooking.checkInDate.toJavaLocalDate()
                this[HotelBookings.checkOutDate] = hotelBooking.checkOutDate.toJavaLocalDate()
                this[HotelBookings.roomType] = hotelBooking.roomType
                this[HotelBookings.rate] = hotelBooking.rate
                this[HotelBookings.totalPrice] = hotelBooking.totalPrice
                this[HotelBookings.latitude] = hotelBooking.latitude
                this[HotelBookings.longitude] = hotelBooking.longitude
            }

            val flightBookings = bookings.flatMap { listOf(it.flightBooking) }
            FlightBookings.batchInsert(flightBookings) { booking ->
                this[FlightBookings.bookingReference] = booking.bookingReference
                this[FlightBookings.oneWay] = booking.oneWay
                this[FlightBookings.originCity] = booking.originCity
                this[FlightBookings.destinationCity] = booking.destinationCity
                this[FlightBookings.travelClass] = booking.travelClass
                this[FlightBookings.status] = booking.status
                this[FlightBookings.totalPrice] = booking.totalPrice
            }

            flightBookings.forEach { booking ->
                val segments = booking.segments
                FlightSegments.batchInsert(segments) { segment ->
                    this[FlightSegments.flightBookingId] = booking.id!!
                    this[FlightSegments.direction] = segment.direction
                    this[FlightSegments.duration] = segment.duration.toJavaDuration()
                    this[FlightSegments.stops] = segment.stops
                }

                val insertedSegments = FlightSegmentEntity.find { FlightSegments.flightBookingId eq booking.id }.map { it.toFlightSegment() }

                segments.forEachIndexed { index, segment ->
                    val insertedSegment = insertedSegments[index]
                    val flights = segment.flights
                    Flights.batchInsert(flights) { flight ->
                        this[Flights.flightSegmentId] = insertedSegment.id!!
                        this[Flights.departureIataCode] = flight.departureAirport.iataCode
                        this[Flights.departureTime] = flight.departureTime.toJavaLocalDateTime()
                        this[Flights.arrivalIataCode] = flight.arrivalAirport.iataCode
                        this[Flights.arrivalTime] = flight.arrivalTime.toJavaLocalDateTime()
                        this[Flights.carrierIataCode] = flight.carrierIataCode
                    }
                }
            }

            Bookings.batchInsert(bookings) { booking ->
                this[Bookings.userId] = 1
                this[Bookings.orgId] = booking.user.orgId
                this[Bookings.originIata] = booking.origin.iataCode
                this[Bookings.destinationIata] = booking.destination.iataCode
                this[Bookings.departureDate] = booking.departureDate.toJavaLocalDate()
                this[Bookings.returnDate] = booking.returnDate?.toJavaLocalDate()
                this[Bookings.flightBookingId] = booking.flightBooking.id!!
                this[Bookings.hotelBookingId] = booking.hotelBooking?.id
                this[Bookings.adultsNumber] = booking.adultsNumber
                this[Bookings.status] = booking.status
                this[Bookings.totalPrice] = booking.totalPrice
            }
        }
    }

    fun cleanup() {
        // Clean the database and reset the auto-increment
        transaction {
            Orgs.deleteAll()
            Users.deleteAll()
            HotelBookings.deleteAll()
            FlightBookings.deleteAll()
            FlightSegments.deleteAll()
            Flights.deleteAll()
            Airports.deleteAll()
            Airlines.deleteAll()
            Hotels.deleteAll()
            Bookings.deleteAll()

            exec("ALTER SEQUENCE orgs_id_seq RESTART WITH 1;")
            exec("ALTER SEQUENCE users_id_seq RESTART WITH 1;")
            exec("ALTER SEQUENCE airports_id_seq RESTART WITH 1;")
            exec("ALTER SEQUENCE airlines_id_seq RESTART WITH 1;")
            exec("ALTER SEQUENCE hotels_id_seq RESTART WITH 1;")
            exec("ALTER SEQUENCE hotelbookings_id_seq RESTART WITH 1;")
            exec("ALTER SEQUENCE flightbookings_id_seq RESTART WITH 1;")
            exec("ALTER SEQUENCE flightsegments_id_seq RESTART WITH 1;")
            exec("ALTER SEQUENCE flights_id_seq RESTART WITH 1;")
            exec("ALTER SEQUENCE bookings_id_seq RESTART WITH 1;")
        }
    }

    // Signs user in for tests that required authentication
    fun signIn(email: String, password: String): String {
        lateinit var token: String
        testApplication {
            application { testModule() }
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
