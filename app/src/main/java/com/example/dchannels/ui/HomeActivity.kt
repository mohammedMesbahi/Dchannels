package com.example.dchannels.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dchannels.Constants
import com.example.dchannels.R
import com.example.dchannels.databinding.ActivityHomeBinding
import com.example.dchannels.utilities.MyPreferences
import com.example.dchannels.utilities.Utilities

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var preferenceManager: MyPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager = MyPreferences(this)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        populateViews()
    }

    private fun populateViews() {
        binding.nameTextView.text = preferenceManager.name
        preferenceManager.id?.let { Utilities.loadImageIntoView(binding.profileImageView, it) }
    }
}