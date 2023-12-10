package com.example.dchannels.doa

import com.example.dchannels.Constants
import com.example.dchannels.Models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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
    override fun addUser(user: User): Task<Void> {
        return usersCollectionReference.document(user.id!!).set(user, SetOptions.merge())
    }

    override fun getUser(user: User): Task<DocumentSnapshot> {
        return usersCollectionReference.document(user.id!!).get()
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

    override fun getAllUsersQuery(): Query {
        return usersCollectionReference.orderBy(Constants.USER_NAME_FIELD, Query.Direction.ASCENDING)
    }
}