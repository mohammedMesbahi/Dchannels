package com.example.dchannels.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.dchannels.Constants
import com.example.dchannels.Models.Admin
import com.example.dchannels.databinding.ActivitySignInBinding
import com.example.dchannels.doa.AdminDoaStore
import com.example.dchannels.utilities.Authentication
import com.example.dchannels.utilities.MyPreferences
import com.example.dchannels.utilities.Utilities
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class SignInActivity : FullScreenActivity() {

    private var gso: GoogleSignInOptions? = null
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
        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(Constants.default_web_client_id)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)


        mainLayout = binding.mainLayout // replace with your layout ID
        isFullscreen = true
        // Set up the user interaction to manually show or hide the system UI.
        mainLayout.setOnClickListener { toggle() }
        // Trigger the initial hide() shortly after the activity has been created.
        delayedHide(100)

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

        Authentication.signIn(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val loggedInUser = FirebaseAuth.getInstance().currentUser
                    if (loggedInUser != null) {
                        AdminDoaStore.getInstance().getAdminById(loggedInUser.uid)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val admin = task.result?.toObject(Admin::class.java)
                                    Log.d("admin", "admin: $admin")
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
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.d("googleTag", "Google sign in failed", e)
            }
        }
    }

    // Inside LoginActivity
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = FirebaseAuth.getInstance().currentUser
                    preferenceManager.setUser(user)
                    startHomeActivity()

                } else {
                    // If sign in fails, display a message to the user.
                    Utilities.showToast(this, "Failed to sign in with google ${task.exception?.message}")
                }
            }
    }

    private fun startHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

}