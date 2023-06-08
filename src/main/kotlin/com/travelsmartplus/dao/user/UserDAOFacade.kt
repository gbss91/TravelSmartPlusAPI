package com.travelsmartplus.dao.user

import com.travelsmartplus.models.User
import com.travelsmartplus.utils.SaltedHash

interface UserDAOFacade {
    suspend fun getUser(id: Int): User?
    suspend fun allUsers(orgId: Int): List<User>
    suspend fun getUserByEmail(email: String): User?
    suspend fun addUser(user: User): User?
    suspend fun editUser(id: Int, editedUser: User): User
    suspend fun updatePassword(id: Int, newPassword: SaltedHash)
    suspend fun deleteUser(id: Int)
}
