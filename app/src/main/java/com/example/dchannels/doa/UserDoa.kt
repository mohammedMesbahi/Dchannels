package com.example.dchannels.doa

import com.example.dchannels.Models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query

interface UserDoa {
    fun addUser(user: User):Task<Void>
    fun getUser(user: User): Task<DocumentSnapshot>
    fun updateUser(user: User)
    fun deleteUser(user: User)
    fun getAllUsers():List<User>
    fun getAllUsersQuery(): Query
    fun changeRole(user: User, role: String):Task<Void>
}