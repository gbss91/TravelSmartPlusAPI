package com.travelsmartplus.routes

import com.travelsmartplus.dao.org.OrgDAOFacadeImpl
import com.travelsmartplus.models.Org
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.orgRoutes() {
    val dao = OrgDAOFacadeImpl()

    // Create Org
    post("/org") {
        try {
            val newOrg = call.receive<Org>()
            val org = dao.addOrg(newOrg)

            if (org == null) {
                call.respond(HttpStatusCode.InternalServerError, "Failed to create organization")
                return@post
            }
            call.respond(HttpStatusCode.Created, org.id!!)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, "Failed to create organization: ${e.message}")
        }
    }

    route("/org/{id}") {
        // Get Org
        get {
            try {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
                val org = dao.getOrg(id) ?: throw NotFoundException()
                call.respond(HttpStatusCode.OK, org)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Failed to get organization: ${e.message}")
            }
        }

        // Delete Org
        delete {
            try {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
                dao.deleteOrg(id)
                call.respond(HttpStatusCode.OK)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Failed to delete organization: ${e.message}")
            }
        }
    }
}
