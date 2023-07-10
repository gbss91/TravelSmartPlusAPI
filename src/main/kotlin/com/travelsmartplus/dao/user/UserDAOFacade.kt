package com.travelsmartplus.dao.user

<<<<<<< HEAD
import com.travelsmartplus.models.*
=======
import com.travelsmartplus.models.User
import com.travelsmartplus.utils.SaltedHash
>>>>>>> development

interface UserDAOFacade {
    suspend fun getUser(id: Int): User?
    suspend fun allUsers(orgId: Int): List<User>
    suspend fun getUserByEmail(email: String): User?
    suspend fun addUser(user: User): User?
<<<<<<< HEAD
    suspend fun editUser(id: Int, firstName: String, lastName: String, email: String, admin: Boolean, password: String, salt: String)
=======
    suspend fun editUser(id: Int, editedUser: User): User
    suspend fun updatePassword(id: Int, newPassword: SaltedHash)
>>>>>>> development
    suspend fun deleteUser(id: Int)
}
