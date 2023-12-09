package com.example.dchannels.doa

import com.example.dchannels.Constants
import com.example.dchannels.Models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class UserDoaStore: UserDoa {
    companion object {
        private var instance: UserDoaStore? = null
        fun getInstance(): UserDoaStore {
            if(instance == null){
                synchronized(UserDoaStore::class){
                    if(instance == null){
                        instance = UserDoaStore()
                    }
                }
            }
            return instance!!
        }
        private var usersCollectionReference = FirebaseFirestore.getInstance()
            .collection(Constants.USERS_COLLECTION)
    }
    override fun addUser(user: User) {
        usersCollectionReference.document(user.id!!).set(user, SetOptions.merge())
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