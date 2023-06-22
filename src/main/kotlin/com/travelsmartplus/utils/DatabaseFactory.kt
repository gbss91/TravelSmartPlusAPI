package com.travelsmartplus.utils

import com.travelsmartplus.models.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * DatabaseFactory creates the database used in the project and uses [com.zaxxer.hikari]
 * to create a connection pool
 * @author Gabriel Salas
*/

object DatabaseFactory {

    private val dbUrl = System.getenv("DB_URL")
    private val dbUser = System.getenv("DB_USER")
    private val dbPassword = System.getenv("DB_PASSWORD")

    fun init() {
        Database.connect(hikari())
        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                Users,
                Orgs,
                Bookings,
                Flights,
                FlightSegments,
                FlightBookings,
                Airports,
                Airlines,
                Hotels,
                HotelBookings
            )
        }
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.postgresql.Driver"
        config.jdbcUrl = dbUrl
        config.username = dbUser
        config.password = dbPassword
        config.maximumPoolSize = 5
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }

    // Executes a database query within a transaction using the provided block
    suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }
}
