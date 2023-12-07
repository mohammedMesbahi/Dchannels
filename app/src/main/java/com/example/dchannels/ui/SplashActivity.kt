package com.example.dchannels.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import com.example.dchannels.databinding.ActivitySplashBinding


class SplashActivity : FullScreenActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialiseFullScreen(binding.mainLayout)
        // Delay in milliseconds
        val delayMillis: Long = 2000

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()  // Optional: if you want to close the current activity
        }, delayMillis)

    }
}