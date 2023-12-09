package com.example.dchannels.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.dchannels.Constants
import com.example.dchannels.Models.Admin
import com.example.dchannels.databinding.ActivityAddAdminBinding
import com.example.dchannels.doa.AdminDoaStore
import com.example.dchannels.utilities.MyPreferences
import com.example.dchannels.utilities.Utilities
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.firestore.FirebaseFirestore

class AddAdminActivity : FullScreenActivity() {

    private var togglePasswordVisibility: Boolean = false
    private lateinit var binding: ActivityAddAdminBinding
    private var isLoading: Boolean = false
    private lateinit var selectedImageUri: Any
    private lateinit var loggedInAdmin: Admin
    private var imagePickLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedImageUri = Uri.EMPTY

        loggedInAdmin = MyPreferences(this).getAdmin()

        binding = ActivityAddAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialiseFullScreen(binding.mainLayout)

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
                isLoading = !isLoading
                Utilities.toggleLoading(isLoading, binding.addBtn, binding.progressBar)

                var newAdmin = Admin()
                newAdmin.name = binding.username.text.toString()
                newAdmin.email = binding.email.text.toString()
                newAdmin.password = binding.password.text.toString()

                // Create admin account with email and password
                loggedInAdmin.createAccountForAdmin(newAdmin)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            newAdmin.id =  task.result?.user?.uid
                            loggedInAdmin.addAdmin(newAdmin).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(baseContext, "Admin added successfully", Toast.LENGTH_SHORT).show()
                                    if (selectedImageUri != Uri.EMPTY) {
                                        Utilities.uploadImageToCloudStorage(newAdmin, selectedImageUri as Uri)
                                            .addOnCompleteListener {
                                                if (task.isSuccessful) {
                                                    Toast.makeText(baseContext, "image added successfully", Toast.LENGTH_SHORT).show()
                                                    newAdmin.profileImage = newAdmin.id
                                                    AdminDoaStore.getInstance().updateAdminImage(newAdmin)
                                                } else {
                                                    // Handle unsuccessful uploads
                                                    Utilities.showToast(this, "failed to upload image ${task.exception?.message} ")
                                                }
                                                resetAllFields()
                                            }
                                    } else {
                                        newAdmin.profileImage = ""
                                    }
                                }
                                else {
                                    Toast.makeText(baseContext, "Failed to store admin data: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
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

}