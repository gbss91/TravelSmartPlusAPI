package com.travelsmartplus.routes

import com.travelsmartplus.dao.airport.AirportDAOFacadeImpl
import com.travelsmartplus.dao.booking.BookingDAOFacadeImpl
import com.travelsmartplus.dao.user.UserDAOFacadeImpl
import com.travelsmartplus.models.requests.BookingSearchRequest
import com.travelsmartplus.models.responses.HttpResponses
import com.travelsmartplus.services.BookingServiceFacadeImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
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
    val bookingService = BookingServiceFacadeImpl()

    post("/user/{id}/bookingSearch") {
        try {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Missing parameter")
            val request = call.receive<BookingSearchRequest>()

            // Get user exists
            val user = userDAO.getUser(id) ?: throw NotFoundException()

            // Get predicted booking
            val predictedBooking = bookingService.newPredictedBooking(request, user)

            if (predictedBooking == null) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.OK, predictedBooking)
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

    get("/airports/search") {
        try {
            val query = call.request.queryParameters["query"] ?: throw BadRequestException("Missing parameter")
            val airports = airportDAO.getAirportsQuery(query)
            call.respond(HttpStatusCode.OK, airports)

        } catch (e: BadRequestException) {
            e.printStackTrace()
            call.respond(HttpStatusCode.BadRequest, HttpResponses.BAD_REQUEST)
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, HttpResponses.INTERNAL_SERVER_ERROR)
        }
    }


}