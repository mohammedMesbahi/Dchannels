package com.example.dchannels.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import com.example.dchannels.R

open class FullScreenActivity : AppCompatActivity() {
    lateinit var mainLayout: View
    val hideHandler = Handler(Looper.getMainLooper())
    var isFullscreen: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen)
    }

    fun toggle() {
        if (isFullscreen) {
            hide()
        } else {
            show()
        }
    }

    fun hide() {
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

    fun show() {
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
    fun delayedHide(delayMillis: Int) {
        hideHandler.removeCallbacksAndMessages(null)
        hideHandler.postDelayed({ hide() }, delayMillis.toLong())
    }
    fun initialiseFullScreen(mainLayout:View){
        this.mainLayout = mainLayout // replace with your layout ID
        isFullscreen = true
        // Set up the user interaction to manually show or hide the system UI.
        mainLayout.setOnClickListener { toggle() }
        // Trigger the initial hide() shortly after the activity has been created.
        delayedHide(UI_ANIMATION_DELAY)
    }
    companion object {
        const val UI_ANIMATION_DELAY = 300
    }
}