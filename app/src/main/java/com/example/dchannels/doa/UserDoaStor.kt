package com.example.dchannels.doa

import com.example.dchannels.Models.User

class UserDoaStor: UserDoa {
    private var instance: UserDoaStor? = null
    fun getInstance(): UserDoaStor {
        if(instance == null){
            synchronized(UserDoaStor::class){
                if(instance == null){
                    instance = UserDoaStor()
                }
            }
        }
        return instance!!
    }
    override fun addUser(user: User) {
        TODO("Not yet implemented")
    }

    override fun getUser(user: User) {
        TODO("Not yet implemented")
    }

    override fun updateUser(user: User) {
        TODO("Not yet implemented")
    }

    override fun deleteUser(user: User) {
        TODO("Not yet implemented")
    }

    override fun getAllUsers(): List<User> {
        TODO("Not yet implemented")
    }
}