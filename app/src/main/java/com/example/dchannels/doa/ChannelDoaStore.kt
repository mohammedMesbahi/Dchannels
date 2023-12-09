package com.example.dchannels.doa

import com.example.dchannels.Constants
import com.example.dchannels.Models.Channel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Query
class ChannelDoaStore : ChannelDoa {
    companion object {
        private var instance: ChannelDoaStore? = null
        fun getInstance(): ChannelDoaStore {
            if (instance == null) {
                synchronized(UserDoaStore::class) {
                    if (instance == null) {
                        instance = ChannelDoaStore()
                    }
                }
            }
            return instance!!
        }
        private var channelsCollectionReference = FirebaseFirestore.getInstance()
            .collection(Constants.CHANNELS_COLLECTION)
    }
    fun addChannel(channel: Channel): Task<DocumentReference> {
        return channelsCollectionReference.add(channel)
    }

    override fun updateChannel(channel: Channel): Task<Void> {
        return channelsCollectionReference.document(channel.id!!).set(channel, SetOptions.merge())
    }
    fun getAllChannelsQuery(): Query {
//        return channelsCollectionReference.orderBy(Constants.CHANNEL_LAST_MESSAGE_FIELD, Query.Direction.DESCENDING)
        return channelsCollectionReference.orderBy(Constants.CHANNEL_TIMESTAMP_FIELD, Query.Direction.DESCENDING)
    }

}