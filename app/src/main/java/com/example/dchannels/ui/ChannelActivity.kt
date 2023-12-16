package com.example.dchannels.ui

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.dchannels.Constants
import com.example.dchannels.Models.Attachment
import com.example.dchannels.Models.Channel
import com.example.dchannels.Models.User
import com.example.dchannels.R
import com.example.dchannels.adapters.AttachmentRecyclerAdapter
import com.example.dchannels.databinding.ActivityChannelBinding
import com.example.dchannels.doa.AttachmentDoaStore
import com.example.dchannels.doa.ChannelDoaStore
import com.example.dchannels.foa.FileUtilities
import com.example.dchannels.utilities.MyPreferences
import com.example.dchannels.utilities.Utilities
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp

class ChannelActivity : FullScreenActivity() {
    private lateinit var binding: ActivityChannelBinding
    private lateinit var channel: Channel
    private lateinit var loggedInUser: User
    private lateinit var myPreferences: MyPreferences
    private lateinit var adapter: AttachmentRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChannelBinding.inflate(layoutInflater)
        channel = Channel()
        channel.id = intent.getStringExtra(Constants.CHANNEL_ID_FIELD)
        channel.label = intent.getStringExtra(Constants.CHANNEL_LABEL_FIELD)
        channel.description = intent.getStringExtra(Constants.CHANNEL_DESCRIPTION_FIELD)
        channel.channelImage = intent.getStringExtra(Constants.CHANNEL_IMAGE_FIELD)
        myPreferences = MyPreferences(this)
        loggedInUser = myPreferences.getUser()
        setContentView(binding.root)
        initialiseFullScreen(binding.mainLayout)
        setupRecyclerView()
        binding.channelLabel.text = channel.label
        binding.tvChannelDescription.text = channel.description
        binding.messageSendBtn.setOnClickListener {
            val textMessage = binding.chatMessageInput.text.toString().trim()
            if (textMessage.isNotEmpty()) {
                var newTextMessage = Attachment()
                newTextMessage.text = textMessage
                newTextMessage.sender = loggedInUser
                newTextMessage.type = Constants.ATTACHMENT_TYPE_TEXT
                newTextMessage.timestamp = Timestamp.now()
                AttachmentDoaStore.getInstance()
                    .addAttachment(channel, newTextMessage)
                    .addOnSuccessListener {it ->
                        // update channel last message
                        newTextMessage.id = it.id
                        ChannelDoaStore.getInstance().updateChannelLastMessage(channel,newTextMessage)
                    }
                    .addOnFailureListener {
                        Utilities.showToast(this, "Failed to send message ${it.message}")
                    }
                binding.chatMessageInput.setText("")

            }
//
        }
        if (channel.channelImage == null || channel.channelImage!!.isEmpty()) {
            binding.channelPicImageView.setImageResource(R.drawable.sharp_groups_24)
        } else {
            FileUtilities.getInstance().downloadFile(channel.channelImage!!).addOnSuccessListener { uri ->
                Glide.with(this)
                    .load(uri)
                    .apply(RequestOptions.circleCropTransform())
                    .placeholder(R.drawable.sharp_groups_24) // Placeholder image
                    .into(binding.channelPicImageView)
            }.addOnFailureListener {
            }
        }
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    fun setupRecyclerView() {
        val query = AttachmentDoaStore.getInstance().getAllAttachmentsQuery(channel)
        val options = FirestoreRecyclerOptions.Builder<Attachment>()
            .setQuery(query, Attachment::class.java).build()
        adapter = AttachmentRecyclerAdapter(options, this)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        binding.chatRecyclerView.layoutManager = layoutManager
        binding.chatRecyclerView.adapter = adapter
        adapter.startListening()
    }

    override fun onStart() {
        super.onStart()
        if (adapter != null) adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        if (adapter != null) adapter.stopListening()
    }

    override fun onResume() {
        super.onResume()
        if (adapter != null) adapter.notifyDataSetChanged()
    }
}