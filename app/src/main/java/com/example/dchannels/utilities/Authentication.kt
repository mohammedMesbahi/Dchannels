package com.example.dchannels.utilities

import android.os.Bundle
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.Toast
import com.example.dchannels.Constants
import com.example.dchannels.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class Authentication {
    var gso: GoogleSignInOptions
//    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        private var instance : Authentication? = null
        fun getInstance() : Authentication {
            if (instance == null) {
                instance = Authentication()
            }
            return instance!!
        }
        /*val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()*/


        /*fun createAccount(email: String, password: String): Task<AuthResult> {
            // [START create_user_with_email]
            return Firebase.auth.createUserWithEmailAndPassword(email, password)

        }*/
    }
    fun signIn(email: String, password: String): Task<AuthResult> {
        return FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)

    }
    fun singOut() {
        TODO()
    }
    fun firebaseAuthWithGoogle(idToken: String): Task<AuthResult> {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return FirebaseAuth.getInstance().signInWithCredential(credential)
    }
    init {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(Constants.default_web_client_id)
            .requestEmail()
            .build()
//        googleSignInClient = GoogleSignIn.getClient(null, gso)
    }
}