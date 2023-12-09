package com.example.dchannels.doa

import com.example.dchannels.Constants
import com.example.dchannels.Models.Attachment
import com.example.dchannels.Models.Channel
import com.example.dchannels.doa.ChannelDoaStore.Companion.channelsCollectionReference
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class AttachmentDoaStore {
    companion object {
        private var instance: AttachmentDoaStore? = null
        fun getInstance(): AttachmentDoaStore {
            if (instance == null) {
                instance = AttachmentDoaStore()
            }
            return instance!!
        }

    }

    fun addAttachment(channel: Channel, attachment: Attachment): Task<DocumentReference> {
        return FirebaseFirestore.getInstance()
            .collection(Constants.CHANNELS_COLLECTION)
            .document(channel.id!!)
            .collection(Constants.ATTACHMENTS_COLLECTION).add(attachment)
    }

    fun getAllAttachmentsQuery(channel: Channel): Query {
        return channelsCollectionReference
            .document(channel.id!!)
            .collection(Constants.ATTACHMENTS_COLLECTION)
            .orderBy(Constants.ATTACHMENT_TIMESTAMP_FIELD, Query.Direction.DESCENDING)
    }
}