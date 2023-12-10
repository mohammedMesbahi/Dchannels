package com.example.dchannels.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.dchannels.Constants
import com.example.dchannels.Models.Admin
import com.example.dchannels.Models.User
import com.example.dchannels.databinding.ActivitySignInBinding
import com.example.dchannels.doa.AdminDoaStore
import com.example.dchannels.doa.UserDoaStore
import com.example.dchannels.utilities.Authentication
import com.example.dchannels.utilities.MyPreferences
import com.example.dchannels.utilities.Utilities
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : FullScreenActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient

    lateinit var binding: ActivitySignInBinding
    private lateinit var preferenceManager: MyPreferences

    var isLoading = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager = MyPreferences(this)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (FirebaseAuth.getInstance().currentUser != null) {
            startHomeActivity()
            finish()
        }

        googleSignInClient = GoogleSignIn.getClient(this, Authentication.getInstance().gso)

        initialiseFullScreen(binding.mainLayout)

        setListeners()
    }

    fun setListeners() {
        binding.signInButton.setOnClickListener {
            if (isValidSingInDetails()) {
                signIn()
            }
        }
        binding.googleSignInButton.setOnClickListener {
            signInWithGoogle()
        }
    }

    // Inside LoginActivity
    fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun signIn() {
        isLoading = true
        Utilities.toggleLoading(isLoading, binding.signInButton, binding.signInProgressBar)
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        Authentication.getInstance().signIn(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val loggedInUser = FirebaseAuth.getInstance().currentUser
                    if (loggedInUser != null) {
                        AdminDoaStore.getInstance().getAdminById(loggedInUser.uid)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val admin = task.result?.toObject(Admin::class.java)
                                    if (admin != null) {
                                        preferenceManager.setAdmin(admin)
                                        startHomeActivity()
                                    }
                                } else {
                                    Utilities.showToast(this, "${task.exception?.message}")
                                }
                            }
                    }
                } else {
                    // Handle errors (e.g., wrong credentials)
                    Utilities.showToast(this, "${task.exception?.message}")
                }
                isLoading = false
                Utilities.toggleLoading(
                    isLoading,
                    binding.signInButton,
                    binding.signInProgressBar
                )
            }
    }


    private fun isValidSingInDetails(): Boolean {
        if (binding.emailEditText.text.toString().isEmpty()
            || !android.util.Patterns.EMAIL_ADDRESS.matcher(binding.emailEditText.text.toString())
                .matches()
        ) {
            binding.emailEditText.error = "Please enter a valid email"
            return false
        }
        if (binding.passwordEditText.text.toString().isEmpty()) {
            binding.passwordEditText.error = "Please enter a password"
            return false
        }
        return true
    }

    companion object {
        private const val RC_SIGN_IN = 9001 // This can be any integer unique to the Activity

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account.idToken?.let {
                    Authentication.getInstance().firebaseAuthWithGoogle(it)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                val currentUser = FirebaseAuth.getInstance().currentUser
                                if (currentUser != null) {
                                    val user = User(
                                        currentUser.uid,
                                        currentUser.displayName!!,
                                        currentUser.email!!,
                                        currentUser.photoUrl.toString()
                                    )
                                    UserDoaStore.getInstance().getUser(user)
                                        .addOnCompleteListener { task ->
                                            // if user is not present in database then add user else start home activity
                                            if (task.isSuccessful) {
                                                val user = task.result?.toObject(User::class.java)
                                                if (user != null) {
                                                    preferenceManager.setUser(user)
                                                    startHomeActivity()
                                                } else {
                                                    val newUser = User(
                                                        currentUser.uid,
                                                        currentUser.displayName!!,
                                                        currentUser.email!!,
                                                        currentUser.photoUrl.toString(),
                                                        Constants.ROLE_USER
                                                    )
                                                    UserDoaStore.getInstance().addUser(newUser)
                                                        .addOnCompleteListener { task ->
                                                            if (task.isSuccessful) {
                                                                preferenceManager.setUser(newUser)
                                                                Utilities.showToast(
                                                                    this,
                                                                    "welcome ✨"
                                                                )
                                                                startHomeActivity()
                                                            } else {
                                                                Utilities.showToast(
                                                                    this,
                                                                    "Failed to add user ${task.exception?.message}"
                                                                )
                                                            }
                                                        }
                                                }
                                            } else {
                                                Utilities.showToast(
                                                    this,
                                                    "Failed to get user ${task.exception?.message}"
                                                )
                                            }

                                        }
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Utilities.showToast(
                                    this,
                                    "Failed to sign in with google ${task.exception?.message}"
                                )
                            }
                        }
                }
            } catch (e: ApiException) {
                Log.d("googleTag", "Google sign in failed", e)
            }
        }
    }

    private fun startHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

}