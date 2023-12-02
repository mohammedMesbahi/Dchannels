package com.example.dchannels.doa

import com.example.dchannels.Models.User

interface UserDoa {
    fun addUser(user: User)
    fun getUser(user: User)
    fun updateUser(user: User)
    fun deleteUser(user: User)
    fun getAllUsers():List<User>
}