package com.example.dchannels.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.dchannels.Models.Admin
import com.example.dchannels.databinding.ActivityAddAdminBinding
import com.example.dchannels.doa.AdminDoaStore
import com.example.dchannels.utilities.Utilities
import com.github.dhaval2404.imagepicker.ImagePicker

class AddAdminActivity : AppCompatActivity() {

    private lateinit var mainLayout: View
    private val hideHandler = Handler(Looper.getMainLooper())
    private var isFullscreen: Boolean = false
    private var togglePasswordVisibility: Boolean = false
    private lateinit var binding: ActivityAddAdminBinding
    private var isLoading: Boolean = false
    private lateinit var selectedImageUri: Any

    var imagePickLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedImageUri = Uri.EMPTY
        binding = ActivityAddAdminBinding.inflate(layoutInflater)
        setContentView(binding.root) // replace with your layout resource
        mainLayout = binding.mainLayout // replace with your layout ID
        isFullscreen = true
        // Set up the user interaction to manually show or hide the system UI.
        mainLayout.setOnClickListener { toggle() }
        // Trigger the initial hide() shortly after the activity has been created.
        delayedHide(100)

        imagePickLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null && data.data != null) {
                    selectedImageUri = data.data!!
                    Utilities.setProfilePic(this, selectedImageUri as Uri, binding.profilePic)
                }
            }
        }
        setListeners()

    }

    private fun setListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.checkBoxShowPassword.setOnClickListener {
            if (togglePasswordVisibility) {
                binding.password.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.confirmPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                togglePasswordVisibility = false
            } else {
                binding.password.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.confirmPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                togglePasswordVisibility = true
            }
        }
        binding.addBtn.setOnClickListener {
            if (binding.username.text.toString().trim().isEmpty()) {
                binding.username.error = "Username is required"
                return@setOnClickListener
            } else if (binding.email.text.toString().trim().isEmpty()
                || !android.util.Patterns.EMAIL_ADDRESS.matcher(
                    binding.email.text.toString().trim()
                ).matches()
            ) {
                binding.email.error = " A valid Email is required"
                return@setOnClickListener
            } else if (binding.password.text.toString().trim().isEmpty()) {
                binding.password.error = "Password is required"
                return@setOnClickListener
            } else if (binding.confirmPassword.text.toString().trim().isEmpty()) {
                binding.confirmPassword.error = "Confirm Password is required"
                return@setOnClickListener
            } else if (binding.password.text.toString()
                    .trim() != binding.confirmPassword.text.toString().trim()
            ) {
                binding.confirmPassword.error = "Password and Confirm Password must be same"
                return@setOnClickListener
            } else {
                isLoading = !isLoading
                Utilities.toggleLoading(isLoading, binding.addBtn, binding.progressBar)
                var admin = Admin(
                    binding.username.text.toString().trim(),
                    binding.email.text.toString().trim(),
                    binding.password.text.toString().trim(),
                )
                AdminDoaStore.getInstance().addAdmin(admin)
                    .addOnSuccessListener { documentReference ->
                        admin.id = documentReference.id
                        // add image to cloud storage
                        if (selectedImageUri != Uri.EMPTY) {
                            Utilities.uploadImageToCloudStorage(
                                admin,
                                selectedImageUri!! as Uri
                            )
                                .addOnSuccessListener { taskSnapshot ->
                                    resetAllFields()
                                    // Task completed successfully
                                    admin.profileImage =
                                        taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                                    AdminDoaStore.getInstance().updateAdminImage(admin)
                                        .addOnSuccessListener {
                                            Utilities.showToast(
                                                this,
                                                "Admin added successfully"
                                            )
                                        }
                                        .addOnFailureListener { exception ->
                                            Utilities.showToast(
                                                this,
                                                "Error adding admin ${exception.message}"
                                            )
                                        }
                                }
                                .addOnFailureListener {
                                    // Handle unsuccessful uploads
                                    Utilities.showToast(this, "failed to upload image")
                                }
                        } else {
                            admin.profileImage =
                                Uri.parse("android.resource://com.example.dchannels/drawable/profile_pic")
                        }
                    }
                    .addOnFailureListener { exception ->
                        resetAllFields()
                        Utilities.showToast(this, "Error adding admin ${exception.message}")
                    }
            }

        }
        binding.profilePic.setOnClickListener {
            ImagePicker.with(this)
                .cropSquare()
                .compress(512)
                .maxResultSize(512, 512)
                .createIntent { intent ->
                    imagePickLauncher?.launch(intent)
                    Utilities.setProfilePic(this, selectedImageUri, binding.profilePic)
                }
        }

    }
    fun resetAllFields() {
        isLoading = !isLoading
        Utilities.toggleLoading(
            isLoading,
            binding.addBtn,
            binding.progressBar
        )
        binding.username.text.clear()
        binding.email.text.clear()
        binding.password.text.clear()
        binding.confirmPassword.text.clear()
        binding.profilePic.setImageResource(com.example.dchannels.R.drawable.ic_profile)
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
    }

}