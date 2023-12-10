package com.example.dchannels.doa

import com.example.dchannels.Constants
import com.example.dchannels.Models.Admin
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class AdminDoaStore : AdminDoa {
    companion object {
        private var instance: AdminDoaStore? = null
        fun getInstance(): AdminDoaStore {
            if (instance == null) {
                synchronized(UserDoaStore::class) {
                    if (instance == null) {
                        instance = AdminDoaStore()
                    }
                }
            }
            return instance!!
        }

        val adminsCollectionReference = FirebaseFirestore.getInstance()
            .collection(Constants.ADMINS_COLLECTION)
//            FirebaseDatabase.getInstance(Constants.REALTIME_DATABASE_URL)
//                .getReference(Constants.ADMINS_COLLECTION)
    }

    override fun addAdmin(newAdmin: Admin): Task<Void> {
        return adminsCollectionReference.document(newAdmin.id!!).set(newAdmin)
    }


    override fun getAllAdmins(): List<Admin> {
        TODO("Not yet implemented")
    }

    override fun updateAdminImage(admin: Admin): Task<Void> {
        return adminsCollectionReference.document(admin.id!!).update(Constants.USER_PROFILE_IMAGE_FIELD,admin.profileImage)
    }

    override fun getAdminById(id: String): Task<DocumentSnapshot> {
        return adminsCollectionReference.document(id).get()
    }

    override fun getAllAdminsQuery(): Query {
        return adminsCollectionReference.orderBy(Constants.USER_NAME_FIELD, Query.Direction.ASCENDING)
    }

    fun addModerator() {
        TODO()
    }

    fun removeModerator() {
        TODO()
    }

    fun createAccountForAdmin(newAdmin: Admin): Task<AuthResult> {
        return FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(newAdmin.email!!, newAdmin.password!!)
    }
}