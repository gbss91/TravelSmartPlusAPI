package com.travelsmartplus

import com.travelsmartplus.dao.DatabaseFactory
import com.travelsmartplus.models.OrgEntity
import com.travelsmartplus.models.Orgs
import com.travelsmartplus.models.UserEntity
import com.travelsmartplus.models.Users
import com.travelsmartplus.models.requests.SignInRequest
import com.travelsmartplus.models.responses.AuthResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseTestHelper {

    fun setup() {
        DatabaseFactory.init() // Creates test database using environment variables
        transaction {
            // Add test data
            OrgEntity.new {
                orgName = "Test Company"
                duns = 123456789
            }
            UserEntity.new {
                orgId = OrgEntity[1]
                firstName = "John"
                lastName = "Doe"
                email = "john@test.com"
                admin = true
                password = "23646131f8752ab2e9d65345cfc7b5d515af4661a15ba749922cb2e674c36d9d" // myPass123
                salt = "e41ea5cc46b2b8b8099f81cd1e493bc6ad6f9d4d19fc149f37ca4ae154ba28f7"
            }
        }
    }

    fun cleanup() {
        // Clean the database and reset the auto-increment
        transaction {
            Orgs.deleteAll()
            Users.deleteAll()
            exec("ALTER SEQUENCE orgs_id_seq RESTART WITH 1;")
            exec("ALTER SEQUENCE users_id_seq RESTART WITH 1;")
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
