package com.example.dchannels.ui

import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.dchannels.Constants
import com.example.dchannels.adapters.ViewPagerAdapter
import com.example.dchannels.databinding.ActivityHomeBinding
import com.example.dchannels.utilities.MyPreferences
import com.example.dchannels.utilities.Utilities
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : FullScreenActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var preferenceManager: MyPreferences
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager = MyPreferences(this)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialiseFullScreen(binding.mainLayout)
        populateViews()
        setListens()
        // Configure sign-in to request the user's ID, email address, and basic profile. ID and
        // basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(Constants.default_web_client_id)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        binding.viewPager.adapter = ViewPagerAdapter(this)
        // Link the TabLayout and the ViewPager2 together and synchronize their scroll
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "CHANNELS"
                1 -> "ADMINS"
                2 -> "USERS"
                else -> null
            }
        }.attach()
    }

    private fun setListens() {
        binding.logoutButton.setOnClickListener {
            logout()
        }

    }

    private fun populateViews() {
        binding.nameTextView.text = preferenceManager.name
        if (preferenceManager.role == Constants.ROLE_ADMIN) {
            preferenceManager.profileImage?.let { Utilities.loadProfileImageIntoView(binding.profileImageView, it) }

        } else {
            Glide.with(this)
                .load(FirebaseAuth.getInstance().currentUser?.photoUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.profileImageView)

        }
        if(preferenceManager.role == Constants.ROLE_ADMIN) {
            binding.adminImageView.visibility = android.view.View.VISIBLE
        } else {
            binding.adminImageView.visibility = android.view.View.GONE
        }
    }

    private fun updateToken(token: String) {
        FirebaseFirestore.getInstance().collection(Constants.ADMINS_COLLECTION)
            .document(preferenceManager.id).update(Constants.FCM_TOKEN_FIELD, token)
            .addOnSuccessListener { Utilities.showToast(this, "Token updated successfully") }
            .addOnFailureListener { e ->
                Utilities.showToast(
                    this,
                    "Failed to update Token ${e.message}"
                )
            }
    }

    private fun logout() {
        // Firebase sign out
        FirebaseAuth.getInstance().signOut()
// Google sign out
        googleSignInClient.signOut().addOnCompleteListener(this) {
            // After signing out, you may want to update your UI to reflect that the user is no longer signed in
            // Navigate back to the SignInActivity, or update the UI accordingly
            preferenceManager.id = ""
            preferenceManager.name = ""
            preferenceManager.email = ""
            preferenceManager.profileImage = ""
            preferenceManager.role = ""
            Utilities.showToast(this, "Logged out successfully")
            startLoginActivity()
            finish()
        }

    }

    private fun startLoginActivity() {
        startActivity(Intent(this, SignInActivity::class.java))
    }
}