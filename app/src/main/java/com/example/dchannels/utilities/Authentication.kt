package com.example.dchannels.utilities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.dchannels.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class Authentication {
    companion object {
        fun signIn(email: String, password: String): Task<QuerySnapshot> {
            return FirebaseFirestore.getInstance().collection(Constants.ADMINS_COLLECTION)
                .whereEqualTo(Constants.USER_EMAIL_FIELD, email).whereEqualTo(Constants.USER_PASSWORD_FIELD, password)
                .get()

        }

        fun singOut() {
            TODO()
        }

        /*fun createAccount(email: String, password: String): Task<AuthResult> {
            // [START create_user_with_email]
            return Firebase.auth.createUserWithEmailAndPassword(email, password)

        }*/

    }
}