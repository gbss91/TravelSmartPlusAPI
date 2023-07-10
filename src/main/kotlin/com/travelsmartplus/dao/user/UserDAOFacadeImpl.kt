package com.travelsmartplus.dao.user

import com.travelsmartplus.models.*
import com.travelsmartplus.utils.DatabaseFactory.dbQuery
import com.travelsmartplus.utils.SaltedHash
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
                this.accountSetup = user.accountSetup
            }.toUser()
        }
    }

    override suspend fun editUser(id: Int, editedUser: User): User = dbQuery {
        val userEntity = UserEntity.findById(id) ?: throw NotFoundException("User not found")
        userEntity.apply {
            firstName = editedUser.firstName
            lastName = editedUser.lastName
            email = editedUser.email
            admin = editedUser.admin
            password = editedUser.password
            salt = editedUser.salt
            accountSetup = editedUser.accountSetup
            preferredAirlines = editedUser.preferredAirlines?.joinToString()
            preferredHotelChains = editedUser.preferredHotelChains?.joinToString()
        }
        userEntity.toUser()
    }

    override suspend fun updatePassword(id: Int, newPassword: SaltedHash) = dbQuery {
        val user = UserEntity.findById(id) ?: throw NotFoundException("User not found")
        user.password = newPassword.hash
        user.salt = newPassword.salt
    }

    override suspend fun deleteUser(id: Int) = dbQuery {
        val user = UserEntity.findById(id) ?: throw NotFoundException("User not found")
        user.delete()
    }
}
