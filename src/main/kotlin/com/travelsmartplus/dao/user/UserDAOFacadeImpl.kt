package com.travelsmartplus.dao.user

import com.travelsmartplus.dao.DatabaseFactory.dbQuery
import com.travelsmartplus.models.*

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
        UserEntity.new {
            this.orgId = org
            this.firstName = user.firstName
            this.lastName = user.lastName
            this.email = user.email
            this.password = user.password
            this.salt = user.salt
        }.toUser()
    }


    override suspend fun editUser( id: Int, firstName: String, lastName: String, email: String, password: String, salt: String ) = dbQuery {
        UserEntity[id].firstName = firstName
        UserEntity[id].lastName= lastName
        UserEntity[id].email = email
        UserEntity[id].password = password
        UserEntity[id].salt = salt
    }

    override suspend fun deleteUser(id: Int) = dbQuery {
        UserEntity[id].delete()
    }
}