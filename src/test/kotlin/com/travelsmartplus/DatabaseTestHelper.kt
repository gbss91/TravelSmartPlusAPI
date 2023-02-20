package com.travelsmartplus

import com.travelsmartplus.dao.DatabaseFactory
import com.travelsmartplus.models.OrgEntity
import com.travelsmartplus.models.Orgs
import com.travelsmartplus.models.UserEntity
import com.travelsmartplus.models.Users
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
}
