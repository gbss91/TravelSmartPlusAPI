package com.travelsmartplus.routes

import com.travelsmartplus.dao.org.OrgDAOFacadeImpl
import com.travelsmartplus.models.Org
<<<<<<< HEAD
import com.travelsmartplus.models.responses.HttpResponses.FAILED_CREATE_ORG
import com.travelsmartplus.models.responses.HttpResponses.FAILED_DELETE_ORG
import com.travelsmartplus.models.responses.HttpResponses.INTERNAL_SERVER_ERROR
=======
import com.travelsmartplus.models.responses.HttpResponses
import com.travelsmartplus.models.responses.HttpResponses.FAILED_CREATE_ORG
import com.travelsmartplus.models.responses.HttpResponses.FAILED_DELETE_ORG
import com.travelsmartplus.models.responses.HttpResponses.INTERNAL_SERVER_ERROR
import com.travelsmartplus.models.responses.HttpResponses.NOT_FOUND
>>>>>>> development
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

<<<<<<< HEAD
=======
/**
 * Defines the org  routes to get, add and delete orgs.
 * @author Gabriel Salas
 */

>>>>>>> development
fun Route.orgRoutes() {
    val dao = OrgDAOFacadeImpl()

    // Create Org
    post("/org") {
        try {
            val newOrg = call.receive<Org>()
            val org = dao.addOrg(newOrg)

            if (org == null) {
                call.respond(HttpStatusCode.InternalServerError, FAILED_CREATE_ORG)
                return@post
            }
            call.respond(HttpStatusCode.Created, org.id!!)
        } catch (e: Exception) {
<<<<<<< HEAD
=======
            e.printStackTrace()
>>>>>>> development
            call.respond(HttpStatusCode.InternalServerError, FAILED_CREATE_ORG)
        }
    }

    route("/org/{id}") {
        // Get Org
        get {
            try {
<<<<<<< HEAD
                val id = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
                val org = dao.getOrg(id) ?: throw NotFoundException()
                call.respond(HttpStatusCode.OK, org)
=======
                val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Missing parameter")
                val org = dao.getOrg(id) ?: throw NotFoundException()
                call.respond(HttpStatusCode.OK, org)
            } catch (e: BadRequestException) {
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, HttpResponses.BAD_REQUEST)
            } catch (e: NotFoundException) {
                e.printStackTrace()
                call.respond(HttpStatusCode.NotFound, NOT_FOUND)
>>>>>>> development
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, INTERNAL_SERVER_ERROR)
            }
        }

        // Delete Org
        delete {
            try {
<<<<<<< HEAD
                val id = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
                dao.deleteOrg(id)
                call.respond(HttpStatusCode.OK)
            } catch (e: Exception) {
=======
                val id = call.parameters["id"]?.toIntOrNull() ?: throw BadRequestException("Missing parameter")
                dao.deleteOrg(id)
                call.respond(HttpStatusCode.OK)
            } catch (e: BadRequestException) {
                e.printStackTrace()
                call.respond(HttpStatusCode.BadRequest, HttpResponses.BAD_REQUEST)
            } catch (e: NotFoundException) {
                e.printStackTrace()
                call.respond(HttpStatusCode.NotFound, NOT_FOUND)
            } catch (e: Exception) {
                e.printStackTrace()
>>>>>>> development
                call.respond(HttpStatusCode.InternalServerError, FAILED_DELETE_ORG)
            }
        }
    }
}
