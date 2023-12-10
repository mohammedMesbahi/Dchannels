package com.example.dchannels.utilities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.dchannels.Constants
import com.example.dchannels.Models.Channel
import com.example.dchannels.Models.User
import com.example.dchannels.R
import com.example.dchannels.foa.FileUtilities
import com.google.firebase.Timestamp
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.text.SimpleDateFormat
import java.util.*

class Utilities {
    companion object {
        fun toggleLoading(isLoading: Boolean, button: Button, progressBar: ProgressBar) {
            if (isLoading) {
                button.visibility = View.INVISIBLE
                progressBar.visibility = View.VISIBLE
            } else {
                button.visibility = View.VISIBLE
                progressBar.visibility = View.INVISIBLE
            }
        }

        fun setProfilePic(activity: Activity, selectedImageUri: Any, profilePic: ImageView) {
            Glide.with(activity).load(selectedImageUri).apply(RequestOptions.circleCropTransform())
                .into(profilePic)
        }

        fun showToast(context: AppCompatActivity, s: String) {
            Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
        }

        fun uploadImageToCloudStorage(
            user: User,
            imageUri: Uri
        ): UploadTask {
            val storage = FirebaseStorage.getInstance()
            // Create a storage reference
            val storageRef = storage.reference
            // Create a reference to 'images/specificId.jpg'
            val imageRef = storageRef.child(Constants.FOLDER_PROFILE_PICS).child(user.id!!)
            // Upload the file to Firebase Storage

            return imageRef.putFile(imageUri)
        }

        fun loadProfileImageIntoView(imageView: ImageView, path: String) {
            if (path != null && path.isNotEmpty()) {
                // Download and display the image using Glide
                FileUtilities.getInstance().downloadFile(path).addOnSuccessListener { uri ->
                    Glide.with(imageView.context)
                        .load(uri)
                        .apply(RequestOptions.circleCropTransform())
                        .placeholder(R.drawable.person_icon) // Placeholder image
                        .into(imageView)
                }.addOnFailureListener {
                    // Handle any errors
                    showToast(
                        imageView.context as AppCompatActivity,
                        "Image load failed ${it.message}"
                    )
                }
            }

        }

        fun passChannelToIntent(intent: Intent, model: Channel) {
            intent.putExtra(Constants.CHANNEL_ID_FIELD, model.id)
            intent.putExtra(Constants.CHANNEL_LABEL_FIELD, model.label)
            intent.putExtra(Constants.CHANNEL_DESCRIPTION_FIELD, model.description)
        }

        fun foramatDate(timestamp: Timestamp): String {
            val date = timestamp.toDate()

            val dateFormat = SimpleDateFormat("MM-dd HH:mm", Locale.getDefault())
            return dateFormat.format(date)
        }

    }
}