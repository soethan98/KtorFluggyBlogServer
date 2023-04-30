package com.example.data.repository

import com.example.domain.model.User
import com.example.domain.repository.UserDataSource
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class UserDataSourceImpl(database:CoroutineDatabase):UserDataSource {

    private val users = database.getCollection<User>()
    override suspend fun getUserInfo(userId: String): User? {
        return users.findOne(filter = User::id eq userId)
    }

    override suspend fun saveUserInfo(user: User): Boolean {
       val existingUser = users.findOne(filter = User::id eq user.id)
        return if (existingUser == null){
            users.insertOne(document = user).wasAcknowledged()
        }else{
            true
        }
    }



    override suspend fun deleteUser(userId: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserInfo(userId: String, firstName: String, lastName: String): Boolean {
        TODO("Not yet implemented")
    }
}