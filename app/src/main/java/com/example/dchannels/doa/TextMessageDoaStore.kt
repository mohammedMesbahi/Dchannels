package com.example.dchannels.doa

import com.example.dchannels.Constants
import com.example.dchannels.Models.Channel
import com.example.dchannels.Models.TextMessage
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class TextMessageDoaStore : AttachmentDoa {
    companion object {
        private var instance: TextMessageDoaStore? = null
        fun getInstance(): TextMessageDoaStore {
            if (instance == null) {
                instance = TextMessageDoaStore()
            }
            return instance!!
        }

    }

    fun addTextMessage(channel: Channel, textMessage: TextMessage): Task<DocumentReference> {
        return FirebaseFirestore.getInstance()
            .collection(Constants.CHANNELS_COLLECTION)
            .document(channel.id!!)
            .collection(Constants.ATTACHMENTS_COLLECTION).add(textMessage)
    }

}