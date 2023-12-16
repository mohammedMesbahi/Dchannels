package com.example.dchannels.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.dchannels.Models.Channel
import com.example.dchannels.databinding.ActivityAddChannelBinding
import com.example.dchannels.doa.ChannelDoaStore
import com.example.dchannels.foa.FileUtilities
import com.example.dchannels.utilities.Utilities
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.Timestamp
import java.util.*

class AddChannelActivity : FullScreenActivity() {
    private lateinit var binding: ActivityAddChannelBinding
    private var imagePickLauncher: ActivityResultLauncher<Intent>? = null
    private lateinit var selectedImageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedImageUri = Uri.EMPTY
        binding = ActivityAddChannelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialiseFullScreen(binding.mainLayout)
        imagePickLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null && data.data != null) {
                    selectedImageUri = data.data!!
                    Utilities.setImageIntoView(this, selectedImageUri, binding.ivChannelImage)
                }
            }
        }
        setListeners()
    }

    private fun setListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnAddChannel.setOnClickListener {
            val channelLabel = binding.etChannelLabel.text.toString()
            val channelDescription = binding.etChannelDescription.text.toString()
            if (channelLabel.isEmpty()) {
                Utilities.showToast(
                    this,
                    "Channel label is required"
                )
                return@setOnClickListener
            }

            val channel = Channel()
            channel.label = channelLabel
            channel.description = channelDescription
            channel.isPublic = binding.cbPublic.isChecked
//            channel.members = ArrayList()
//            channel.attachments = ArrayList()
            channel.timestamp = Timestamp(Date())
            ChannelDoaStore.getInstance().addChannel(channel).addOnCompleteListener {
                if (it.isSuccessful) {
                    Utilities.showToast(
                        this,
                        "Channel added successfully"
                    )
                    channel.id = it.result?.id!!
                    ChannelDoaStore.getInstance().updateChannel(channel)
                    channel.channelImage = channel.generateChannelImagePath()
                    if (selectedImageUri != Uri.EMPTY){
                        FileUtilities.getInstance()
                            .uploadFile(selectedImageUri, channel.channelImage!!)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    ChannelDoaStore.getInstance().updateChannelImage(channel)
                                } else {
                                    Utilities.showToast(
                                        this,
                                        "${it.exception?.message}"
                                    )
                                }
                            }
                    }
                    resetAllFields()
                } else {
                    Utilities.showToast(
                        this,
                        "${it.exception?.message}"
                    )
                }
            }
        }
        binding.ivChannelImage.setOnClickListener {
            ImagePicker.with(this)
                .cropSquare()
                .compress(512)
                .maxResultSize(512, 512)
                .createIntent { intent ->
                    imagePickLauncher?.launch(intent)
//                    Utilities.setImageIntoView(this, selectedImageUri, binding.ivChannelImage)
                }
        }

    }

    private fun resetAllFields() {
        binding.etChannelLabel.setText("")
        binding.etChannelDescription.setText("")
    }
}