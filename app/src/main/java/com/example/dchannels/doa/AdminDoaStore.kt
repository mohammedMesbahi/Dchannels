package com.example.dchannels.doa

import android.net.Uri
import android.widget.Toast
import com.example.dchannels.Constants
import com.example.dchannels.Models.Admin
import com.example.dchannels.utilities.Utilities
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
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

        private val adminsCollectionReference =
            FirebaseDatabase.getInstance(Constants.REALTIME_DATABASE_URL)
                .getReference(Constants.ADMINS_COLLECTION)
    }

    override fun addAdmin(newAdmin: Admin): Task<Void> {
        return adminsCollectionReference.child(newAdmin.id!!).setValue(newAdmin)
    }


    override fun getAllAdmins(): List<Admin> {
        TODO("Not yet implemented")
    }

    override fun updateAdminImage(admin: Admin): Task<Void> {
        return adminsCollectionReference.child(admin.id!!).child(Constants.USER_PROFILE_IMAGE_FIELD)
            .setValue(admin.profileImage)
    }

    override fun getAdminById(id: String): Task<DataSnapshot> {
        return adminsCollectionReference.child(id).get()
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