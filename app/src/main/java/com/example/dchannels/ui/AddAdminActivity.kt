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
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.dchannels.Constants
import com.example.dchannels.Models.Admin
import com.example.dchannels.databinding.ActivityAddAdminBinding
import com.example.dchannels.doa.AdminDoaStore
import com.example.dchannels.utilities.Utilities
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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
            if (isInputsValid()) {
                addAdmin()
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

    private fun addAdmin() {
        isLoading = !isLoading
        Utilities.toggleLoading(isLoading, binding.addBtn, binding.progressBar)

        var admin = Admin(
            binding.username.text.toString().trim(),
            binding.email.text.toString().trim(),
            binding.password.text.toString().trim(),
        )

        // Create admin account with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(admin.email, admin.password)
            .addOnCompleteListener(this) { task ->
                // Admin account created successfully
                if (task.isSuccessful) {
                    // Store additional details in Firestore
                    admin.id =  task.result?.user?.uid
                    val adminsCollection = FirebaseFirestore.getInstance().collection(Constants.ADMINS_COLLECTION)
                    val adminData = hashMapOf(
                        Constants.USER_NAME_FIELD to admin.name,
                        Constants.USER_EMAIL_FIELD to admin.email,
                        Constants.USER_PASSWORD_FIELD to admin.password,
                        Constants.USER_PROFILE_IMAGE_FIELD to admin.id,
                        // Add other fields as needed
                    )
                    admin.id?.let {
                        adminsCollection.document(it).set(adminData)
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    // Admin data stored successfully
                                    // You can navigate to the admin dashboard or perform other actions
                                    Toast.makeText(baseContext, "Admin added successfully", Toast.LENGTH_SHORT).show()
                                    if (selectedImageUri != Uri.EMPTY) {
                                        Utilities.uploadImageToCloudStorage(admin, selectedImageUri as Uri)
                                            .addOnCompleteListener(this) {
                                                if (task.isSuccessful) {
                                                    Toast.makeText(baseContext, "image added successfully", Toast.LENGTH_SHORT).show()
                                                } else {
                                                    // Handle unsuccessful uploads
                                                    Utilities.showToast(this, "failed to upload image")
                                                }
                                                resetAllFields()
                                            }
                                    } else {
                                        admin.profileImage = ""
                                    }
                                }
                                else {
                                    Toast.makeText(baseContext, "Failed to store admin data: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                }
                else {
                    // If registration fails, display a message to the user.
                    Toast.makeText(baseContext, "Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
                isLoading = !isLoading
                Utilities.toggleLoading(isLoading, binding.addBtn, binding.progressBar)
            }
    }

    private fun isInputsValid(): Boolean {
        if (binding.username.text.toString().trim().isEmpty()) {
            binding.username.error = "Username is required"
            return false
        } else if (binding.email.text.toString().trim().isEmpty()
            || !android.util.Patterns.EMAIL_ADDRESS.matcher(
                binding.email.text.toString().trim()
            ).matches()
        ) {
            binding.email.error = " A valid Email is required"
            return false
        } else if (binding.password.text.toString().trim().isEmpty()) {
            binding.password.error = "Password is required"
            return false
        } else if (binding.confirmPassword.text.toString().trim().isEmpty()) {
            binding.confirmPassword.error = "Confirm Password is required"
            return false
        } else if (binding.password.text.toString()
                .trim() != binding.confirmPassword.text.toString().trim()
        ) {
            binding.confirmPassword.error = "Password and Confirm Password must be same"
            return false
        }
        return true

    }

    private fun resetAllFields() {
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