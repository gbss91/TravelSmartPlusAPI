package com.travelsmartplus.dao.user

import com.travelsmartplus.dao.hotel.HotelBookingFacade
import com.travelsmartplus.models.*
import com.travelsmartplus.utils.DatabaseFactory.dbQuery
import io.ktor.server.plugins.*

/**
 * Implementation of the [UserDAOFacade] interface.
 * This class provides methods to get, add and delete  user information from the database.
 * @author Gabriel Salas
 */

class UserDAOFacadeImpl : UserDAOFacade {

    override suspend fun getUser(id: Int): User? = dbQuery {
        UserEntity.findById(id)?.toUser()
    }

    override suspend fun allUsers(orgId: Int): List<User> = dbQuery {
        UserEntity.find { Users.orgId eq orgId }.map(UserEntity::toUser)
    }

    override suspend fun getUserByEmail(email: String): User? = dbQuery {
        UserEntity.find { Users.email eq email }.map(UserEntity::toUser).singleOrNull()
    }

    override suspend fun addUser(user: User): User? = dbQuery {
        val org = OrgEntity[user.orgId]

        if (UserEntity.find { Users.email eq user.email }.firstOrNull() != null) {
            null
        } else {
            UserEntity.new {
                this.orgId = org
                this.firstName = user.firstName
                this.lastName = user.lastName
                this.email = user.email
                this.admin = user.admin
                this.password = user.password
                this.salt = user.salt
            }.toUser()
        }
    }

    override suspend fun editUser(
        id: Int,
        firstName: String,
        lastName: String,
        email: String,
        admin: Boolean,
        password: String,
        salt: String
    ) = dbQuery {
        val user = UserEntity.findById(id) ?: throw NotFoundException("User not found")
        UserEntity[user.id].firstName = firstName
        UserEntity[user.id].lastName = lastName
        UserEntity[user.id].email = email
        UserEntity[user.id].admin = admin
        UserEntity[user.id].password = password
        UserEntity[user.id].salt = salt
    }

    override suspend fun deleteUser(id: Int) = dbQuery {
        val user = UserEntity.findById(id) ?: throw NotFoundException("User not found")
        user.delete()
    }
}
