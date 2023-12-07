package com.example.dchannels.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.dchannels.databinding.ActivitySplashBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SplashActivity : FullScreenActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialiseFullScreen(binding.mainLayout)
        // Delay in milliseconds
        val delayMillis: Long = 2000

        FirebaseApp.initializeApp(this)
        Firebase.database.setPersistenceEnabled(true)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()  // Optional: if you want to close the current activity
        }, delayMillis)

    }
}