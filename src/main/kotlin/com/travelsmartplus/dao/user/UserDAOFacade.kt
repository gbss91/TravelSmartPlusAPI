package com.travelsmartplus.dao.user

import com.travelsmartplus.models.User

interface UserDAOFacade {
    suspend fun getUser(id: Int): User?
    suspend fun allUsers(orgId: Int): List<User>
    suspend fun getUserByEmail(email: String): User?
    suspend fun addUser(user: User): User?
    suspend fun editUser(id: Int, firstName: String, lastName: String, email: String, admin: Boolean, password: String, salt: String)
    suspend fun deleteUser(id: Int)
}
