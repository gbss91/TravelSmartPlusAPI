package com.travelsmartplus.dao.user

import com.travelsmartplus.models.User
import com.travelsmartplus.models.Users
import org.jetbrains.exposed.sql.ResultRow

class UserDAOFacadeImpl : UserDAOFacade {

    //Extracts data from row and create User object
    private fun resultRowToUser(row: ResultRow) = User(
        id = row[Users.id],
        orgId = row[Users.orgId],
        firstName = row[Users.firstName],
        lastName = row[Users.lastName],
        email = row[Users.email],
        password = row[Users.password],
        salt = row[Users.salt]
    )

    override suspend fun allUsers(): List<User> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserByEmail(email: String): User? {
        TODO("Not yet implemented")
    }

    override suspend fun addUser(
        orgId: Int,
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        salt: String
    ): User? {
        TODO("Not yet implemented")
    }

    override suspend fun editUser(
        id: Int,
        orgId: Int,
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        salt: String
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(id: Int): Boolean {
        TODO("Not yet implemented")
    }
}