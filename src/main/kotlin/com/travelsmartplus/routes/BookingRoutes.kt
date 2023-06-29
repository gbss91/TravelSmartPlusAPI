package com.travelsmartplus.routes

import com.travelsmartplus.dao.airline.AirlineDAOFacadeImpl
import com.travelsmartplus.dao.airport.AirportDAOFacadeImpl
import com.travelsmartplus.dao.booking.BookingDAOFacadeImpl
import com.travelsmartplus.dao.hotel.HotelDAOFacadeImpl
import com.travelsmartplus.dao.user.UserDAOFacadeImpl
import com.travelsmartplus.models.Booking
import com.travelsmartplus.models.requests.BookingSearchRequest
import com.travelsmartplus.models.responses.HttpResponses
import com.travelsmartplus.services.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.ContentTransformationException
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Defines the booking routes for booking engine
 * @author Gabriel Salas.
 */

fun Route.bookingRoutes() {

    val bookingDAO = BookingDAOFacadeImpl()
    val userDAO = UserDAOFacadeImpl()
    val airportDAO = AirportDAOFacadeImpl()
    val airlineDAO = AirlineDAOFacadeImpl()
    val hotelDAO = HotelDAOFacadeImpl()
    val bookingService = BookingServiceFacadeImpl()
    val flightService = FlightBookingServiceFacadeImpl()
    val hotelService = HotelBookingServiceFacadeImpl()
    val placesService = PlacesServiceFacadeImpl()

    // Booking search
    post("/bookingSearch") {
        try {
            val request = call.receive<BookingSearchRequest>()
            val id = request.userId

            // Get user exists
            val user = userDAO.getUser(id) ?: throw NotFoundException()

            // Get predicted booking
            val predictedBooking = bookingService.newPredictedBooking(request, user)

            if (predictedBooking == null) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.OK, predictedBooking)
            }

        } catch (e: ContentTransformationException) {
            e.printStackTrace()
            call.respond(HttpStatusCode.BadRequest, HttpResponses.BAD_REQUEST)
        } catch (e: NotFoundException) {
            e.printStackTrace()
            call.respond(HttpStatusCode.NotFound, HttpResponses.NOT_FOUND)
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, HttpResponses.INTERNAL_SERVER_ERROR)
        }
    }

    // Get all airports
    get("/airports/all") {
        try {
            val airports = airportDAO.getAllAirports()
            call.respond(HttpStatusCode.OK, airports)
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, HttpResponses.INTERNAL_SERVER_ERROR)
        }
    }

    // Get all airlines
    get("/airlines/all") {
        try {
            val airlines = airlineDAO.getAllAirlines()
            call.respond(HttpStatusCode.OK, airlines)
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, HttpResponses.INTERNAL_SERVER_ERROR)
        }
    }

    // Get all hotels
    get("/hotels/all") {
        try {
            val hotels = hotelDAO.getAllHotels()
            call.respond(HttpStatusCode.OK, hotels)
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, HttpResponses.INTERNAL_SERVER_ERROR)
        }
    }

    // Fetch flight offers - For manual bookings
    post("/booking/flights") {
        try {
            val request = call.receive<BookingSearchRequest>()
            val flights = flightService.getFlights(request)

            call.respond(HttpStatusCode.OK, flights)

        } catch (e: ContentTransformationException) {
            e.printStackTrace()
            call.respond(HttpStatusCode.BadRequest, HttpResponses.BAD_REQUEST)
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, HttpResponses.INTERNAL_SERVER_ERROR)
        }
    }

    // Fetch hotel offers - For manual bookings
    post("/booking/hotels") {
        try {
            val request = call.receive<BookingSearchRequest>()
            val hotels = hotelService.getHotels(request)

            call.respond(HttpStatusCode.OK, hotels)

        } catch (e: ContentTransformationException) {
            e.printStackTrace()
            call.respond(HttpStatusCode.BadRequest, HttpResponses.BAD_REQUEST)
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, HttpResponses.INTERNAL_SERVER_ERROR)
        }

    }

    // Add a new booking
    post("/booking") {
        try {
            val request = call.receive<Booking>()
            val newBooking = bookingDAO.addBooking(request)

            call.respond(HttpStatusCode.OK, newBooking)

        } catch (e: ContentTransformationException) {
            e.printStackTrace()
            call.respond(HttpStatusCode.BadRequest, HttpResponses.BAD_REQUEST)
        } catch (e: NotFoundException) {
            e.printStackTrace()
            call.respond(HttpStatusCode.NotFound, HttpResponses.NOT_FOUND)
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, HttpResponses.INTERNAL_SERVER_ERROR)
        }
    }

    // Get all user's bookings - Access only to admins and owner
    get("bookings/{userId}") {
        try {
            val userId = call.parameters["userId"]?.toIntOrNull() ?: throw BadRequestException("Missing parameter")
            val requesterId = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString().toInt()// User ID in JWT Token
            val requester = userDAO.getUser(requesterId)

            // Check if user is owner and process bookings
            if (requester?.id == userId) {
                val allBookings = bookingDAO.getBookingsByUser(userId)
                val updatedBookings = placesService.processBookingsImage(allBookings)
                call.respond(HttpStatusCode.OK, updatedBookings)
            } else {
                call.respond(HttpStatusCode.Forbidden, HttpResponses.FORBIDDEN)
            }

        } catch (e: BadRequestException) {
            e.printStackTrace()
            call.respond(HttpStatusCode.BadRequest, HttpResponses.BAD_REQUEST)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, HttpResponses.FAILED_CREATE_USER)
        }

    }


    route("/booking/{id}") {
        // Get booking - Access only to admins and owner
        get {
            try {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Missing parameter")
                val requesterId = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString().toInt()// User ID in JWT Token
                val booking = bookingDAO.getBooking(id) ?: throw NotFoundException()

                // Check if user is owner or admin belongs to the same org
                if (requesterId == booking.user.id|| isAdminSameOrg(requesterId, booking.user.id!!)) {
                    call.respond(HttpStatusCode.OK, booking)
                } else {
                    call.respond(HttpStatusCode.Forbidden, HttpResponses.FORBIDDEN)
                }

            } catch (e: BadRequestException) {
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, HttpResponses.BAD_REQUEST)
            } catch (e: NotFoundException) {
                e.printStackTrace()
                call.respond(HttpStatusCode.NotFound, HttpResponses.NOT_FOUND)
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, HttpResponses.INTERNAL_SERVER_ERROR)
            }
        }

        // Delete booking - Access only to admins and owner
        delete {
            try {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Missing parameter")
                val requesterId = call.principal<JWTPrincipal>()!!.payload.getClaim("userId").asString().toInt()// User ID in JWT Token
                val booking = bookingDAO.getBooking(id) ?: throw NotFoundException()

                // Check if user is owner or admin belongs to the same org
                if (requesterId == booking.user.id|| isAdminSameOrg(requesterId, booking.user.id!!)) {
                    bookingDAO.deleteBooking(id)
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.Forbidden, HttpResponses.FORBIDDEN)
                }

            } catch (e: BadRequestException) {
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, HttpResponses.BAD_REQUEST)
            } catch (e: NotFoundException) {
                e.printStackTrace()
                call.respond(HttpStatusCode.NotFound, HttpResponses.NOT_FOUND)
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, HttpResponses.INTERNAL_SERVER_ERROR)
            }
        }
    }
}

// Check if the user is an admin in the same organization
private suspend fun isAdminSameOrg(requesterId: Int, userId: Int): Boolean {
    val requester = dao.getUser(requesterId)
    val user = dao.getUser(userId) ?: throw NotFoundException()
    return (requester?.admin == true) && (requester.orgId == user.orgId)
}