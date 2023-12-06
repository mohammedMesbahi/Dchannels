package com.example.dchannels.ui

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import com.example.dchannels.Constants
import com.example.dchannels.Models.Admin
import com.example.dchannels.R
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
import com.google.firebase.firestore.FirebaseFirestore

class SignInActivity : AppCompatActivity() {

    private lateinit var mainLayout: View
    private val hideHandler = Handler(Looper.getMainLooper())
    private var isFullscreen: Boolean = false
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
                    // Login successful, proceed to dashboard
                    val loggedInUser = FirebaseAuth.getInstance().currentUser
                    if (loggedInUser != null) {
                        AdminDoaStore.getInstance().getAdminById(loggedInUser.uid)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    if (task.result != null && task.result?.exists()!!) {
                                        val admin = Admin(
                                            task.result?.id as String,
                                            task.result?.get(Constants.USER_NAME_FIELD).toString(),
                                            task.result?.get(Constants.USER_EMAIL_FIELD).toString(),
                                            task.result?.get(Constants.USER_PROFILE_IMAGE_FIELD)
                                                .toString(),
                                            task.result?.get(Constants.USER_PASSWORD_FIELD)
                                                .toString(),
                                        )
                                        Utilities.showToast(this, "Welcome")
                                        preferenceManager.setAdmin(admin)
                                        startHomeActivity()
                                        finish()
                                    } else {
                                        Utilities.showToast(
                                            this,
                                            "Error fetching admin details"
                                        )
                                    }
                                } else {
                                    Utilities.showToast(
                                        this,
                                        "Error fetching admin details"
                                    )
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
            binding.passwordEditText.error = "Please enter password"
            return false
        }
        return true
    }


    private fun toggle() {
        if (isFullscreen) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        // Hide UI first
        supportActionBar?.hide()
        isFullscreen = false

        if (Build.VERSION.SDK_INT >= 30) {
            // Use WindowInsetsController for API 30 and above
            mainLayout.windowInsetsController?.let {
                it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            // Use deprecated methods for older API versions
            mainLayout.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        }
    }

    private fun show() {
        // Show the system bar
        if (Build.VERSION.SDK_INT >= 30) {
            mainLayout.windowInsetsController?.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {
            mainLayout.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        }
        isFullscreen = true

        // Show the ActionBar
        supportActionBar?.show()
    }

    /**
     * Schedules a call to hide() in delayMillis, canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        hideHandler.removeCallbacksAndMessages(null)
        hideHandler.postDelayed({ hide() }, delayMillis.toLong())
    }

    companion object {
        private const val UI_ANIMATION_DELAY = 300
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
                    // Update your UI here
                } else {
                    // If sign in fails, display a message to the user.
                }
            }
    }

    private fun startHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

}