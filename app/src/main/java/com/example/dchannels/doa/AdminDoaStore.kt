package com.example.dchannels.doa

import com.example.dchannels.Constants
import com.example.dchannels.Models.Admin
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class AdminDoaStore : AdminDoa {
    companion object {
        private var instance: AdminDoaStore? = null
        fun getInstance(): AdminDoaStore {
            if (instance == null) {
                synchronized(UserDoaStor::class) {
                    if (instance == null) {
                        instance = AdminDoaStore()
                    }
                }
            }
            return instance!!
        }
    }

    override fun addAdmin(admin: Admin): Task<DocumentReference> {
        var adminMap = mutableMapOf<String, Any>()

        adminMap[Constants.USER_NAME_FIELD] = admin.name
        adminMap[Constants.USER_EMAIL_FIELD] = admin.email
        adminMap[Constants.USER_PASSWORD_FIELD] = admin.password
        adminMap[Constants.USER_ROLE_FIELD] = admin.role

        val db = FirebaseFirestore.getInstance()
        return db.collection(Constants.ADMINS_COLLECTION).add(adminMap)
    }

    override fun getAllAdmins(): List<Admin> {
        TODO("Not yet implemented")
    }

    override fun updateAdminImage(admin: Admin): Task<Void> {
        var map = mutableMapOf<String, Any>()
        map[Constants.USER_PROFILE_IMAGE_FIELD] = admin.profileImage
        return FirebaseFirestore.
        getInstance().
        collection(Constants.ADMINS_COLLECTION).
        document(admin.id!!).update(map)
    }

    override fun getAdminById(id: String): Task<DocumentSnapshot> {
        return FirebaseFirestore.getInstance().collection(Constants.ADMINS_COLLECTION)
            .document(id).get()
    }

    fun addModerator() {
        TODO()
    }

    fun removeModerator() {
        TODO()
    }

}