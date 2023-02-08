package com.travelsmartplus.dao.user

import com.travelsmartplus.models.*

interface UserDAOFacade {
    suspend fun allUsers(): List<User>
    suspend fun getUserByEmail(email: String): User?
    suspend fun addUser(orgId: Int, firstName: String, lastName: String, email: String, password: String, salt: String): User?
    suspend fun editUser(id: Int, orgId: Int, firstName: String, lastName: String, email: String, password: String, salt: String): Boolean
    suspend fun deleteUser(id: Int): Boolean

}